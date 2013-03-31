package com.jeffplaisance.util;

import com.google.common.collect.Ordering;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Map;

public final class Pair<A,B> implements Map.Entry<A, B> {

    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    private static final Ordering<Pair> HALF_COMPARATOR = new Ordering<Pair>() {
        @Override
        public int compare(@Nullable Pair o1, @Nullable Pair o2) {
            return Ordering.natural().compare((Comparable)o1.a, (Comparable) o2.a);
        }
    };

    private static final Ordering<Pair> FULL_COMPARATOR = HALF_COMPARATOR.compound(new Comparator<Pair>() {
        @Override
        public int compare(Pair o1, Pair o2) {
            return Ordering.natural().compare((Comparable)o1.b, (Comparable)o2.b);
        }
    });

    private final A a;

    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public <Z> Z match(Matcher<A, B, Z> matcher) {
        return matcher.apply(a, b);
    }

    public static interface Matcher<A, B, Z> {
        public Z apply(A a, B b);
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    @Override
    public A getKey() {
        return a;
    }

    @Override
    public B getValue() {
        return b;
    }

    @Override
    public B setValue(B value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (a != null ? !a.equals(pair.a) : pair.a != null) return false;
        if (b != null ? !b.equals(pair.b) : pair.b != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    public static <A,B> Comparator<Pair<A,B>> comparator() {
        return (Comparator<Pair<A,B>>)(Comparator)FULL_COMPARATOR;
    }

    public static <A,B> Comparator<Pair<A,B>> comparator(final Comparator<A> aComparator, final Comparator<B> bComparator) {
        return Ordering.from(new Comparator<Pair<A, B>>() {
            @Override
            public int compare(Pair<A, B> o1, Pair<A, B> o2) {
                return aComparator.compare(o1.a, o2.a);
            }
        }).compound(new Comparator<Pair<A, B>>() {
            @Override
            public int compare(Pair<A, B> o1, Pair<A, B> o2) {
                return bComparator.compare(o1.b, o2.b);
            }
        });
    }

    public static <A,B> Comparator<Pair<A,B>> halfComparator() {
        return (Comparator<Pair<A,B>>)(Comparator)HALF_COMPARATOR;
    }

    public static <A,B> Comparator<Pair<A,B>> halfComparator(final Comparator<A> aComparator) {
        return new Comparator<Pair<A, B>>() {
            @Override
            public int compare(Pair<A, B> o1, Pair<A, B> o2) {
                return aComparator.compare(o1.a, o2.a);
            }
        };
    }
}
