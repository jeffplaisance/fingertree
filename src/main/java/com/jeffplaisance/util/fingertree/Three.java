package com.jeffplaisance.util.fingertree;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Three<V, T> implements Digit<V, T> {

    private final T a;
    private final T b;
    private final T c;
    private final Measured<V,T> measured;

    public Three(T a, T b, T c, Measured<V, T> measured) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.measured = measured;
    }

    @Override
    public <Z> Z match(Matcher<T, Z> matcher) {
        return matcher.three(a, b, c);
    }

    @Override
    public V measure() {
        return measured.sum(measured.sum(measured.measure(a), measured.measure(b)), measured.measure(c));
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
