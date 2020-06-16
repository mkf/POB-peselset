package pl.mikf.uam.pob.peselset;

import java.util.Arrays;

public class Pesel {

    private final char[] c;

    /**
     * @param s -- a string of 11 decimal digits being a PESEL
     * @throws BadPesel if the PESEL checksum is wrong or if month is illegal or day is too high
     */
    public Pesel(String s) throws BadPesel {
        c = s.toCharArray();
        if (c.length != 11)
            throw new IllegalArgumentException();
        boolean a;
        a = checkSumMethodA();
        assert a == checkSumMethodB();
        if (!a) throw new BadChecksum();
        if (!(monthCenturyCheck() && dayCheck()))
            throw new BadPesel();
    }

    /**
     * @return simply returns a String of 11 decimal digits and nothing else, as on input
     */
    @Override
    public String toString() {
        return String.valueOf(c);
    }

    public static class BadPesel extends Exception {
    }

    public static class BadChecksum extends BadPesel {
    }

    /**
     * @param o -- the other object
     * @return true iff same class and not a subclass and same chars of pesel
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pesel pesel = (Pesel) o;

        return Arrays.equals(c, pesel.c);
    }

    /**
     * @return just Arrays.hashCode of the chars array of PESEL
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(c);
    }

    /**
     * @param c -- a character supposed to be an ASCII code for a digit
     * @return whether it is a decimal digit. also has an assertion for whether char 1 is right after char 0.
     */
    public static boolean isDigit(char c) {
        assert '0' == '1' - 1;
        return c >= '0' && c <= '9';
    }

    /**
     * @param c -- a dec digit ASCII char code
     * @return the integer value of that digit
     */
    public static int value(char c) {
        if (!isDigit(c))
            throw new IllegalArgumentException(
                    String.valueOf(c));
        return c - '0';
    }

    /**
     * @param x -- index (from zero) of pesel digits
     * @return the integer value of the digit under index
     */
    public int v(int x) {
        return value(c[x]);
    }

    /**
     * @param x -- an index between 0 and 9, incl
     * @return x'th digit of pesel indexed from zero multiplied by ten plus x'th digit of pesel indexed from one
     */
    public int vv(int x) {
        return v(x) * 10 + v(x + 1);
    }

    /**
     * @param x an index between 0 and 9, incl
     * @param y an index one greater than x
     * @return after an assertion that y is one greater than x, (zero-indexed) x'th digit times 10 plus y'th times 1
     */
    public int v(int x, int y) {
        assert y == x + 1;
        return vv(x);
    }

    /**
     * @param x instead of numbering the 11 digits of pesel with numbers,
     *          we numerate them with chars a-k like wikipedia
     * @return the digit of pesel under the letter-index, as integer
     */
    public int v(char x) {
        return v(x - 'a');
    }

    /**
     * numerating the 11 digits of pesel with chars a-k.
     * @param x one less than y
     * @param y one greater than x
     * @return the digit for x times 10 plus the digit for y times one
     */
    public int v(char x, char y) {
        return v(x - 'a', y - 'a');
    }

    public static boolean even(int i) {
        return i % 2 == 0;
    }

    /**
     * @return whether the month is valid, regardless of the mod20 century mark
     */
    public boolean monthCenturyCheck() {
        return even(v('c')) ? v('d') != 0 : v('d') <= 2;
    }

    /**
     * @return the century as one of 1800, 1900, 2000, 2100, 2200
     */
    public int century() {
        int r = v('a') / 2;
        if (r == 4) r = -1;
        return r * 100 + 1900;
    }

    /**
     * @return the year of birth of the pesel-owner, modulo 100 (like, just the 98 from 1998)
     */
    public int centurysYear() {
        return v('a') % 2 * 10 + v('b');
    }


    /**
     * @return whether the birth year was leap. takes into account the 4, the 100, and the 400.
     */
    public boolean isLeapYear() {
        int y = centurysYear();
        return y % 4 == 0 && (y != 0 || v('a') / 2 == 1);
    }

    /**
     * @return how many days did the month of birth have (in the year of birth -- Feb in leap/common)
     */
    public int monthDaysCount() {
        switch (v('c', 'd') % 20) {
            case 2:
                return isLeapYear() ? 29 : 28;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            default:
                return 30;
        }
    }

    /**
     * @return whether the day is not too high for the month (and for the year -- Feb in leap/common)
     */
    public boolean dayCheck() {
        int d = v('e', 'f');
        return d > 0 && d <= monthDaysCount();
    }

    public static final int[] FOR_A = {9, 7, 3, 1, 9, 7, 3, 1, 9, 7};
    public static final int[] FOR_B = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1};

    /**
     * @return checking the checksum digit by calculating the correct one and comparing
     */
    public boolean checkSumMethodA() {
        int acc = -v('k');
        for (int i = 0; i < 10; i++)
            acc = (acc + FOR_A[i] * v(i)) % 10;
        return acc == 0;
    }

    /**
     * @return checking the checksum digit by having the accumulator render a modulo zero after adding the checksum digit
     */
    public boolean checkSumMethodB() {
        int acc = 0;
        for (int i = 0; i < 11; i++)
            acc = (acc + FOR_B[i] * v(i)) % 10;
        return acc == 0;
    }

}
