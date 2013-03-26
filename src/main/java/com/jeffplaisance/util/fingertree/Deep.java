package com.jeffplaisance.util.fingertree;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

public class Deep<V,T> implements FingerTree<V,T> {

    private final Digit<V,T> prefix;
    private final FingerTree<V,Node<V,T>> middle;
    private final Digit<V,T> suffix;
    private final Measured<V, T> measured;
    private final V measure;

    public Deep(Digit<V, T> prefix, FingerTree<V, Node<V,T>> middle, Digit<V, T> suffix, Measured<V,T> measured) {
        this.prefix = prefix;
        this.middle = middle;
        this.suffix = suffix;
        this.measured = measured;
        this.measure = measured.sum(measured.sum(prefix.measure(), middle.measure()), suffix.measure());
    }

    @Override
    public <Z> Z match(Matcher<V, T, Z> matcher) {
        return matcher.deep(prefix, middle, suffix);
    }

    @Override
    public V measure() {
        return measure;
    }

    @Override
    public FingerTree<V, T> prepend(final T t) {
        return prefix.match(new Digit.Matcher<T, FingerTree<V, T>>() {
            @Override
            public FingerTree<V, T> one(T a) {
                return new Deep<V, T>(new Two<V, T>(t, a, measured), middle, suffix, measured);
            }

            @Override
            public FingerTree<V, T> two(T a, T b) {
                return new Deep<V, T>(new Three<V, T>(t, a, b, measured), middle, suffix, measured);
            }

            @Override
            public FingerTree<V, T> three(T a, T b, T c) {
                return new Deep<V, T>(new Four<V, T>(t, a, b, c, measured), middle, suffix, measured);
            }

            @Override
            public FingerTree<V, T> four(T a, T b, T c, T d) {
                return new Deep<V, T>(new Two<V, T>(t, a, measured), middle.prepend(new Node3<V, T>(b, c, d, measured)), suffix, measured);
            }
        });
    }

    @Override
    public FingerTree<V, T> append(final T t) {
        return suffix.match(new Digit.Matcher<T, FingerTree<V, T>>() {
            @Override
            public FingerTree<V, T> one(T a) {
                return new Deep<V, T>(prefix, middle, new Two<V, T>(a, t, measured), measured);
            }

            @Override
            public FingerTree<V, T> two(T b, T a) {
                return new Deep<V, T>(prefix, middle, new Three<V, T>(b, a, t, measured), measured);
            }

            @Override
            public FingerTree<V, T> three(T c, T b, T a) {
                return new Deep<V, T>(prefix, middle, new Four<V, T>(c, b, a, t, measured), measured);
            }

            @Override
            public FingerTree<V, T> four(T d, T c, T b, T a) {
                return new Deep<V, T>(prefix, middle.append(new Node3<V, T>(d, c, b, measured)), new Two<V, T>(a, t, measured), measured);
            }
        });
    }

    @Override
    public FingerTree<V, T> concat(FingerTree<V, T> other) {
        return app3(this, IterableReverseIterables.<T>empty(), other, measured);
    }

    private static <V,T> FingerTree<V,T> app3(final FingerTree<V,T> left, final IterableReverseIterable<T> itrit, final FingerTree<V,T> right, final Measured<V,T> measured) {
        return left.match(new Matcher<V, T, FingerTree<V, T>>() {
            @Override
            public FingerTree<V, T> empty() {
                return FingerTrees.prepend(itrit, right);
            }

            @Override
            public FingerTree<V, T> single(T t) {
                return FingerTrees.prepend(itrit, right).prepend(t);
            }

            @Override
            public FingerTree<V, T> deep(final Digit<V, T> prefixA, final FingerTree<V, Node<V, T>> middleA, final Digit<V, T> suffixA) {
                return right.match(new Matcher<V, T, FingerTree<V, T>>() {
                    @Override
                    public FingerTree<V, T> empty() {
                        return FingerTrees.append(left, itrit);
                    }

                    @Override
                    public FingerTree<V, T> single(T t) {
                        return FingerTrees.append(left, itrit).append(t);
                    }

                    @Override
                    public FingerTree<V, T> deep(Digit<V, T> prefixB, FingerTree<V, Node<V, T>> middleB, Digit<V, T> suffixB) {
                        final IterableReverseIterable<Node<V, T>> nodes = nodes(
                                IterableReverseIterables.concat(
                                        suffixA,
                                        IterableReverseIterables.concat(itrit,prefixB)
                                ),
                                measured
                        );
                        final FingerTree<V, Node<V, T>> middle = app3(middleA, nodes, middleB, measured.nodeMeasured());
                        return new Deep<V, T>(prefixA, middle, suffixB, measured);
                    }
                });
            }
        });
    }

    private static <V,T> IterableReverseIterable<Node<V,T>> nodes(IterableReverseIterable<T> itrit, Measured<V,T> measured) {
        final List<Node<V,T>> ret = Lists.newArrayList();
        final ArrayDeque<T> ts = new ArrayDeque<T>();
        for (T t : itrit) {
            ts.add(t);
        }
        while (ts.size() > 4) {
            ret.add(new Node3<V, T>(ts.removeFirst(), ts.removeFirst(), ts.removeFirst(), measured));
        }
        if (ts.size() == 4) {
            ret.add(new Node2<V, T>(ts.removeFirst(), ts.removeFirst(), measured));
            ret.add(new Node2<V, T>(ts.removeFirst(), ts.removeFirst(), measured));
        } else if (ts.size() == 3) {
            ret.add(new Node3<V, T>(ts.removeFirst(), ts.removeFirst(), ts.removeFirst(), measured));
        } else {
            //ts.size() == 2
            ret.add(new Node2<V, T>(ts.removeFirst(), ts.removeFirst(), measured));
        }
        return IterableReverseIterables.fromList(ret);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private static final Digit.Matcher FIRST = new Digit.Matcher() {
        @Override
        public Object one(Object a) {
            return a;
        }

        @Override
        public Object two(Object a, Object b) {
            return a;
        }

        @Override
        public Object three(Object a, Object b, Object c) {
            return a;
        }

        @Override
        public Object four(Object a, Object b, Object c, Object d) {
            return a;
        }
    };

    private static <T> Digit.Matcher<T, T> firstMatcher() {
        return FIRST;
    }

    @Override
    public T first() {
        return prefix.match(Deep.<T>firstMatcher());
    }

    private static final Digit.Matcher LAST = new Digit.Matcher() {
        @Override
        public Object one(Object a) {
            return a;
        }

        @Override
        public Object two(Object b, Object a) {
            return a;
        }

        @Override
        public Object three(Object c, Object b, Object a) {
            return a;
        }

        @Override
        public Object four(Object d, Object c, Object b, Object a) {
            return a;
        }
    };

    private static <T> Digit.Matcher<T, T> lastMatcher() {
        return LAST;
    }

    @Override
    public T last() {
        return suffix.match(Deep.<T>lastMatcher());
    }

    @Override
    public FingerTree<V, T> removeFirst() {
        return prefix.match(new Digit.Matcher<T, FingerTree<V, T>>() {
            @Override
            public FingerTree<V, T> one(T a) {
                if (middle.isEmpty()) {
                    return FingerTrees.toTree(suffix, measured);
                } else {
                    return new Deep<V, T>(middle.first().toDigit(), middle.removeFirst(), suffix, measured);
                }
            }

            @Override
            public FingerTree<V, T> two(T a, T b) {
                return new Deep<V, T>(new One<V, T>(b, measured), middle, suffix, measured);
            }

            @Override
            public FingerTree<V, T> three(T a, T b, T c) {
                return new Deep<V, T>(new Two<V, T>(b, c, measured), middle, suffix, measured);
            }

            @Override
            public FingerTree<V, T> four(T a, T b, T c, T d) {
                return new Deep<V, T>(new Three<V, T>(b, c, d, measured), middle, suffix, measured);
            }
        });
    }

    @Override
    public FingerTree<V, T> removeLast() {
        return suffix.match(new Digit.Matcher<T, FingerTree<V, T>>() {
            @Override
            public FingerTree<V, T> one(T a) {
                if (middle.isEmpty()) {
                    return FingerTrees.toTree(prefix, measured);
                } else {
                    return new Deep<V, T>(prefix, middle.removeLast(), middle.last().toDigit(), measured);
                }
            }

            @Override
            public FingerTree<V, T> two(T b, T a) {
                return new Deep<V, T>(prefix, middle, new One<V, T>(b, measured), measured);
            }

            @Override
            public FingerTree<V, T> three(T c, T b, T a) {
                return new Deep<V, T>(prefix, middle, new Two<V, T>(c, b, measured), measured);
            }

            @Override
            public FingerTree<V, T> four(T d, T c, T b, T a) {
                return new Deep<V, T>(prefix, middle, new Three<V, T>(d, c, b, measured), measured);
            }
        });
    }

    @Override
    public Split<V, T> split(Predicate<V> predicate, V initial) {
        final V vpr = measured.sum(initial, prefix.measure());
        if (predicate.apply(vpr)) {
            final SplitDigit<V, T> splitDigit = splitDigit(measured, predicate, initial, prefix);
            return new Split<V, T>(FingerTrees.toTree(splitDigit.head, measured), splitDigit.t, deepL(splitDigit.tail, middle, suffix, measured));
        }
        final V vm = measured.sum(vpr, middle.measure());
        if (predicate.apply(vm)) {
            final Split<V, Node<V, T>> split = middle.split(predicate, vpr);
            final SplitDigit<V, T> splitDigit = splitDigit(measured, predicate, measured.sum(vpr, split.getHead().measure()), split.getElement());
            return new Split<V, T>(deepR(prefix, split.getHead(), splitDigit.head, measured), splitDigit.t, deepL(splitDigit.tail, split.getTail(), suffix, measured));
        } else {
            final SplitDigit<V, T> splitDigit = splitDigit(measured, predicate, vm, suffix);
            return new Split<V, T>(deepR(prefix, middle, splitDigit.head, measured), splitDigit.t, FingerTrees.toTree(splitDigit.tail, measured));
        }
    }

    private static <V,T> FingerTree<V, T> deepL(List<T> prefix, FingerTree<V, Node<V,T>> middle, Digit<V, T> suffix, Measured<V, T> measured) {
        if (prefix.isEmpty()) {
            if (middle.isEmpty()) {
                return FingerTrees.toTree(suffix, measured);
            } else {
                return new Deep<V, T>(middle.first().toDigit(), middle.removeFirst(), suffix, measured);
            }
        } else {
            final Digit<V,T> newPrefix;
            switch (prefix.size()) {
                case 1:
                    newPrefix = new One<V, T>(prefix.get(0), measured);
                    break;
                case 2:
                    newPrefix = new Two<V, T>(prefix.get(0), prefix.get(1), measured);
                    break;
                case 3:
                    newPrefix = new Three<V, T>(prefix.get(0), prefix.get(1), prefix.get(2), measured);
                    break;
                case 4:
                    newPrefix = new Four<V, T>(prefix.get(0), prefix.get(1), prefix.get(2), prefix.get(3), measured);
                    break;
                default:
                    throw new IllegalStateException("unreachable");
            }
            return new Deep<V, T>(newPrefix, middle, suffix, measured);
        }
    }

    private static <V,T> FingerTree<V, T> deepR(Digit<V,T> prefix, FingerTree<V, Node<V,T>> middle, List<T> suffix, Measured<V, T> measured) {
        if (suffix.isEmpty()) {
            if (middle.isEmpty()) {
                return FingerTrees.toTree(prefix, measured);
            } else {
                return new Deep<V, T>(prefix, middle.removeLast(), middle.last().toDigit(), measured);
            }
        } else {
            final Digit<V,T> newSuffix;
            switch (suffix.size()) {
                case 1:
                    newSuffix = new One<V, T>(suffix.get(0), measured);
                    break;
                case 2:
                    newSuffix = new Two<V, T>(suffix.get(0), suffix.get(1), measured);
                    break;
                case 3:
                    newSuffix = new Three<V, T>(suffix.get(0), suffix.get(1), suffix.get(2), measured);
                    break;
                case 4:
                    newSuffix = new Four<V, T>(suffix.get(0), suffix.get(1), suffix.get(2), suffix.get(3), measured);
                    break;
                default:
                    throw new IllegalStateException("unreachable");
            }
            return new Deep<V, T>(prefix, middle, newSuffix, measured);
        }
    }

    private static <V,T> SplitDigit<V,T> splitDigit(Measured<V,T> measured, Predicate<V> predicate, V initial, Iterable<T> digit) {
        final List<T> head = Lists.newArrayList();
        final List<T> tail = Lists.newArrayList();
        final Iterator<T> iterator = digit.iterator();
        V i = initial;
        while (iterator.hasNext()) {
            final T a = iterator.next();
            if (!iterator.hasNext()) {
                return new SplitDigit<V, T>(head, a, tail);
            }
            i = measured.sum(i, measured.measure(a));
            if (predicate.apply(i)) {
                while (iterator.hasNext()) {
                    tail.add(iterator.next());
                }
                return new SplitDigit<V, T>(head, a, tail);
            }
            head.add(a);
        }
        throw new IllegalStateException("unreachable");
    }

    private static final class SplitDigit<V,T> {
        private final List<T> head;
        private final T t;
        private final List<T> tail;

        private SplitDigit(List<T> head, T t, List<T> tail) {
            this.head = head;
            this.t = t;
            this.tail = tail;
        }
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<T> concat = Iterators.concat(Iterators.transform(middle.iterator(), new Function<Node<V, T>, Iterator<T>>() {
            @Override
            public Iterator<T> apply(Node<V, T> ts) {
                return ts.iterator();
            }
        }));
        return Iterators.concat(prefix.iterator(), concat, suffix.iterator());
    }
}
