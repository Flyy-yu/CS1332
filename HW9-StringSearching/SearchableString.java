/**
 * Class we will use for grading your StringSearching algorithms.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public final class SearchableString implements CharSequence {
    private String str;
    private int count;

    /**
     * Creates the SearchableString
     * @param s the string for the SearchableString to be created from
     */
    public SearchableString(String s) {
        str = s;
        count = 0;
    }

    @Override
    public char charAt(int i) {
        count++;
        return str.charAt(i);
    }

    @Override
    public int length() {
        return str.length();
    }

    /**
     * Returns the number of times charAt has been called for this object
     * @return the number of times charAt has been called for this object
     */
    public int getCount() {
        return count;
    }

    /**
     * Do NOT use this. It will not help at all.
     * @param start a parameter that should not be used
     * @param end another parameter that should not be used
     * @return never
     */
    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("Do not use method "
                + "subSequence.");
    }

    /**
     * Do NOT use this (with the exception of debugging your code).
     * If you use this method for any other purpose,
     * you WILL get a 0 on the entire assignment.
     * @return debugging messsage
     */
    @Override
    public String toString() {
        return "SearchableString containing: " + str + " (debug use only)";
    }

    /**
     * Do NOT use this. It will not help at all.
     * @param o a parameter that should not be used
     * @return never
     */
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Do not use method equals.");
    }
}
