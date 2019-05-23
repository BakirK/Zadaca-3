package ba.unsa.etf.rs.zadaca3;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void getIsbn() {
        Book k = new Book("a","b","c",1, LocalDate.now());
        assertEquals("c", k.getIsbn());
    }

    @Test
    void getPublishDate() {
        Book k = new Book("a","b","c",1, LocalDate.now());
        assertEquals(LocalDate.now(), k.getPublishDate());
    }

    @Test
    void setPublishDate() {
        Book k = new Book("a","b","c",1, LocalDate.of(2018, 11, 17));
        assertEquals(LocalDate.of(2018, 11, 17), k.getPublishDate());
    }

    @Test
    void toStringTest() {
        Book k = new Book("a","b","c",1,LocalDate.of(2018, 11, 17));
        String result = "" + k;
        String expected = "a, b, c, 1, 17. 11. 2018";
        assertEquals(expected, result);
    }

    @Test
    void idTest() {
        Book k = new Book("a","b","c",1,LocalDate.of(2018, 11, 17));
        assertEquals(0, k.getId());
    }

    @Test
    void idCtorTest() {
        Book k = new Book(1234,"a","b","c",1,LocalDate.of(2018, 11, 17));
        assertEquals(1234, k.getId());
    }
}