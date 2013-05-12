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

public class Split<V,T> {

    private final FingerTree<V,T> head;
    private final
    T element;
    private final FingerTree<V,T> tail;

    public Split(FingerTree<V, T> head, T element, FingerTree<V, T> tail) {
        this.head = head;
        this.element = element;
        this.tail = tail;
    }

    public FingerTree<V, T> getHead() {
        return head;
    }

    public T getElement() {
        return element;
    }

    public FingerTree<V, T> getTail() {
        return tail;
    }
}
