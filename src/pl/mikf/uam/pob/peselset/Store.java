package pl.mikf.uam.pob.peselset;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

public class Store implements Iterable<Store.Entry> {
    private Vector<Entry> vec = new Vector<>();

    /**
     * replaces in-place if there is one with same pesel, appends to end if there is none
     * @param entry -- the entry that we want to add
     */
    public void add(Entry entry) {
        int i = vec.indexOf(entry);
        if (i == -1) vec.add(entry);
        else vec.setElementAt(entry, i);
    }

    /**
     * @return an iterator of all the entries in order of addition
     */
    public Iterator<Entry> iterator() {
        return vec.iterator();
    }

    /**
     * writes the lines "pesel firstname lastname" to the outputstream, ending each with newline character
     * will do it in order in which they were added
     * @param writer -- an OutputStream, like FileOutputStream or ByteArrayOutputStream
     * @throws IOException if the write method of the OS throws IOException
     */
    public void write(OutputStream writer) throws IOException {
        for (Entry i : this) {
            writer.write((i.toString() + "\n").getBytes());
        }
    }

    public static class Entry {
        public final Pesel pesel;
        public final String firstName, lastName;

        /**
         * a line was given to the Entry constructor that had not just three fields. could have been zero.
         */
        public static class BadNumberOfFields extends Exception {
            int number;

            public BadNumberOfFields(int number) {
                this.number = number;
            }
        }

        /**
         * @param line : a line of exactly three space-delimited substrings (pesel, firstname, lastname)
         * @throws BadNumberOfFields if there is not three but zero or any other number
         * @throws Pesel.BadPesel if there are three but the pesel is deemed invalid
         */
        public Entry(String line) throws BadNumberOfFields, Pesel.BadPesel {
            String[] fields = line.split(" ");
            if (fields.length != 3)
                throw new BadNumberOfFields(fields.length);
            pesel = new Pesel(fields[0]);
            firstName = fields[1];
            lastName = fields[2];
        }

        /**
         * @return returns the 11 digits of pesel followed by a space, the firstname, and the lastname.
         * no final delimiting character like a newline or something.
         */
        @Override
        public String toString() {
            return pesel + " " + firstName + " " + lastName;
        }

        /**
         * @param o -- another object to compare
         * @return not really whether the objects are both equal entries
         * but only whether both are entries and whether both have the same pesels.
         * It came in handy for the use of indexOf on vec which was just neat.
         * I only found a proper predicate-using method for removal and not for getting index.
         * Yea I know I shouldn't have done that to an equals method.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            return pesel.equals(entry.pesel);
        }

        /**
         * @return not really the hashcode of pesel with names but the hashcode of just the pesel object.
         * it came in handy to use indexOf on vec. See equals of this class
         * not sure if indexOf even needed the hashCode method, but why not add it if there's equals.
         */
        @Override
        public int hashCode() {
            return pesel.hashCode();
        }
    }
}
