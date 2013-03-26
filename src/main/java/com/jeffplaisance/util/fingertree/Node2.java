package com.jeffplaisance.util.fingertree;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Node2<V,T> implements Node<V,T> {

    private final T a;
    private final T b;
    private final Measured<V, T> measured;
    private final V measure;

    public Node2(T a, T b, Measured<V,T> measured) {
        this.a = a;
        this.b = b;
        this.measured = measured;
        this.measure = measured.sum(measured.measure(a), measured.measure(b));
    }

    @Override
    public <Z> Z match(Matcher<T, Z> matcher) {
        return matcher.node2(a, b);
    }

    @Override
    public V measure() {
        return measure;
    }

    @Override
    public Digit<V, T> toDigit() {
        return new Two<V, T>(a, b, measured);
    }

    @Override
    public Iterator<T> reverseIterator() {
        return Iterators.forArray(b, a);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.forArray(a, b);
    }
}
