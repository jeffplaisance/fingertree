package com.jeffplaisance.util.fingertree;

import com.google.common.base.Predicate;

import javax.annotation.Nullable;

public class Test {
    public static void main(String[] args) {
        FingerTree<Integer, String> tree = new Empty<Integer, String>(new Measured<Integer, String>() {
            @Override
            public Integer sum(Integer a, Integer b) {
                return a + b;
            }

            @Override
            public Integer measure(String a) {
                return a.length();
            }

            @Override
            public Integer zero() {
                return 0;
            }
        });
        for (char i = 'a'; i <= 'z'; i++) {
            for (char j = 'a'; j <= 'z'; j++) {
                tree = tree.append(i+""+j);
            }
        }
        tree = tree.split(new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 20;
            }
        }, 0).getHead();
        while (!tree.isEmpty()) {
            System.out.println(tree.first());
            tree = tree.removeFirst();
        }
    }
}
