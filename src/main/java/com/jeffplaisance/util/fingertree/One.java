package com.jeffplaisance.util.fingertree;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public class One<V, T> implements Digit<V, T> {

    private final T a;
    private final Measured<V,T> measured;

    public One(T a, Measured<V, T> measured) {
        this.a = a;
        this.measured = measured;
    }

    @Override
    public <Z> Z match(Matcher<T, Z> matcher) {
        return matcher.one(a);
    }

    @Override
    public V measure() {
        return measured.measure(a);
    }

    @Override
    public Iterator<T> reverseIterator() {
        return iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.singletonIterator(a);
    }
}
