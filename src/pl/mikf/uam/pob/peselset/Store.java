package pl.mikf.uam.pob.peselset;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;

public class Store implements Iterable<Store.Entry> {
    private Vector<Entry> vec = new Vector<>();

    public int size() {
        return vec.size();
    }

    public boolean isEmpty() {
        return vec.isEmpty();
    }

    public boolean contains(Object o) {
        for (Entry i : vec) {
            if (i.pesel.equals(o)) return true;
        }
        return false;
    }

    public void add(Entry entry) {
        int i = vec.indexOf(entry);
        if (i == -1) vec.add(entry);
        else vec.setElementAt(entry, i);
    }

    public Iterator<Entry> iterator() {
        return vec.iterator();
    }

    public void write(OutputStream writer) throws IOException {
        for(Entry i : this) {
            writer.write((i.toString()+"\n").getBytes());
        }
    }

    public static class Entry {
        public final Pesel pesel;
        public final String firstName, lastName;

        public static class BadNumberOfFields extends Exception {
            int number;

            public BadNumberOfFields(int number) {
                this.number = number;
            }
        }

        public Entry(String line) throws BadNumberOfFields, Pesel.BadPesel {
            String[] fields = line.split(" ");
            if (fields.length != 3)
                throw new BadNumberOfFields(fields.length);
            pesel = new Pesel(fields[0]);
            firstName = fields[1];
            lastName = fields[2];
        }

        @Override
        public String toString() {
            return pesel + " " + firstName + " " + lastName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            return pesel.equals(entry.pesel);
        }

        @Override
        public int hashCode() {
            return pesel.hashCode();
        }
    }
}
