package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.jeffplaisance.util.Pair;

import java.util.Iterator;

public final class Empty<V,T> extends FingerTree<V,T> {

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
    public FingerTree<V, T> takeUntil(Predicate<V> predicate) {
        return this;
    }

    @Override
    public FingerTree<V, T> dropUntil(Predicate<V> predicate) {
        return this;
    }

    @Override
    public FingerTree<V, T> takeUntil(Predicate<V> predicate, boolean inclusive) {
        return this;
    }

    @Override
    public FingerTree<V, T> dropUntil(Predicate<V> predicate, boolean inclusive) {
        return this;
    }

    @Override
    protected FingerTree<V, T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive) {
        return this;
    }

    @Override
    protected FingerTree<V, T> splitRight(Predicate<V> predicate, V initial, boolean inclusive) {
        return this;
    }

    @Override
    public Pair<FingerTree<V, T>, FingerTree<V, T>> split(Predicate<V> predicate) {
        final Empty<V, T> empty = new Empty<>(measured);
        return Pair.<FingerTree<V, T>, FingerTree<V, T>>of(empty, empty);
    }

    @Override
    protected Split<V, T> splitTree(Predicate<V> predicate, V initial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.emptyIterator();
    }
}
