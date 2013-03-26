package com.jeffplaisance.util.fingertree;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Four<V, T> implements Digit<V, T> {

    private final T a;
    private final T b;
    private final T c;
    private final T d;
    private final Measured<V,T> measured;

    public Four(T a, T b, T c, T d, Measured<V, T> measured) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.measured = measured;
    }

    @Override
    public <Z> Z match(Matcher<T, Z> matcher) {
        return matcher.four(a, b, c, d);
    }

    @Override
    public V measure() {
        return measured.sum(measured.sum(measured.sum(measured.measure(a), measured.measure(b)), measured.measure(c)), measured.measure(d));
    }

    @Override
    public Iterator<T> reverseIterator() {
        return Iterators.forArray(d, c, b, a);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.forArray(a, b, c, d);
    }
}
