package com.jeffplaisance.fingertree;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.jeffplaisance.util.fingertree.bytestring.ByteString;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class TestFingerTreeByteString extends TestCase {
    public void testStuff() throws IOException {
        long start = -System.nanoTime();
        ByteString byteString = ByteString.empty();
        for (char i = 'a'; i <= 'z'; i++) {
            for (char j = 'a'; j <= 'z'; j++) {
                for (char k = 'a'; k <= 'z'; k++) {
                    final byte[] bytes = new String(new char[]{i, j, k, '\n'}).getBytes(Charsets.UTF_8);
                    byteString = byteString.concat(ByteString.fromBytes(bytes));
                }
            }
        }

        ByteString bs2 = byteString;

        System.out.println((char)byteString.getByte(6));

        System.out.println();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteStreams.copy(byteString.newInput(), out);
        final String str = new String(out.toByteArray(), Charsets.UTF_8);
        assertEquals((char)byteString.getByte(6), str.charAt(6));
        final String substring = str.substring(117, 284);
        System.out.print(substring);
        System.out.println();

        byteString = byteString.substring(117, 284);
        byteString.writeTo(System.out);
        System.out.println();

        assertEquals(substring, CharStreams.toString(new InputStreamReader(byteString.newInput(), Charsets.UTF_8)));

        ByteString byteString2 = byteString.substring(0, byteString.length());
        byteString2.writeTo(System.out);

        start += System.nanoTime();
        System.out.println();
        System.out.println(start/1000000d+" ms");
    }
}
