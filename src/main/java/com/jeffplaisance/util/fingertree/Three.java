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
