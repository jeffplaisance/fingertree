package com.jeffplaisance.util.fingertree.list;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.jeffplaisance.util.Pair;
import com.jeffplaisance.util.fingertree.Empty;
import com.jeffplaisance.util.fingertree.FingerTree;
import com.jeffplaisance.util.fingertree.FingerTrees;
import com.jeffplaisance.util.fingertree.Measured;
import com.jeffplaisance.util.fingertree.Single;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class IndexedList<T> implements Iterable<T> {

    private static final Measured<Integer, Object> COUNT_MEASURED = new Measured<Integer, Object>() {
        @Override
        public Integer sum(Integer a, Integer b) {
            return a+b;
        }

        @Override
        public Integer measure(Object a) {
            return 1;
        }

        @Override
        public Integer zero() {
            return 0;
        }
    };

    private static <T> Measured<Integer, T> getCountMeasured() {
        return (Measured<Integer, T>) COUNT_MEASURED;
    }

    private final FingerTree<Integer, T> data;

    public static <T> IndexedList<T> empty() {
        return new IndexedList<T>(new Empty<Integer, T>(IndexedList.<T>getCountMeasured()));
    }

    public static <T> IndexedList<T> singleton(T t) {
        return new IndexedList<T>(new Single<Integer, T>(t, IndexedList.<T>getCountMeasured()));
    }

    public static <T> IndexedList<T> newList(Iterable<T> iterable) {
        return new IndexedList<T>(FingerTrees.toTree(iterable, IndexedList.<T>getCountMeasured()));
    }

    public static <T> IndexedList<T> newList(T... ts) {
        return new IndexedList<T>(FingerTrees.toTree(Arrays.asList(ts), IndexedList.<T>getCountMeasured()));
    }

    private IndexedList(FingerTree<Integer, T> data) {
        this.data = data;
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    public int size() {
        return data.measure();
    }

    public T get(final int index) {
        final Pair<FingerTree<Integer, T>, FingerTree<Integer, T>> split = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > index;
            }
        });
        return split.b().first();
    }

    public IndexedList<T> set(final int index, T t) {
        final Pair<FingerTree<Integer, T>, FingerTree<Integer, T>> split = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > index;
            }
        });
        return new IndexedList<T>(split.a().addLast(t).concat(split.b().removeFirst()));
    }

    public IndexedList<T> addFirst(T t) {
        return new IndexedList<T>(data.addFirst(t));
    }

    public IndexedList<T> addLast(T t) {
        return new IndexedList<T>(data.addLast(t));
    }

    public IndexedList<T> add(T t) {
        return new IndexedList<T>(data.addLast(t));
    }

    public IndexedList<T> removeFirst() {
        return new IndexedList<T>(data.removeFirst());
    }

    public IndexedList<T> removeLast() {
        return new IndexedList<T>(data.removeLast());
    }

    public T first() {
        return data.first();
    }

    public T last() {
        return data.last();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public IndexedList<T> concat(IndexedList<T> ts) {
        return new IndexedList<T>(data.concat(ts.data));
    }

    public IndexedList<T> subList(final int start, final int end) {
        final Pair<FingerTree<Integer, T>, FingerTree<Integer, T>> split1 = data.split(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > start;
            }
        });
        final FingerTree<Integer, T> subList = split1.b().takeUntil(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer >= end - start;
            }
        }, true);
        return new IndexedList<T>(subList);
    }

    public T[] toArray(T[] a) {
        if (a.length < size()) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length);
        }
        final Iterator<T> iterator = data.iterator();
        for (int i = 0; ;i++) {
            if (!iterator.hasNext()) break;
            a[i] = iterator.next();
        }
        return a;
    }

    @Override
    public int hashCode() {
        final Hasher hasher = Hashing.murmur3_32().newHasher();
        for (T t : this) {
            hasher.putInt(t.hashCode());
        }
        return hasher.hash().asInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IndexedList)) {
            return false;
        }
        final IndexedList other = (IndexedList) obj;
        return size() == other.size() && Iterables.elementsEqual(this, other);
    }

    public static void main(String[] args) {
        final List<Integer> list = Lists.newArrayList();
        IndexedList<Integer> iList = empty();
        final Random r = new Random(0);
        for (int i = 0; i < 100000; i++) {
            final int rand = r.nextInt();
            list.add(rand);
            iList = iList.add(rand);
        }
        long sum1 = 0;
        long sum2 = 0;
        for (int i = 0; i < 100000; i++) {
            final int index = r.nextInt(100000);
            sum1 += list.get(index);
            sum2 += iList.get(index);
        }
        System.out.println(sum1);
        System.out.println(sum2);
        final int start = r.nextInt(list.size());
        final int length = r.nextInt(list.size()-start);
        System.out.println(Iterables.elementsEqual(list.subList(start, start+length), iList.subList(start, start+length)));
    }
}
