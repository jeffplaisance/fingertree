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

public abstract class Measured<V,T> {

    private volatile Measured<V, Node<V,T>> nodeMeasured = null;

    public abstract V sum(V a, V b);

    public abstract V measure(T a);

    public abstract V zero();

    Measured<V, Node<V,T>> nodeMeasured() {
        if (nodeMeasured == null) {
            synchronized (this) {
                if (nodeMeasured == null) {
                    nodeMeasured = new NodeMeasured<V, T>(this);
                }
            }
        }
        return nodeMeasured;
    }

    public static final class NodeMeasured<V,T> extends Measured<V, Node<V,T>> {

        private final Measured<V, T> measured;

        public NodeMeasured(Measured<V, T> measured) {
            this.measured = measured;
        }

        @Override
        public V sum(V a, V b) {
            return measured.sum(a, b);
        }

        @Override
        public V measure(Node<V, T> a) {
            return a.measure();
        }

        @Override
        public V zero() {
            return measured.zero();
        }
    }
}
