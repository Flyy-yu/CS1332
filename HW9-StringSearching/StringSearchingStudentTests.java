import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * String searching student tests.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class StringSearchingStudentTests {
    private List<Integer> sellAnswer;
    private List<Integer> emptyList;
    private SearchableString sell;
    private SearchableString sellNotThere;
    private SearchableString sellText;

    private List<Integer> kmpAnswer;
    private SearchableString kmpPattern;
    private SearchableString kmpText;
    private SearchableString kmpNotThere;

    public static final int TIMEOUT = 200;

    @Before
    public void setUp() {
        sell = new SearchableString("sell");
        sellNotThere = new SearchableString("sea lions trains cardinal "
                + "boardwalk");
        sellText = new SearchableString("She sells seashells by the seashore.");

        sellAnswer = new ArrayList<>();
        sellAnswer.add(4);

        emptyList = new ArrayList<>();

        kmpPattern = new SearchableString("ababa");
        kmpText = new SearchableString("ababaaababa");
        kmpNotThere = new SearchableString("ababbaba");

        kmpAnswer = new ArrayList<>();
        kmpAnswer.add(0);
        kmpAnswer.add(6);
    }

    @Test(timeout = TIMEOUT)
    public void testBuildFailureTable() {
        int[] failureTable = StringSearching.buildFailureTable(kmpPattern);
        int[] expected = {0, 0, 1, 2, 3};
        assertArrayEquals(expected, failureTable);
        assertTrue("Comparisons made: " + kmpPattern.getCount(),
                kmpPattern.getCount() <= 8);
    }

    @Test(timeout = TIMEOUT)
    public void testKMPThere() {
        assertEquals(kmpAnswer, StringSearching.kmp(kmpPattern, kmpText));
        assertTrue("Comparisons made: " + kmpText.getCount(),
                kmpText.getCount() <= 15);
    }

    @Test(timeout = TIMEOUT)
    public void testKMPNotThere() {
        assertEquals(emptyList, StringSearching.kmp(kmpPattern, kmpNotThere));
        assertTrue("Comparisons made: " + kmpNotThere.getCount(),
                kmpNotThere.getCount() <= 6);
    }

    @Test(timeout = TIMEOUT)
    public void testBuildLastTable() {
        Map<Character, Integer> lastTable = StringSearching
            .buildLastTable(sell);
        Map<Character, Integer> expectedLastTable = new HashMap<>();
        expectedLastTable.put('s', 0);
        expectedLastTable.put('e', 1);
        expectedLastTable.put('l', 3);
        assertEquals(expectedLastTable, lastTable);
        assertEquals(4, sell.getCount());
    }

    @Test(timeout = TIMEOUT)
    public void testBoyerMooreThere() {
        assertEquals(sellAnswer, StringSearching.boyerMoore(sell, sellText));
        assertTrue("sellText count was " + sellText.getCount()
                + ". Should be <= 20.", sellText.getCount() <= 20);
    }

    @Test(timeout = TIMEOUT)
    public void testBoyerMooreNotThere() {
        assertEquals(emptyList, StringSearching.boyerMoore(sell, sellNotThere));
        assertTrue("sellNotThere count was " + sellNotThere.getCount()
                + ". Should be <= 9.", sellNotThere.getCount() <= 9);
    }

    @Test(timeout = TIMEOUT)
    public void testGenerateHash() {
        assertEquals(277220518, StringSearching.generateHash(
                    "matt is my friend", 4));
    }

    @Test(timeout = TIMEOUT)
    public void testUpdateHash() {
        assertEquals(731294060, StringSearching.updateHash(99342732, 5, 'a',
                    'q'));
    }

    @Test(timeout = TIMEOUT)
    public void testRabinKarpThere() {
        assertEquals(sellAnswer, StringSearching.rabinKarp(sell, sellText));
        assertEquals(72, sellText.getCount());
    }

    @Test(timeout = TIMEOUT)
    public void testRabinKarpNotThere() {
        assertEquals(emptyList, StringSearching.rabinKarp(sell, sellNotThere));
        assertEquals(66, sellNotThere.getCount());
    }

    @Test(timeout = TIMEOUT)
    public void testBoyerMooreLongerText() {
        SearchableString text = sell;
        SearchableString pattern = sellNotThere;
        assertEquals(emptyList, StringSearching.boyerMoore(pattern, text));
        assertEquals(0, text.getCount());
        assertEquals(0, pattern.getCount());
    }
}
