package pl.mikf.uam.pob.peselset;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    @Test
    void write() throws Pesel.BadPesel, Store.Entry.BadNumberOfFields, IOException {
        String example1 = "44051401359 Michał Feiler";
        String example2a = "44041402359 Nieprąwda Dąprawdy";
        String example2 = "44041402359 Jąn Kąwąlskó";
        byte[] b = (example1+"\n"+example2+"\n").getBytes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Store n = new Store();
        n.add(new Store.Entry(example1));
        n.add(new Store.Entry(example2a));
        n.add(new Store.Entry(example2));
        n.write(out);
        out.close();
        //System.out.println(Arrays.toString(b));
        //System.out.println(Arrays.toString(out.toByteArray()));
        assertArrayEquals(b, out.toByteArray());
    }
}