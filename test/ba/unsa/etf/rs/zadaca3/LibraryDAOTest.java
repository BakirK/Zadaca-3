package ba.unsa.etf.rs.zadaca3;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LibraryDAOTest {

    @Test
    void getCurrentBook() {
        LibraryDAO m = LibraryDAO.getInstance();
        assertNull(m.getCurrentBook());
    }

    @Test
    void deleteInstance() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.defaultData();
        assertEquals(3, m.getBooks().size());

        LibraryDAO.deleteInstance();

        m = LibraryDAO.getInstance();
        assertEquals(3, m.getBooks().size());
        m.clearAll();
        assertEquals(0, m.getBooks().size());
    }

    @Test
    void getBookList() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.defaultData();
        assertTrue(m.getBookList().contains("Meša Selimović, Tvrđava"));
        assertTrue(m.getBookList().contains("Ivo Andrić, Travnička hronika"));
        assertTrue(m.getBookList().contains("J. K. Rowling, Harry Potter"));
        assertFalse(m.getBookList().contains("a, a, a"));
        m.addBook(new Book("a", "a", "a", 10, LocalDate.now()));
        assertTrue(m.getBookList().contains("a, a, a"));
    }

    @Test
    void getBooks() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.defaultData();
        boolean test1 = false, test2 = false, test3 = false;
        for (Book b : m.getBooks()) {
            if (b.getAuthor().equals("Meša Selimović")) test1 = true;
            if (b.getTitle().equals("Travnička hronika")) test2 = true;
            if (b.getIsbn().equals("abcd")) test3 = true;
        }
        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
    }

    @Test
    void setCurrentBook() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.defaultData();
        Book k = m.getBooks().get(0);
        m.setCurrentBook(k);
        assertEquals(k, m.getCurrentBook());
    }

    @Test
    void deleteBook() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.defaultData();
        Book k = m.getBooks().get(1);
        m.setCurrentBook(k);
        m.deleteBook();

        boolean test1 = false;
        for (Book b : m.getBooks()) {
            if (b.getAuthor().equals("Ivo Andrić")) test1 = true;
            if (b.getTitle().equals("Travnička hronika")) test1 = true;
        }
        assertFalse(test1);
        assertNull(m.getCurrentBook());
    }

    @Test
    void addBook() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.addBook(new Book("a", "a", "a", 10, LocalDate.now()));
        m.addBook(new Book("b", "b", "b", 20, LocalDate.now()));
        m.addBook(new Book("c", "c", "c", 30, LocalDate.now()));
        assertEquals(3, m.getBooks().size());
        assertNull(m.getCurrentBook());

        boolean test1 = false, test2 = false, test3 = false;
        for (Book b : m.getBooks()) {
            if (b.getAuthor().equals("a") && b.getTitle().equals("a") && b.getIsbn().equals("a") && b.getPageCount() == 10)
                test1 = true;
            if (b.getAuthor().equals("b") && b.getTitle().equals("b") && b.getIsbn().equals("b") && b.getPageCount() == 20)
                test2 = true;
            if (b.getAuthor().equals("c") && b.getTitle().equals("c") && b.getIsbn().equals("c") && b.getPageCount() == 30)
                test3 = true;
        }

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
    }


    @Test
    void addDeleteCurrent() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        Book b = new Book("a", "a", "a", 10, LocalDate.now());
        m.addBook(b);
        m.defaultData();
        m.setCurrentBook(b);
        assertEquals(4, m.getBooks().size());
        assertSame(b, m.getCurrentBook());

        m.deleteBook();
        assertNull(m.getCurrentBook());

        m.deleteBook();

        assertEquals(3, m.getBooks().size());
    }

    @Test
    void updateCurrentBook() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        Book b1 = new Book("a", "a", "a", 10, LocalDate.now());
        Book b2 = new Book("b", "b", "b", 20, LocalDate.now());
        Book b3 = new Book("c", "c", "c", 30, LocalDate.now());
        m.addBook(b1);
        m.addBook(b2);
        m.addBook(b3);
        m.setCurrentBook(m.getBooks().get(1));

        Book b4 = new Book("d", "d", "d", 40, LocalDate.now());
        m.updateCurrentBook(b4);

        assertEquals(3, m.getBooks().size());

        boolean test1 = false, test2 = false, test3 = false;
        for (Book b : m.getBooks()) {
            if (b.getAuthor().equals("a") && b.getTitle().equals("a") && b.getIsbn().equals("a") && b.getPageCount() == 10)
                test1 = true;
            if (b.getAuthor().equals("d") && b.getTitle().equals("d") && b.getIsbn().equals("d") && b.getPageCount() == 40)
                test2 = true;
            if (b.getAuthor().equals("c") && b.getTitle().equals("c") && b.getIsbn().equals("c") && b.getPageCount() == 30)
                test3 = true;
        }

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
    }


    @Test
    void updateCurrentBookDefaultData() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.defaultData();
        m.setCurrentBook(m.getBooks().get(1));

        Book b4 = new Book("d", "d", "d", 40, LocalDate.now());
        m.updateCurrentBook(b4);

        boolean test1 = false, test2 = false, test3 = false, test4 = true;
        for (Book b : m.getBooks()) {
            if (b.getAuthor().equals("Meša Selimović")) test1 = true;
            if (b.getTitle().equals("Travnička hronika")) { test2 = true; System.out.println(b); }
            if (b.getIsbn().equals("abcd")) test3 = true;
            if (b.getAuthor().equals("d") && b.getTitle().equals("d") && b.getIsbn().equals("d") && b.getPageCount() == 40)
                test4 = true;
        }

        assertTrue(test1);
        assertFalse(test2);
        assertTrue(test3);
        assertTrue(test4);
    }


    @Test
    void addBookPersist() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        m.addBook(new Book("a", "a", "a", 10, LocalDate.now()));
        m.addBook(new Book("b", "b", "b", 20, LocalDate.now()));
        m.addBook(new Book("c", "c", "c", 30, LocalDate.now()));
        m = null;

        LibraryDAO.deleteInstance();

        LibraryDAO m2 = LibraryDAO.getInstance();
        assertEquals(3, m2.getBooks().size());

        boolean test1 = false, test2 = false, test3 = false;
        for (Book b : m2.getBooks()) {
            if (b.getAuthor().equals("a") && b.getTitle().equals("a") && b.getIsbn().equals("a") && b.getPageCount() == 10)
                test1 = true;
            if (b.getAuthor().equals("b") && b.getTitle().equals("b") && b.getIsbn().equals("b") && b.getPageCount() == 20)
                test2 = true;
            if (b.getAuthor().equals("c") && b.getTitle().equals("c") && b.getIsbn().equals("c") && b.getPageCount() == 30)
                test3 = true;
        }

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
    }


    @Test
    void updateCurrentBookPersist() {
        LibraryDAO m = LibraryDAO.getInstance();
        m.clearAll();
        Book b1 = new Book("a", "a", "a", 10, LocalDate.now());
        Book b2 = new Book("b", "b", "b", 20, LocalDate.now());
        Book b3 = new Book("c", "c", "c", 30, LocalDate.now());
        m.addBook(b1); m.addBook(b2); m.addBook(b3);
        m.setCurrentBook(m.getBooks().get(1));

        Book b4 = new Book("d", "d", "d", 40, LocalDate.now());
        m.updateCurrentBook(b4);
        m = null;

        LibraryDAO.deleteInstance();

        LibraryDAO m2 = LibraryDAO.getInstance();
        assertEquals(3, m2.getBooks().size());
        m2.printBooks();

        boolean test1=false, test2=false, test3=false;
        for(Book b : m2.getBooks()) {
            if (b.getAuthor().equals("a") && b.getTitle().equals("a") && b.getIsbn().equals("a") && b.getPageCount() == 10) test1 = true;
            if (b.getAuthor().equals("d") && b.getTitle().equals("d") && b.getIsbn().equals("d") && b.getPageCount() == 40) test2 = true;
            if (b.getAuthor().equals("c") && b.getTitle().equals("c") && b.getIsbn().equals("c") && b.getPageCount() == 30) test3 = true;
        }

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
    }
}