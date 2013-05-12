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
import com.google.common.collect.Iterators;
import com.jeffplaisance.util.Pair;

import java.util.Iterator;

public final class Single<V,T> extends FingerTree<V,T> {

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
    public FingerTree<V, T> addFirst(T t) {
        return new Deep<V, T>(new One<V, T>(t, measured), new Empty<V, Node<V, T>>(measured.nodeMeasured()), new One<V, T>(a, measured), measured);
    }

    @Override
    public FingerTree<V, T> addLast(T t) {
        return new Deep<V, T>(new One<V, T>(a, measured), new Empty<V, Node<V, T>>(measured.nodeMeasured()), new One<V, T>(t, measured), measured);
    }

    @Override
    public FingerTree<V, T> concat(FingerTree<V, T> other) {
        return other.addFirst(a);
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
    public FingerTree<V, T> takeUntil(Predicate<V> predicate) {
        return splitLeft(predicate, measured.zero(), false);
    }

    @Override
    public FingerTree<V, T> dropUntil(Predicate<V> predicate) {
        return splitRight(predicate, measured.zero(), true);
    }

    @Override
    public FingerTree<V, T> takeUntil(Predicate<V> predicate, boolean inclusive) {
        return splitLeft(predicate, measured.zero(), inclusive);
    }

    @Override
    public FingerTree<V, T> dropUntil(Predicate<V> predicate, boolean inclusive) {
        return splitRight(predicate, measured.zero(), inclusive);
    }

    @Override
    protected FingerTree<V, T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive) {
        if (inclusive) return this;
        return predicate.apply(measured.sum(initial, measure)) ? new Empty<V, T>(measured) : this;
    }

    @Override
    protected FingerTree<V, T> splitRight(Predicate<V> predicate, V initial, boolean inclusive) {
        if (inclusive) return this;
        return predicate.apply(measured.sum(initial, measure)) ? this : new Empty<V, T>(measured);
    }

    @Override
    public Pair<FingerTree<V, T>, FingerTree<V, T>> split(Predicate<V> predicate) {
        if (predicate.apply(measure)) {
            return Pair.<FingerTree<V, T>, FingerTree<V, T>>of(new Empty<V, T>(measured), this);
        }
        return Pair.<FingerTree<V, T>, FingerTree<V, T>>of(this, new Empty<V, T>(measured));
    }

    @Override
    public Split<V, T> splitTree(Predicate<V> predicate, V initial) {
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
