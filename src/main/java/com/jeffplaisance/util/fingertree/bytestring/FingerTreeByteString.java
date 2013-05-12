package com.jeffplaisance.util.fingertree.bytestring;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import com.jeffplaisance.util.Pair;
import com.jeffplaisance.util.fingertree.Empty;
import com.jeffplaisance.util.fingertree.FingerTree;
import com.jeffplaisance.util.fingertree.Measured;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FingerTreeByteString extends ByteString {

    public static final Measured<Integer,ByteStringLiteral> BYTE_STRING_LENGTH_MEASURE = new Measured<Integer, ByteStringLiteral>() {
        @Override
        public Integer sum(Integer a, Integer b) {
            return a + b;
        }

        @Override
        public Integer measure(ByteStringLiteral a) {
            return a.length();
        }

        @Override
        public Integer zero() {
            return 0;
        }
    };

    static FingerTreeByteString emptyFT() {
        return new FingerTreeByteString(new Empty<Integer, ByteStringLiteral>(BYTE_STRING_LENGTH_MEASURE));
    }

    final FingerTree<Integer, ByteStringLiteral> bytes;

    public FingerTreeByteString(FingerTree<Integer, ByteStringLiteral> bytes) {
        this.bytes = bytes;
    }

    public ByteString prepend(ByteStringLiteral literal) {
        if (!bytes.isEmpty() && literal.length()+bytes.first().length() <= ByteStringLiteral.COPY_THRESHOLD) {
            return new FingerTreeByteString(bytes.removeFirst().addFirst(ByteStringLiteral.copy(literal, bytes.first())));
        } else {
            return new FingerTreeByteString(bytes.addFirst(literal));
        }
    }

    public ByteString append(ByteStringLiteral literal) {
        if (!bytes.isEmpty() && bytes.last().length()+literal.length() <= ByteStringLiteral.COPY_THRESHOLD) {
            return new FingerTreeByteString(bytes.removeLast().addLast(ByteStringLiteral.copy(bytes.last(), literal)));
        } else {
            return new FingerTreeByteString(bytes.addLast(literal));
        }
    }

    @Override
    public ByteString concat(ByteString other) {
        if (!other.isLiteral()) {
            return new FingerTreeByteString(bytes.concat(((FingerTreeByteString) other).bytes));
        } else {
            return append((ByteStringLiteral)other);
        }
    }

    @Override
    public ByteString substring(final int start, final int end) {
        final Pair<FingerTree<Integer, ByteStringLiteral>, FingerTree<Integer, ByteStringLiteral>> split1 = bytes.split(
                new Predicate<Integer>() {
                    @Override
                    public boolean apply(Integer integer) {
                        return integer > start;
                    }
                }
        );
        final int headLength = split1.a().measure();
        final FingerTree<Integer, ByteStringLiteral> substring = split1.b().takeUntil(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer >= end - headLength;
            }
        }, true);
        final int substringLen = substring.measure();
        final FingerTree<Integer, ByteStringLiteral> substring2 = substring.removeFirst().addFirst(substring.first().substring(start - headLength, substring.first().length()));
        final FingerTree<Integer, ByteStringLiteral> substring3 = substring2.removeLast().addLast(substring2.last().substring(0, substring2.last().length() - (headLength + substringLen - end)));
        return new FingerTreeByteString(substring3);
    }

    @Override
    public ByteString substring(int start) {
        return substring(start, length());
    }

    @Override
    public InputStream newInput() {
        try {
            return ByteStreams.join(Iterables.transform(
                    bytes,
                    new Function<ByteStringLiteral, InputSupplier<InputStream>>() {
                        @Override
                        public InputSupplier<InputStream> apply(final ByteStringLiteral literal) {
                            return new InputSupplier<InputStream>() {
                                @Override
                                public InputStream getInput() throws IOException {
                                    return literal.newInput();
                                }
                            };
                        }
                    }
            )).getInput();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        for (ByteStringLiteral literal : bytes) {
            literal.writeTo(out);
        }
    }

    @Override
    public byte getByte(final int index) {
        final Pair<FingerTree<Integer, ByteStringLiteral>, FingerTree<Integer, ByteStringLiteral>> split = bytes.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > index;
            }
        });
        return split.b().first().getByte(index-split.a().measure());
    }

    @Override
    public int length() {
        return bytes.measure();
    }

    @Override
    boolean isLiteral() {
        return false;
    }
}
