package com.jeffplaisance.util.fingertree;

import java.util.Iterator;

public class FingerTrees {
    public static <V,T> FingerTree<V,T> toTree(Iterable<T> iterable, Measured<V,T> measured) {
        FingerTree<V,T> ret = new Empty<V, T>(measured);
        for (T t : iterable) {
            ret = ret.append(t);
        }
        return ret;
    }

    public static <V,T> FingerTree<V,T> prepend(ReverseIterable<T> rit, FingerTree<V,T> tree) {
        final Iterator<T> iterator = rit.reverseIterator();
        FingerTree<V, T> ret = tree;
        while (iterator.hasNext()) {
            ret = tree.prepend(iterator.next());
        }
        return ret;
    }

    public static <V,T> FingerTree<V,T> append(FingerTree<V,T> tree, Iterable<T> it) {
        FingerTree<V, T> ret = tree;
        for (T t : it) {
            ret = ret.append(t);
        }
        return ret;
    }
}
