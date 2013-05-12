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

package com.jeffplaisance.fingertree;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jeffplaisance.util.fingertree.list.IndexedList;
import junit.framework.TestCase;

import java.util.List;
import java.util.Random;

public class TestIndexedList extends TestCase {
    public void testStuff() {
        final List<Integer> list = Lists.newArrayList();
        IndexedList<Integer> iList = IndexedList.empty();
        final Random r = new Random(0);
        for (int i = 0; i < 100000; i++) {
            final int rand = r.nextInt();
            list.add(rand);
            iList = iList.add(rand);
        }
        for (int i = 0; i < 100000; i++) {
            final int index = r.nextInt(100000);
            assertEquals(list.get(index), iList.get(index));
        }
        final int start = r.nextInt(list.size());
        final int length = r.nextInt(list.size()-start);
        assertTrue(Iterables.elementsEqual(list.subList(start, start + length), iList.subList(start, start + length)));
    }
}
