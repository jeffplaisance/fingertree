package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import java.util.Iterator;

public class FingerTreeThunk<V,T> implements FingerTree<V,T> {

    private Supplier<FingerTree<V,T>> supplier;
    private volatile FingerTree<V,T> tree;

    public FingerTreeThunk(Supplier<FingerTree<V, T>> supplier) {
        this.supplier = supplier;
    }

    private FingerTree<V,T> getTree() {
        FingerTree<V,T> localTree = tree;
        if (localTree == null) {
            synchronized (this) {
                if (tree == null) {
                    localTree = supplier.get();
                    tree = localTree;
                    supplier = null;
                } else {
                    localTree = tree;
                }
            }
        }
        return localTree;
    }

    public <Z> Z match(Matcher<V, T, Z> matcher) {
        return getTree().match(matcher);
    }

    @Override
    public V measure() {
        return getTree().measure();
    }

    public FingerTree<V,T> addFirst(T t) {
        return getTree().addFirst(t);
    }

    public FingerTree<V,T> addLast(T t) {
        return getTree().addLast(t);
    }

    public FingerTree<V,T> concat(FingerTree<V, T> other) {
        return getTree().concat(other);
    }

    @Override
    public boolean isEmpty() {
        return getTree().isEmpty();
    }

    @Override
    public T first() {
        return getTree().first();
    }

    @Override
    public T last() {
        return getTree().last();
    }

    @Override
    public FingerTree<V,T> removeFirst() {
        return getTree().removeFirst();
    }

    @Override
    public FingerTree<V,T> removeLast() {
        return getTree().removeLast();
    }

    @Override
    public FingerTree<V, T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive) {
        return getTree().splitLeft(predicate, initial, inclusive);
    }

    @Override
    public FingerTree<V, T> splitRight(Predicate<V> predicate, V initial, boolean inclusive) {
        return getTree().splitRight(predicate, initial, inclusive);
    }

    public Split<V,T> split(Predicate<V> predicate, V initial) {
        return getTree().split(predicate, initial);
    }

    @Override
    public Iterator<T> iterator() {
        return getTree().iterator();
    }
}
