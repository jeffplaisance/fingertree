package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;
import com.jeffplaisance.util.Pair;

import java.util.Iterator;

public abstract class FingerTree<V,T> implements Iterable<T> {

    FingerTree() {}

    public abstract <Z> Z match(Matcher<V,T,Z> matcher);

    public abstract V measure();

    public abstract FingerTree<V,T> addFirst(T t);

    public abstract FingerTree<V,T> addLast(T t);

    public abstract FingerTree<V,T> concat(FingerTree<V,T> other);

    public abstract boolean isEmpty();

    public abstract T first();

    public abstract T last();

    public abstract FingerTree<V,T> removeFirst();

    public abstract FingerTree<V,T> removeLast();

    public abstract FingerTree<V,T> takeUntil(Predicate<V> predicate);

    public abstract FingerTree<V,T> dropUntil(Predicate<V> predicate);

    public abstract FingerTree<V, T> takeUntil(Predicate<V> predicate, boolean inclusive);

    public abstract FingerTree<V, T> dropUntil(Predicate<V> predicate, boolean inclusive);

    protected abstract FingerTree<V,T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive);

    protected abstract FingerTree<V,T> splitRight(Predicate<V> predicate, V initial, boolean inclusive);

    public abstract Pair<FingerTree<V,T>, FingerTree<V,T>> split(Predicate<V> predicate);

    protected abstract Split<V,T> splitTree(Predicate<V> predicate, V initial);

    public abstract Iterator<T> iterator();

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
