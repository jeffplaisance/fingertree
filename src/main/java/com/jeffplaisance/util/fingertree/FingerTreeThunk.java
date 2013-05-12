/*
 * Copyright 2013 Jeff Plaisance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.jeffplaisance.util.Pair;

import java.util.Iterator;

public class FingerTreeThunk<V,T> extends FingerTree<V,T> {

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

    public FingerTree<V,T> takeUntil(Predicate<V> predicate) {
        return getTree().takeUntil(predicate);
    }

    public FingerTree<V,T> dropUntil(Predicate<V> predicate) {
        return getTree().dropUntil(predicate);
    }

    public FingerTree<V,T> takeUntil(Predicate<V> predicate, boolean inclusive) {
        return getTree().takeUntil(predicate, inclusive);
    }

    public FingerTree<V,T> dropUntil(Predicate<V> predicate, boolean inclusive) {
        return getTree().dropUntil(predicate, inclusive);
    }

    @Override
    public FingerTree<V, T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive) {
        return getTree().splitLeft(predicate, initial, inclusive);
    }

    @Override
    public FingerTree<V, T> splitRight(Predicate<V> predicate, V initial, boolean inclusive) {
        return getTree().splitRight(predicate, initial, inclusive);
    }

    public Pair<FingerTree<V,T>,FingerTree<V,T>> split(Predicate<V> predicate) {
        return getTree().split(predicate);
    }

    public Split<V,T> splitTree(Predicate<V> predicate, V initial) {
        return getTree().splitTree(predicate, initial);
    }

    @Override
    public Iterator<T> iterator() {
        return getTree().iterator();
    }
}
