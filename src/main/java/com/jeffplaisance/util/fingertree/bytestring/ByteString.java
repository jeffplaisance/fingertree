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

import com.google.common.base.Throwables;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

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

    public static ByteString readFrom(InputStream in) throws IOException {
        final byte[] bytes = ByteStreams.toByteArray(in);
        return new ByteStringLiteral(bytes, 0, bytes.length);
    }

    public static ByteString empty() {
        return FingerTreeByteString.emptyFT();
    }

    ByteString() {}

    public abstract ByteString concat(ByteString other);

    public abstract ByteString substring(int start, int end);

    public abstract ByteString substring(int start);

    public abstract InputStream newInput();

    public abstract void writeTo(OutputStream out) throws IOException;

    public abstract byte getByte(int index);

    public abstract int length();

    abstract boolean isLiteral();

    @Override
    public int hashCode() {
        try {
            return ByteStreams.hash(new InputSupplier<InputStream>() {
                @Override
                public InputStream getInput() throws IOException {
                    return newInput();
                }
            }, Hashing.murmur3_32()).asInt();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ByteString)) {
            return false;
        }
        try {
            return ByteStreams.equal(
                    new InputSupplier<InputStream>() {
                        @Override
                        public InputStream getInput() throws IOException {
                            return newInput();
                        }
                    },
                    new InputSupplier<InputStream>() {
                        @Override
                        public InputStream getInput() throws IOException {
                            return ((ByteString) obj).newInput();
                        }
                    }
            );
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
