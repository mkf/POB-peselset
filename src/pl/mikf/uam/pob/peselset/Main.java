package pl.mikf.uam.pob.peselset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String miasto = scanner.nextLine();
        File plik = new File(miasto + ".txt");
        if (!plik.createNewFile())
            System.err.println("Nie udało się utworzyć pliku.");
        else {
            Store store = new Store();
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                if (linia.length() == 0) break;
                try {
                    store.add(new Store.Entry(linia));
                } catch (Store.Entry.BadNumberOfFields badNumberOfFields) {
                    System.err.println("Zła liczba pól. " + badNumberOfFields.number + " . ");
                } catch (Pesel.BadPesel badPesel) {
                    System.err.println("Zły pesel." + badPesel.getClass().getName() + " . ");
                }
            }
            FileOutputStream writer = new FileOutputStream(plik);
            store.write(writer);
            writer.close();
        }
    }
}
