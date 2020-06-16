package pl.mikf.uam.pob.peselset;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.NullType;

import static org.junit.jupiter.api.Assertions.*;

class PeselTest {
    @Test
    void constructorThrowsOnBadDigit() {
        Pesel.BadChecksum thrown =
                assertThrows(Pesel.BadChecksum.class,
                        () -> new Pesel("11111111111"));
    }

    @Test
    void constructorThrowsOnOneChar() {
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class,
                        () -> new Pesel("1"));
    }

    @Test
    void wikipediaExampleCorrected() throws Pesel.BadPesel {
        new Pesel("44051401359");
    }

    @Test
    void wikipediaExampleCorrectedModded() throws Pesel.BadPesel {
        new Pesel("44041402359");
    }

    @Test
    void wikipediaExampleOriginalError() {
        Pesel.BadChecksum thrown = assertThrows(Pesel.BadChecksum.class,
                () -> new Pesel("44051401358"));
    }
}