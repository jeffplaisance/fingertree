package com.jeffplaisance.util.fingertree;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public class Two<V, T> implements Digit<V, T> {

    private final T a;
    private final T b;
    private final Measured<V,T> measured;

    public Two(T a, T b, Measured<V, T> measured) {
        this.a = a;
        this.b = b;
        this.measured = measured;
    }

    @Override
    public <Z> Z match(Matcher<T, Z> matcher) {
        return matcher.two(a, b);
    }

    @Override
    public V measure() {
        return measured.sum(measured.measure(a), measured.measure(b));
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
