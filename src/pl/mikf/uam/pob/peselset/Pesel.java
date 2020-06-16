package pl.mikf.uam.pob.peselset;

import java.util.Arrays;

public class Pesel {

    private final char[] c;

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

    @Override
    public String toString() {
        return String.valueOf(c);
    }

    public static class BadPesel extends Exception {
    }

    public static class BadChecksum extends BadPesel {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pesel pesel = (Pesel) o;

        return Arrays.equals(c, pesel.c);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(c);
    }

    public static boolean isDigit(char c) {
        assert '0' == '1' - 1;
        return c >= '0' && c <= '9';
    }

    public static int value(char c) {
        if (!isDigit(c))
            throw new IllegalArgumentException(
                    String.valueOf(c));
        return c - '0';
    }

    public int v(int x) {
        return value(c[x]);
    }


    public int vv(int x) {
        return v(x) * 10 + v(x + 1);
    }

    public int v(int x, int y) {
        assert y == x + 1;
        return vv(x);
    }

    public int v(char x) {
        return v(x - 'a');
    }

    public int v(char x, char y) {
        return v(x - 'a', y - 'a');
    }

    public static boolean even(int i) {
        return i % 2 == 0;
    }

    public boolean monthCenturyCheck() {
        return even(v('a')) ? v('b') != 0 : v('b') <= 2;
    }

    public int century() {
        int r = v('a') / 2;
        if (r == 4) r = -1;
        return r * 100 + 1900;
    }

    public int centurysYear() {
        return v('a') % 2 * 10 + v('b');
    }

    public boolean isLeapYear() {
        int y = centurysYear();
        return y % 4 == 0 && (y != 0 || v('a') / 2 == 1);
    }

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

    public boolean dayCheck() {
        int d = v('e', 'f');
        return d > 0 && d <= monthDaysCount();
    }

    public static final int[] FOR_A = {9, 7, 3, 1, 9, 7, 3, 1, 9, 7};
    public static final int[] FOR_B = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1};

    public boolean checkSumMethodA() {
        int acc = -v('k');
        for (int i = 0; i < 10; i++)
            acc = (acc + FOR_A[i] * v(i)) % 10;
        return acc == 0;
    }

    public boolean checkSumMethodB() {
        int acc = 0;
        for (int i = 0; i < 11; i++)
            acc = (acc + FOR_B[i] * v(i)) % 10;
        return acc == 0;
    }

}
