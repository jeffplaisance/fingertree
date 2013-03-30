package com.jeffplaisance.util.fingertree.bytestring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public abstract class ByteString {

    public static ByteString fromBytes(byte[] bytes) {
        return new ByteStringLiteral(Arrays.copyOf(bytes, bytes.length), 0, bytes.length);
    }

    public static ByteString fromBytes(byte[] bytes, int offset, int length) {
        return new ByteStringLiteral(Arrays.copyOfRange(bytes, offset, offset+length), 0, length);
    }

    public static ByteString empty() {
        return FingerTreeByteString.emptyFT();
    }

    ByteString() {}

    public abstract ByteString concat(ByteString other);

    public abstract ByteString substring(int start, int end);

    public abstract InputStream newInputStream();

    public abstract void writeTo(OutputStream out) throws IOException;

    public abstract byte getByte(int index);

    public abstract int length();

    abstract boolean isLiteral();
}
