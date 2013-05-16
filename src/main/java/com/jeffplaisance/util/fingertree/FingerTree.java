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
import com.jeffplaisance.util.Pair;

import java.util.Iterator;

/**
 * base type for all finger tree instances
 *
 * finger trees are immutable, all state modifying operations return a new fingertree
 *
 * @param <V> measure type
 * @param <T> element type
 */
public abstract class FingerTree<V,T> implements Iterable<T> {

    /**
     * package private constructor, should not be subclassed except for Empty, Single, Deep
     */
    FingerTree() {}

    /**
     * Pattern Matching on FingerTree type
     * FingerTree subclasses are: Empty, Single, Deep
     * @param matcher match functions for each subclass
     * @param <Z> return type
     * @return result of applying correct function to this tree
     */
    public abstract <Z> Z match(Matcher<V,T,Z> matcher);

    /**
     * measure the tree
     * @return measure of the tree
     */
    public abstract V measure();

    /**
     * add t to tree at beginning
     * @param t element to add
     * @return new tree with t added
     */
    public abstract FingerTree<V,T> addFirst(T t);

    /**
     * add t to tree at end
     * @param t element to add
     * @return new tree with t added
     */
    public abstract FingerTree<V,T> addLast(T t);

    /**
     * concatenate 2 finger trees
     * @param other tree to append to this one
     * @return concatenation of this and other
     */
    public abstract FingerTree<V,T> concat(FingerTree<V,T> other);

    /**
     * @return true if this tree is empty
     */
    public abstract boolean isEmpty();

    /**
     * @return first element of this tree
     */
    public abstract T first();

    /**
     * @return last element of this tree
     */
    public abstract T last();

    /**
     * remove first element
     * @return tree with first element removed
     */
    public abstract FingerTree<V,T> removeFirst();

    /**
     * remove last element
     * @return tree with last element removed
     */
    public abstract FingerTree<V,T> removeLast();

    /**
     * calculates the longest prefix of the tree for which predicate.apply(tree.measure()) is true
     * @param predicate predicate on measure
     * @return prefix
     */
    public abstract FingerTree<V,T> takeUntil(Predicate<V> predicate);

    /**
     * like takeUntil but returns remaining elements not in the longest prefix for which predicate.apply(tree.measure()) is true
     * @param predicate predicate on measure
     * @return suffix
     */
    public abstract FingerTree<V,T> dropUntil(Predicate<V> predicate);

    /**
     * calculates the longest prefix of the tree for which predicate.apply(tree.measure()) is true
     * @param predicate predicate on measure
     * @param inclusive if true include the first element for which predicate is false in prefix
     * @return prefix
     */
    public abstract FingerTree<V, T> takeUntil(Predicate<V> predicate, boolean inclusive);

    /**
     * like takeUntil but returns remaining elements not in the longest prefix for which predicate.apply(tree.measure()) is true
     * @param predicate predicate on measure
     * @param inclusive if true include the last element for which predicate was true in suffix
     * @return suffix
     */
    public abstract FingerTree<V, T> dropUntil(Predicate<V> predicate, boolean inclusive);

    protected abstract FingerTree<V,T> splitLeft(Predicate<V> predicate, V initial, boolean inclusive);

    protected abstract FingerTree<V,T> splitRight(Predicate<V> predicate, V initial, boolean inclusive);

    /**
     * splits tree into two halves, first half is longest prefix for which predicate.apply(tree.measure()) is true
     * second half is remainder
     * @param predicate predicate on measure
     * @return pair containing prefix and suffix
     */
    public abstract Pair<FingerTree<V,T>, FingerTree<V,T>> split(Predicate<V> predicate);

    protected abstract Split<V,T> splitTree(Predicate<V> predicate, V initial);

    public abstract Iterator<T> iterator();

    public static abstract class Matcher<V,T,Z> {

        public Z empty() {
            return otherwise();
        }

        public Z single(T t) {
            return otherwise();
        }

        public Z deep(Digit<V,T> prefix, FingerTree<V,Node<V,T>> middle, Digit<V,T> suffix) {
            return otherwise();
        }

        public Z otherwise() {
            throw new UnsupportedOperationException();
        }
    }
}
