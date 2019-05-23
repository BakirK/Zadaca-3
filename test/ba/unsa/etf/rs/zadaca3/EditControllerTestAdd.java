package ba.unsa.etf.rs.zadaca3;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

// Testovi za EditController operacija dodavanje nove knjige

@ExtendWith(ApplicationExtension.class)
class EditControllerTestAdd {
    Stage theStage;
    EditController controller;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editbook.fxml"));
        controller = new EditController(null);
        loader.setController(controller);
        Parent root = loader.load();
        stage.setTitle("Library");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
        stage.toFront();

        theStage = stage;
    }


    @Test
    public void testAddSpinner(FxRobot robot) {
        robot.clickOn("#fldAuthor");
        robot.write("Autor 1");
        robot.clickOn("#fldTitle");
        robot.write("Naslov 2");
        robot.clickOn("#fldIsbn");
        robot.write("ISBN 3");
        // Spinner
        robot.press(KeyCode.TAB).release(KeyCode.TAB); // Prelazimo sa ISBN polja na spinner
        robot.press(KeyCode.UP).release(KeyCode.UP); // Biramo 15 strelicom gore

        // Koju vrijednost ima spinner?
        Spinner kbs = robot.lookup("#spinPageCount").queryAs(Spinner.class);
        assertNotNull(kbs);
        Integer i = (Integer)kbs.getValueFactory().getValue();

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertFalse(theStage.isShowing());

        Book b = controller.getBook();
        assertEquals("Autor 1", b.getAuthor());
        assertEquals("Naslov 2", b.getTitle());
        assertEquals("ISBN 3", b.getIsbn());
        assertEquals(15, b.getPageCount());
        assertEquals(LocalDate.now(), b.getPublishDate());

        // Spinner treba imati vrijednost 15
        assertEquals(new Integer(15), i); //autoboxing ne radi?
    }

    @Test
    public void testAddDateFormat(FxRobot robot) {
        robot.clickOn("#fldAuthor");
        robot.write("Testni autor");
        robot.clickOn("#fldTitle");
        robot.write("Testni naslov");
        robot.clickOn("#fldIsbn");
        robot.write("1234");
        robot.press(KeyCode.TAB).release(KeyCode.TAB); // Prelazimo sa ISBN polja na spinner
        robot.press(KeyCode.TAB).release(KeyCode.TAB); // Prelazimo sa spinnera na datum

        // Selektujemo postojeću vrijednost kako bi ista bila obrisana
        robot.clickOn("#dpPublishDate");
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrl = KeyCode.COMMAND;
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);

        robot.write("13. 02. 1920");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        // Pritisak na tipku enter pokreće internu validaciju koju obavlja DatePicker kontrola
        // Ako datum nije prepoznat, neće se zadržati izmjena

        DatePicker datePicker = robot.lookup("#dpPublishDate").queryAs(DatePicker.class);
        assertNotNull(datePicker);
        String date = datePicker.getEditor().getText();

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertFalse(theStage.isShowing());

        // Da li je datum uspješno promijenjen?
        assertEquals("13. 02. 1920", date);

        // Da li je datum ispravan u knjizi?
        Book b = controller.getBook();
        assertEquals(LocalDate.of(1920, 2, 13), b.getPublishDate());
    }

    @Test
    public void testAddCancel(FxRobot robot) {
        // Da li Cancel dugme radi što treba?
        robot.clickOn("#fldAuthor");
        robot.write("Testni autor");
        robot.clickOn("#fldTitle");
        robot.write("Testni naslov");
        robot.clickOn("#fldIsbn");
        robot.write("1234");

        robot.clickOn("#btnCancel");
        // Da li je forma zatvorena?
        assertFalse(theStage.isShowing());

        // getBook treba vratiti null
        assertNull(controller.getBook());
    }

    @Test
    public void testAddValidateAuthor (FxRobot robot) {
        robot.clickOn("#fldAuthor");
        robot.write("abc");

        // Uzmi boju
        TextField autor = robot.lookup("#fldAuthor").queryAs(TextField.class);
        Background bg = autor.getBackground();
        Paint yellowgreen = Paint.valueOf("#adff2f");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;
        assertTrue(colorFound);

        // Autor je sada validan - Brišemo autora
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        // Uzmi sada boju
        bg = autor.getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;
        assertTrue(colorFound);

        // Sad ćemo ponovo nešto otkucati
        robot.write("a");
        bg = autor.getBackground();
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;
        assertTrue(colorFound);
    }

    @Test
    public void testAddValidateTitle(FxRobot robot) {
        robot.clickOn("#fldTitle");
        robot.write("abc");

        // Uzmi boju
        TextField naslov = robot.lookup("#fldTitle").queryAs(TextField.class);
        Background bg = naslov.getBackground();
        Paint yellowgreen = Paint.valueOf("#adff2f");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;
        assertTrue(colorFound);

        // Naslov je sada validan - Brišemo naslov
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        // Uzmi sada boju
        bg = naslov.getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;
        assertTrue(colorFound);

        // Sad ćemo ponovo nešto otkucati
        robot.write("a");
        bg = naslov.getBackground();
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;
        assertTrue(colorFound);
    }

    @Test
    public void testAddValidatePublishDate(FxRobot robot) {
        robot.clickOn("#fldAuthor");
        robot.write("Autor 1");
        robot.clickOn("#fldTitle");
        robot.write("Naslov 2");
        robot.clickOn("#fldIsbn");
        robot.write("ISBN 3");

        // Selektujemo postojeću vrijednost kako bi ista bila obrisana
        robot.clickOn("#dpPublishDate");
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrl = KeyCode.COMMAND;
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);

        // Datum u budućnosti
        robot.write("19. 10. 2019");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Uzmi boju
        DatePicker datePicker = robot.lookup("#dpPublishDate").queryAs(DatePicker.class);
        Background bg = datePicker.getEditor().getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;
        assertTrue(colorFound);

        // Probavamo Ok
        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertTrue(theStage.isShowing());
        assertNull(controller.getBook());

        // Datum u prošlosti
        robot.clickOn("#dpPublishDate");
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
        robot.write("19. 10. 2018");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Uzmi boju
        bg = datePicker.getEditor().getBackground();
        Paint yellowgreen = Paint.valueOf("#adff2f");
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;
        assertTrue(colorFound);

        // Sada Ok radi
        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertFalse(theStage.isShowing());
        assertNotNull(controller.getBook());

        Book b = controller.getBook();
        assertEquals(LocalDate.of(2018,10,19), b.getPublishDate());
    }


    @Test
    public void testAddInvalid (FxRobot robot) {
        // Nevalidna knjiga se ne može dodati
        robot.clickOn("#fldAuthor");
        robot.write("abc");
        robot.clickOn("#fldTitle");
        robot.write("def");
        robot.clickOn("#fldIsbn");
        robot.write("1234");

        // Brišemo autora
        robot.clickOn("#fldAuthor");

        // Naslov je sada validan - Brišemo naslov
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertTrue(theStage.isShowing());
        assertNull(controller.getBook());

        // Vraćamo autora, brišemo naslov
        robot.write("d");
        robot.clickOn("#fldTitle");
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertTrue(theStage.isShowing());
        assertNull(controller.getBook());

        // Vraćamo naslov
        robot.write("d");

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertFalse(theStage.isShowing());
        assertNotNull(controller.getBook());

        Book b = controller.getBook();
        assertEquals("d", b.getAuthor());
        assertEquals("d", b.getTitle());
    }

    /*

    @Test
    public void testChangeValidateNaslov (FxRobot robot) {
        robot.clickOn("#tblBooks");

        // Selektujemo Ivu Andrića
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        robot.clickOn("#tblBooks");
        robot.clickOn("#tbChange");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#fldTitle").tryQuery().isPresent();

        // Brišemo naslov
        robot.clickOn("#fldTitle");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);

        // Da li je boja lightpink
        TextField naslov = robot.lookup("#fldTitle").queryAs(TextField.class);
        Background bg = naslov.getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        assertTrue(colorFound);

        // Da li je knjiga izmijenjena? trebalo bi da nije
        String expected = "Ivo Andrić, Travnička hronika,";
        assertTrue(model.getBookList().contains(expected));

        // Nova statusna poruka
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book nije izmijenjena.", statusMsg);
    }

 */
}