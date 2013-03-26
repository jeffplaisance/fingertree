package com.jeffplaisance.util.fingertree;

public class Split<V,T> {

    private final FingerTree<V,T> head;
    private final T element;
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
