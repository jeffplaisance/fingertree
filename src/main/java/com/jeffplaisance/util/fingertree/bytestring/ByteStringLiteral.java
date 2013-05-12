/*
 * Copyright 2013 Jeff Plaisance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeffplaisance.util.fingertree.bytestring;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                return FingerTreeByteString.emptyFT().append(this).concat(other);
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
        if (start < 0 || end > length) {
            throw new IllegalArgumentException();
        }
        return new ByteStringLiteral(bytes, offset+start, end-start);
    }

    @Override
    public ByteString substring(int start) {
        return substring(start, length);
    }

    @Override
    public InputStream newInput() {
        return new ByteArrayInputStream(bytes, offset, length);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(bytes, offset, length);
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
