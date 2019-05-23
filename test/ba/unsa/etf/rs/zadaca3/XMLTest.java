package ba.unsa.etf.rs.zadaca3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Testovi XML funkcija

class XMLTest {
    LibraryDAO model;

    @Test
    public void testSave () {
        model = LibraryDAO.getInstance();
        model.clearAll();
        model.defaultData();

        // Fiksiramo jedan datum da ga možemo provjeriti
        model.getBooks().get(0).setPublishDate(LocalDate.of(2000, Month.DECEMBER, 1));

        ArrayList<Book> lista = new ArrayList<>();
        for(Book b : model.getBooks())
            lista.add(b);

        File test = new File("test.xml");
        XMLFormat.write(test, lista);
        try {
            String content = new String(Files.readAllBytes(Paths.get(test.getPath())));

            assertTrue(content.contains("<book pageCount=\"500\">"));
            assertTrue(content.contains("<author>Meša Selimović</author>"));
            assertTrue(content.contains("<title>Harry Potter</title>"));
            assertTrue(content.contains("<publishDate>01. 12. 2000</publishDate>"));
        } catch(Exception e) {
            fail("Nije uspjelo čitanje datoteke");
        }
    }

    @Test
    public void testSave2 () {
        ArrayList<Book> list = new ArrayList<>();
        File test = new File("test.xml");
        XMLFormat.write(test, list);
        try {
            String content = new String(Files.readAllBytes(Paths.get(test.getPath())));
            String expected = "<library/>";

            assertTrue(content.contains(expected));
        } catch(Exception e) {
            fail("Nije uspjelo čitanje datoteke");
        }
    }

    @Test
    public void testOpen () {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        content += "<library>";
        content += "<book pageCount=\"1\"><author>A</author><title>B</title><isbn>C</isbn><publishDate>20. 12. 2018</publishDate></book>";
        content += "<book pageCount=\"2\"><author>X</author><title>Y</title><isbn>Z</isbn><publishDate>30. 01. 1910</publishDate></book>";
        content += "</library>";

        try {
            PrintWriter out = new PrintWriter("test.xml");
            out.print(content);
            out.close();
        } catch(Exception e) {
            // Ne bi se nikada trebalo desiti
        }

        File test = new File("test.xml");
        try {
            ArrayList<Book> list = XMLFormat.read(test);

            assertEquals(2, list.size());
            assertEquals(2, list.get(1).getPageCount());
            assertEquals("Y", list.get(1).getTitle());
            assertEquals("A", list.get(0).getAuthor());
            assertEquals("C", list.get(0).getIsbn());

        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOpen2 () {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        content += "<library>";
        content += "</library>";

        try {
            PrintWriter out = new PrintWriter("test.xml");
            out.print(content);
            out.close();
        } catch(Exception e) {
            // Ne bi se nikada trebalo desiti
        }

        File test = new File("test.xml");
        try {
            ArrayList<Book> list = XMLFormat.read(test);

            assertEquals(0, list.size());

        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOpenInvalid() {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        content += "<biblioteka>"; // Nepoznat tag biblioteka
        content += "<book pageCount=\"1\"><author>A</author><title>B</title><isbn>C</isbn><publishDate>20. 12. 2018</publishDate></book>";
        content += "<book pageCount=\"2\"><author>X</author><title>Y</title><isbn>Z</isbn><publishDate>30. 01. 1910</publishDate></book>";
        content += "</biblioteka>";

        try {
            PrintWriter out = new PrintWriter("test.xml");
            out.print(content);
            out.close();
        } catch(Exception e) {
            // Ne bi se nikada trebalo desiti
        }

        File test = new File("test.xml");
        assertThrows(Exception.class, () -> {ArrayList<Book> list = XMLFormat.read(test);});

        content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        content += "<library>"; // Nepoznat tag knjiga
        content += "<book pageCount=\"1\"><author>A</author><title>B</title><isbn>C</isbn><publishDate>20. 12. 2018</publishDate></book>";
        content += "<knjiga pageCount=\"2\"><author>X</author><title>Y</title><isbn>Z</isbn><publishDate>30. 01. 1910</publishDate></knjiga>";
        content += "</library>";

        try {
            PrintWriter out = new PrintWriter("test.xml");
            out.print(content);
            out.close();
        } catch(Exception e) {
            // Ne bi se nikada trebalo desiti
        }

        assertThrows(Exception.class, () -> {ArrayList<Book> list = XMLFormat.read(test);});

        content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        content += "<library>";
        content += "<book pageCount=\"1\"><author>A</author><title>B</title><isbn>C</isbn><publishDate>20. 12. 2018</publishDate></book>";
        content += "<book pageCount=\"2\"><author>X</author><isbn>Z</isbn><publishDate>30. 01. 1910</publishDate></book>"; // Nedostaje title
        content += "</library>";

        try {
            PrintWriter out = new PrintWriter("test.xml");
            out.print(content);
            out.close();
        } catch(Exception e) {
            // Ne bi se nikada trebalo desiti
        }

        assertThrows(Exception.class, () -> {ArrayList<Book> list = XMLFormat.read(test);});

        content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        content += "<library>";
        content += "<book pageCount=\"1\"><author>A</author><title>B</title><isbn>C</isbn><publishDate>20. 12. 2018</publishDate></book>";
        content += "<book pageCount=\"2\"><author>X</author><title>Y</title><isbn>Z</isbn><publishDate>1234</publishDate></book>";
        // Neispravan format datuma
        content += "</library>";

        try {
            PrintWriter out = new PrintWriter("test.xml");
            out.print(content);
            out.close();
        } catch(Exception e) {
            // Ne bi se nikada trebalo desiti
        }

        assertThrows(Exception.class, () -> {ArrayList<Book> list = XMLFormat.read(test);});
    }
}