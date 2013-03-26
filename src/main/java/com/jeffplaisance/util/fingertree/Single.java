package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Single<V,T> implements FingerTree<V,T> {

    private final T a;
    private final Measured<V, T> measured;
    private final V measure;

    public Single(T t, Measured<V,T> measured) {
        this.a = t;
        this.measured = measured;
        this.measure = measured.measure(t);
    }

    @Override
    public <Z> Z match(Matcher<V, T, Z> matcher) {
        return matcher.single(a);
    }

    @Override
    public V measure() {
        return measure;
    }

    @Override
    public FingerTree<V, T> prepend(T t) {
        return new Deep<V, T>(new One<V, T>(t, measured), new Empty<V, Node<V, T>>(measured.nodeMeasured()), new One<V, T>(a, measured), measured);
    }

    @Override
    public FingerTree<V, T> append(T t) {
        return new Deep<V, T>(new One<V, T>(a, measured), new Empty<V, Node<V, T>>(measured.nodeMeasured()), new One<V, T>(t, measured), measured);
    }

    @Override
    public FingerTree<V, T> concat(FingerTree<V, T> other) {
        return other.prepend(a);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T first() {
        return a;
    }

    @Override
    public T last() {
        return a;
    }

    @Override
    public FingerTree<V, T> removeFirst() {
        return new Empty<V, T>(measured);
    }

    @Override
    public FingerTree<V, T> removeLast() {
        return new Empty<V, T>(measured);
    }

    @Override
    public Split<V, T> split(Predicate<V> predicate, V initial) {
        final Empty<V, T> empty = new Empty<>(measured);
        return new Split<V, T>(empty, a, empty);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.singletonIterator(a);
    }

    @Override
    public String toString() {
        return "Single{" +
                "a=" + a +
                '}';
    }
}
