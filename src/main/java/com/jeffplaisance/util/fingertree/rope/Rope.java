package com.jeffplaisance.util.fingertree.rope;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.jeffplaisance.util.Pair;
import com.jeffplaisance.util.fingertree.Empty;
import com.jeffplaisance.util.fingertree.FingerTree;
import com.jeffplaisance.util.fingertree.Measured;
import com.jeffplaisance.util.fingertree.Single;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

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
        final Pair<FingerTree<Integer, String>, FingerTree<Integer, String>> split = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > index;
            }
        });
        return split.b().first().charAt(index - split.a().measure());
    }

    @Override
    public Rope subSequence(final int start, final int end) {
        final Pair<FingerTree<Integer, String>, FingerTree<Integer, String>> split1 = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > start;
            }
        });
        final int headLength = split1.a().measure();
        final FingerTree<Integer, String> substring = split1.b().takeUntil(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer >= end - headLength;
            }
        }, true);
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

    public void writeTo(Appendable out) throws IOException {
        for (String str : data) {
            out.append(str);
        }
    }

    private static int read(Reader in, char[] chars, int off, int len)
            throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int total = 0;
        while (total < len) {
            final int result = in.read(chars, off + total, len - total);
            if (result == -1) {
                break;
            }
            total += result;
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Rope)) {
            return false;
        }
        final Rope other = (Rope) o;
        if (length() != other.length()) {
            return false;
        }
        final int bufLen = Math.max(length(), 1024);
        final char[] buf1 = new char[bufLen];
        final char[] buf2 = new char[bufLen];

        try {
            final Closer closer = Closer.create();
            try {
                final Reader in1 = closer.register(newReader());
                final Reader in2 = closer.register(other.newReader());
                while (true) {
                    final int read1 = read(in1, buf1, 0, bufLen);
                    final int read2 = read(in2, buf2, 0, bufLen);
                    if (read1 != read2 || !Arrays.equals(buf1, buf2)) {
                        return false;
                    } else if (read1 != bufLen) {
                        return true;
                    }
                }
            } catch (Throwable e) {
                throw closer.rethrow(e);
            } finally {
                closer.close();
            }
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public int hashCode() {
        final Hasher hasher = Hashing.murmur3_32().newHasher();
        for (String str : data) {
            hasher.putString(str);
        }
        return hasher.hash().asInt();
    }
}
