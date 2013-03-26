package com.jeffplaisance.util.fingertree;

import java.util.Iterator;

public interface Digit<V, T> extends IterableReverseIterable<T> {
    public <Z> Z match(Matcher<T, Z> matcher);

    public V measure();

    public Iterator<T> reverseIterator();

    public static abstract class Matcher<T, Z> {
        public Z one(T a) {
            return otherwise();
        }

        public Z two(T a, T b) {
            return otherwise();
        }

        public Z three(T a, T b, T c) {
            return otherwise();
        }

        public Z four(T a, T b, T c, T d) {
            return otherwise();
        }

        public Z otherwise() {
            throw new UnsupportedOperationException();
        }
    }
}
