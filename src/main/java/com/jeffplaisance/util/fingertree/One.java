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
