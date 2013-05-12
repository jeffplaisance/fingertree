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

import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IterableReverseIterables {

    public static <T> IterableReverseIterable<T> concat(final IterableReverseIterable<T> a, final IterableReverseIterable<T> b) {
        return new IterableReverseIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.concat(a.iterator(), b.iterator());
            }

            @Override
            public Iterator<T> reverseIterator() {
                return Iterators.concat(b.reverseIterator(), a.reverseIterator());
            }
        };
    }

    public static <T> IterableReverseIterable<T> empty() {
        return new IterableReverseIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.emptyIterator();
            }

            @Override
            public Iterator<T> reverseIterator() {
                return Iterators.emptyIterator();
            }
        };
    }

    public static <T> IterableReverseIterable<T> fromList(final List<T> list) {
        return new IterableReverseIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.unmodifiableIterator(list.iterator());
            }

            @Override
            public Iterator<T> reverseIterator() {
                final ListIterator<T> iterator = list.listIterator(list.size());
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasPrevious();
                    }

                    @Override
                    public T next() {
                        return iterator.previous();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
