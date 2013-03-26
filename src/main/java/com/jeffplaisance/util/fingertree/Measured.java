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
