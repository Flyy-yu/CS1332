import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test cases for the Rabin-Karp algorithm
 * of the string searching assignment.
 *
 * @version 1.0
 * Created by Arshad Hosein on 09/11/2016.
 * Based on code from Timothy J. Aveni with his permission.
 */
public class test {

    @Rule
    public Timeout globalTimeout = new Timeout(2000L, TimeUnit.MILLISECONDS);

    final PrintStream originalSystemOut = System.out;
    CleanOutputStream cleanOutputStream;

    private class CleanOutputStream extends OutputStream {

        private boolean clean = true;

        @Override
        public void write(int b) throws IOException {
            clean = false;
            originalSystemOut.write(b);
        }

        public boolean isClean() {
            return clean;
        }
    }

    private void assertException(String message, Class<? extends Exception> exceptionClass, Runnable code) {
        assertException(message, new Class[]{exceptionClass}, code);
    }

    private void assertException(String message, Class<? extends Exception>[] exceptionClasses, Runnable code) {
        try {
            code.run();
            Assert.fail(message);
        } catch (Exception e) {
            boolean foundException = false;
            for (Class<? extends Exception> exceptionClass : exceptionClasses) {
                if (exceptionClass.equals(e.getClass())) {
                    foundException = true;
                }
            }

            if (!foundException) {
                e.printStackTrace();
                Assert.fail(message);
            } else {
                assertNotNull(
                        "Exception messages must not be empty",
                        e.getMessage());
                assertNotEquals(
                        "Exception messages must not be empty",
                        "",
                        e.getMessage());
            }
        }
    }

    private class StrictString implements CharSequence {
        private String str;

        /**
         * Creates the SearchableString
         *
         * @param s the string for the SearchableString to be created from
         */
        public StrictString(String s) {
            str = s;
        }

        @Override
        public char charAt(int i) {
            return str.charAt(i);
        }

        @Override
        public int length() {
            return str.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            Assert.fail("You used the subSequence method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method "
                    + "subSequence.");
        }

        @Override
        public String toString() {
            Assert.fail("You used the toString method on a CharSequence. That's forbidden! "
                    + "Like, so forbidden, the javadoc says \"you WILL get a 0 on the entire assignment.\" "
                    + "If you can't figure out how to do it without toString(), you're probably better off just not including that method.");
            return "SearchableString containing: " + str + " (debug use only)";
        }

        @Override
        public boolean equals(Object o) {
            Assert.fail("You used the equals method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method equals.");
        }
    }

    private class CountingSequence implements CharSequence {
        private String str;
        private int[] counts;

        public CountingSequence(String s) {
            str = s;
            counts = new int[s.length()];
        }

        @Override
        public char charAt(int i) {
            counts[i]++;
            return str.charAt(i);
        }

        @Override
        public int length() {
            return str.length();
        }

        public int[] getCounts() {
            return counts;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            Assert.fail("You used the subSequence method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method "
                    + "subSequence.");
        }

        @Override
        public String toString() {
            Assert.fail("You used the toString method on a CharSequence. That's forbidden! "
                    + "Like, so forbidden, the javadoc says \"you WILL get a 0 on the entire assignment.\" "
                    + "If you can't figure out how to do it without toString(), you're probably better off just not including that method.");
            return "SearchableString containing: " + str + " (debug use only)";
        }

        @Override
        public boolean equals(Object o) {
            Assert.fail("You used the equals method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method equals.");
        }
    }

    @Before
    public void init() {
        cleanOutputStream = new CleanOutputStream();
        System.setOut(new PrintStream(cleanOutputStream));
    }

    @After
    public void checkOutput() {
        assertTrue(
                "You used print statements somewhere in your code. That's forbidden!",
                cleanOutputStream.isClean());
    }

    private String getAlphabetString(int numChars) {
        String str = "";
        char c = 'a';
        for (int i = 0; i < numChars; i++) {
            str += c++;
        }
        return str;
    }

    @Test
    public void testGenerateHashExceptions() {
        assertException("Your generateHash() did not throw IllegalArgumentException for null text.",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash(null, 1));
        assertException("Your generateHash() did not throw IllegalArgumentException for negative length.",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash("a", -1));
        assertException("Your generateHash() did not throw IllegalArgumentException for length greater than that of the pattern.",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash("a", 10));
    }

    @Test
    public void testUpdateHashExceptions() {
        assertException("Your updateHash() did not throw IllegalArgumentException for negative length.",
                IllegalArgumentException.class,
                () -> StringSearching.updateHash(0, -1 , 'a', 'a'));
    }

    @Test
    public void testRabinKarpExceptions() {
        assertException("Your rabinKarp() did not throw IllegalArgumentException for null text.",
                IllegalArgumentException.class,
                () -> StringSearching.rabinKarp("a", null));
        assertException("Your rabinKarp() did not throw IllegalArgumentException for null pattern.",
                IllegalArgumentException.class,
                () -> StringSearching.rabinKarp(null, "a"));
        assertException("Your rabinKarp() did not throw IllegalArgumentException for empty pattern.",
                IllegalArgumentException.class,
                () -> StringSearching.rabinKarp("", "a"));
    }

    @Test
    public void testGenerateSimpleHash() {
        CountingSequence text = new CountingSequence(getAlphabetString(5));
        assertEquals("Your generateHash() did not return the right hash.",
                18228966,
                StringSearching.generateHash(text, 3));
    }

    @Test
    public void testGenerateHashForEmptyText() {
        CountingSequence text = new CountingSequence("");
        assertEquals("Your hash function did not return zero for an empty string.",
                0,
                StringSearching.generateHash(text, 0));
        assertArrayEquals("Your generateHash() had too many calls to charAt()).",
                new int[]{},
                text.getCounts());

    }

    @Test
    public void testGenerateHashWithZeroLength() {
        CountingSequence text = new CountingSequence("a");
        assertEquals("Your hash function did not return zero for an empty string.",
                0,
                StringSearching.generateHash(text, 0));
        assertArrayEquals("Your generateHash() had too many calls to charAt()).",
                new int[]{0},
                text.getCounts());
    }

    @Test
    public void testUpdateSimpleHash() {
        assertEquals("Your updateHash() did not return the correct value.",
                18416889,
                StringSearching.updateHash(18228966, 3, 'a', 'd'));
    }

    @Test
    public void testUpdateHashWithSameChar() {
        int oldHash = StringSearching.generateHash("aaa", 2);

        assertEquals("Your generateHash() did not return the right hash initially.",
                42098,
                oldHash);

        assertEquals("Your updateHash() returned a different value when the same character was added and removed.",
                oldHash,
                StringSearching.updateHash(oldHash, 2, 'a', 'a'));

    }

    @Test
    public void testSimpleRabinKarp1() {
        CountingSequence text = new CountingSequence("abcd");
        StrictString pattern = new StrictString("abcd");

        List<Integer> expected = new ArrayList<>();
        expected.add(0);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {2, 2, 2, 2},
                text.getCounts());
    }

    @Test
    public void testSimpleRabinKarp2() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcde");

        List<Integer> expected = new ArrayList<>();
        expected.add(0);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[]{3, 2, 2, 2, 1},
                text.getCounts());
    }

    @Test
    public void testSimpleRabinKarp3() {
        StrictString pattern = new StrictString("bcde");
        CountingSequence text = new CountingSequence("abcde");

        List<Integer> expected = new ArrayList<>();
        expected.add(1);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {2, 2, 2, 2, 2},
                text.getCounts());

    }

    @Test
    public void testRabinKarpNoMatch1() {
        StrictString pattern = new StrictString("bcde");
        CountingSequence text = new CountingSequence("abcd");

        List<Integer> expected = new ArrayList<>();

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {1, 1, 1, 1},
                text.getCounts());
    }

    @Test
    public void testRabinKarpNoMatch2() {
        StrictString pattern = new StrictString("bcde");
        CountingSequence text = new CountingSequence("abcda");

        List<Integer> expected = new ArrayList<>();

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {2, 1, 1, 1, 1},
                text.getCounts());
    }

    @Test
    public void testRabinKarpMatchAtEnd() {
        StrictString pattern = new StrictString("cdef");
        CountingSequence text = new CountingSequence("abcdef");

        List<Integer> expected = new ArrayList<>();
        expected.add(2);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {2, 2, 2, 2, 2, 2},
                text.getCounts());
    }

    @Test
    public void testRabinKarpMultipleMatches1() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcdabcd");

        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(4);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {3, 3, 3, 3, 2, 2, 2, 2},
                text.getCounts());
    }

    @Test
    public void testRabinKarpMultipleMatches2() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcdeabcd");

        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(5);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {3, 3, 3, 3, 2, 2, 2, 2, 2},
                text.getCounts());
    }

    @Test
    public void testRabinKarpOverlappingMatches() {
        StrictString pattern = new StrictString("abca");
        CountingSequence text = new CountingSequence("abcabca");

        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(3);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {3, 3, 3, 3, 2, 2, 2},
                text.getCounts());
    }

    @Test
    public void testRabinKarpHashCollision() {
        StrictString pattern = new StrictString("aa" + (char) 866);
        CountingSequence text = new CountingSequence("aa" + (char) 866
                + "ab" + (char) 433);

        List<Integer> expected = new ArrayList<>();
        expected.add(0);

        assertEquals("A Rabin-Karp search failed to return the correct result.",
                expected,
                StringSearching.rabinKarp(pattern, text));

        assertArrayEquals("Your rabinKarp() had incorrect calls to charAt().",
                new int[] {3, 3, 3, 2, 2, 1},
                text.getCounts());

    }
}