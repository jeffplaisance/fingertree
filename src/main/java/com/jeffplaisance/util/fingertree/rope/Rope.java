package com.jeffplaisance.util.fingertree.rope;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.jeffplaisance.util.fingertree.Empty;
import com.jeffplaisance.util.fingertree.FingerTree;
import com.jeffplaisance.util.fingertree.Measured;
import com.jeffplaisance.util.fingertree.Single;
import com.jeffplaisance.util.fingertree.Split;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

public class Rope implements CharSequence {

    private static final int COPY_THRESHOLD = 64;

    private static final Measured<Integer, String> STRING_LENGTH_MEASURED = new Measured<Integer, String>() {
        @Override
        public Integer sum(Integer a, Integer b) {
            return a+b;
        }

        @Override
        public Integer measure(String a) {
            return a.length();
        }

        @Override
        public Integer zero() {
            return 0;
        }
    };

    private final FingerTree<Integer, String> data;

    public static Rope fromString(String str) {
        return new Rope(new Single<Integer, String>(str, STRING_LENGTH_MEASURED));
    }

    public static Rope empty() {
        return new Rope(new Empty<Integer, String>(STRING_LENGTH_MEASURED));
    }

    private Rope(FingerTree<Integer, String> data) {
        this.data = data;
    }

    @Override
    public int length() {
        return data.measure();
    }

    @Override
    public char charAt(final int index) {
        final Split<Integer, String> split = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > index;
            }
        }, 0);
        return split.getElement().charAt(index - split.getHead().measure());
    }

    @Override
    public Rope subSequence(final int start, final int end) {
        final Split<Integer,String> split1 = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > start;
            }
        }, 0);
        final int headLength = split1.getHead().measure();
        final FingerTree<Integer, String> substring = split1.getTail().addFirst(split1.getElement()).splitLeft(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer >= end - headLength;
            }
        }, 0, true);
        final int substringLen = substring.measure();
        final FingerTree<Integer, String> substring2 = substring.removeFirst().addFirst(substring.first().substring(start - headLength, substring.first().length()));
        final FingerTree<Integer, String> substring3 = substring2.removeLast().addLast(substring2.last().substring(0, substring2.last().length() - (headLength + substringLen - end)));
        return new Rope(substring3);
    }

    public Rope substring(int start, int end) {
        return subSequence(start, end);
    }

    public Rope concat(Rope other) {
        return new Rope(data.concat(other.data));
    }

    public Rope prepend(String str) {
        if (!data.isEmpty() && str.length()+data.first().length() <= COPY_THRESHOLD) {
            return new Rope(data.removeFirst().addFirst(str + data.first()));
        }
        return new Rope(data.addFirst(str));
    }

    public Rope append(String str) {
        if (!data.isEmpty() && str.length()+data.last().length() <= COPY_THRESHOLD) {
            return new Rope(data.removeLast().addLast(data.last()+str));
        }
        return new Rope(data.addLast(str));
    }

    public Reader newReader() {
        try {
            return CharStreams.join(Iterables.transform(data, new Function<String, InputSupplier<StringReader>>() {
                @Override
                public InputSupplier<StringReader> apply(String input) {
                    return CharStreams.newReaderSupplier(input);
                }
            })).getInput();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void writeTo(Writer out) throws IOException {
        for (String str : data) {
            out.write(str);
        }
    }
}
