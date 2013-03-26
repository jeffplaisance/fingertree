package com.jeffplaisance.util.fingertree.bytestring;

import java.io.InputStream;
import java.util.Arrays;

public abstract class ByteString {

    public static ByteString fromBytes(byte[] bytes) {
        return new ByteStringLiteral(Arrays.copyOf(bytes, bytes.length), 0, bytes.length);
    }

    public static ByteString fromBytes(byte[] bytes, int offset, int length) {
        return new ByteStringLiteral(Arrays.copyOfRange(bytes, offset, offset+length), 0, length);
    }

    ByteString() {}

    public abstract ByteString concat(ByteString other);

    public abstract ByteString substring(int start, int end);

    public abstract InputStream getInputStream();

    public abstract byte getByte(int index);

    public abstract int length();

    abstract boolean isLiteral();
}
