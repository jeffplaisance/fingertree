package com.jeffplaisance.util.fingertree;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Node3<V,T> implements Node<V,T> {

    private final T a;
    private final T b;
    private final T c;
    private final Measured<V, T> measured;
    private final V measure;

    public Node3(T a, T b, T c, Measured<V,T> measured) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.measured = measured;
        this.measure = measured.sum(measured.sum(measured.measure(a), measured.measure(b)), measured.measure(c));
    }

    @Override
    public <Z> Z match(Matcher<T, Z> matcher) {
        return matcher.node3(a, b, c);
    }

    @Override
    public V measure() {
        return measure;
    }

    @Override
    public Digit<V, T> toDigit() {
        return new Three<V, T>(a, b, c, measured);
    }

    @Override
    public Iterator<T> reverseIterator() {
        return Iterators.forArray(c, b, a);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.forArray(a, b, c);
    }
}
