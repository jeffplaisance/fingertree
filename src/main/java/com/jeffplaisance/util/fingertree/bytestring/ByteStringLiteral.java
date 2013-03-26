package com.jeffplaisance.util.fingertree.bytestring;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class ByteStringLiteral extends ByteString {

    static final int COPY_THRESHOLD = 128;

    private final byte[] bytes;
    private final int offset;
    private final int length;

    public ByteStringLiteral(byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public ByteString concat(ByteString other) {
        if (other.isLiteral()) {
            final ByteStringLiteral byteString = (ByteStringLiteral) other;
            if (length + byteString.length <= COPY_THRESHOLD) {
                return copy(this, byteString);
            } else {
                return FingerTreeByteString.empty().append(this).concat(other);
            }
        } else {
            return ((FingerTreeByteString)other).prepend(this);
        }
    }

    static ByteStringLiteral copy(ByteStringLiteral a, ByteStringLiteral b) {
        final byte[] newArray = new byte[a.length+b.length];
        System.arraycopy(a.bytes, a.offset, newArray, 0, a.length);
        System.arraycopy(b.bytes, b.offset, newArray, a.length(), b.length);
        return new ByteStringLiteral(newArray, 0, newArray.length);
    }

    @Override
    public ByteStringLiteral substring(int start, int end) {
        return new ByteStringLiteral(bytes, offset+start, end-start);
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes, offset, length);
    }

    public byte getByte(int index) {
        return bytes[offset+index];
    }

    public int length() {
        return length;
    }

    @Override
    boolean isLiteral() {
        return true;
    }
}
