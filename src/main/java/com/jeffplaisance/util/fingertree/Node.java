package com.jeffplaisance.util.fingertree;

import java.util.Iterator;

public interface Node<V,T> extends IterableReverseIterable<T> {
    public <Z> Z match(Matcher<T,Z> matcher);

    public V measure();

    public Digit<V,T> toDigit();

    public Iterator<T> reverseIterator();

    public static abstract class Matcher<T,Z> {

        public Z node2(T a, T b) {
            return otherwise();
        }

        public Z node3(T a, T b, T c) {
            return otherwise();
        }

        public Z otherwise() {
            throw new UnsupportedOperationException();
        }
    }
}
