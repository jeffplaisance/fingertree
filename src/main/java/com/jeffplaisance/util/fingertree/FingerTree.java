package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;

import java.util.Iterator;

public interface FingerTree<V,T> extends Iterable<T> {

    public <Z> Z match(Matcher<V,T,Z> matcher);

    public V measure();

    public FingerTree<V,T> addFirst(T t);

    public FingerTree<V,T> addLast(T t);

    public FingerTree<V,T> concat(FingerTree<V,T> other);

    public boolean isEmpty();

    public T first();

    public T last();

    public FingerTree<V,T> removeFirst();

    public FingerTree<V,T> removeLast();

    public FingerTree<V,T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive);

    public FingerTree<V,T> splitRight(Predicate<V> predicate, V initial, boolean inclusive);

    public Split<V,T> split(Predicate<V> predicate, V initial);

    public Iterator<T> iterator();

    public static abstract class Matcher<V,T,Z> {

        public Z empty() {
            return otherwise();
        }

        public Z single(T t) {
            return otherwise();
        }

        public Z deep(Digit<V,T> prefix, FingerTree<V,Node<V,T>> middle, Digit<V,T> suffix) {
            return otherwise();
        }

        public Z otherwise() {
            throw new UnsupportedOperationException();
        }
    }
}
