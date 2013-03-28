package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Empty<V,T> implements FingerTree<V,T> {

    private final Measured<V, T> measured;
    private final V measure;

    public Empty(Measured<V,T> measured) {
        this.measured = measured;
        this.measure = measured.zero();
    }

    @Override
    public <Z> Z match(Matcher<V, T, Z> matcher) {
        return matcher.empty();
    }

    @Override
    public V measure() {
        return measure;
    }

    @Override
    public FingerTree<V, T> addFirst(T t) {
        return new Single<V, T>(t, measured);
    }

    @Override
    public FingerTree<V, T> addLast(T t) {
        return new Single<V, T>(t, measured);
    }

    @Override
    public FingerTree<V, T> concat(FingerTree<V, T> other) {
        return other;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T first() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T last() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FingerTree<V, T> removeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FingerTree<V, T> removeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FingerTree<V, T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive) {
        return this;
    }

    @Override
    public FingerTree<V, T> splitRight(Predicate<V> predicate, V initial, boolean inclusive) {
        return this;
    }

    @Override
    public Split<V, T> split(Predicate<V> predicate, V initial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.emptyIterator();
    }
}
