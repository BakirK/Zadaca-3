package ba.unsa.etf.rs.zadaca3;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
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

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

// Testovi za EditController operacija izmjena knjige

@ExtendWith(ApplicationExtension.class)
class EditControllerTestEdit {
    Stage theStage;
    EditController controller;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editbook.fxml"));
        Book b = new Book("Testni autor", "Testni naslov", "1234", 100, LocalDate.now());
        controller = new EditController(b);
        loader.setController(controller);
        Parent root = loader.load();
        stage.setTitle("Library");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
        stage.toFront();

        theStage = stage;
    }

    @Test
    public void testEditSpinner(FxRobot robot) {
        robot.clickOn("#fldIsbn");
        // Spinner
        robot.press(KeyCode.TAB).release(KeyCode.TAB); // Prelazimo sa ISBN polja na spinner
        robot.press(KeyCode.UP).release(KeyCode.UP); // Biramo 105 strelicom gore

        // Koju vrijednost ima spinner?
        Spinner kbs = robot.lookup("#spinPageCount").queryAs(Spinner.class);
        assertNotNull(kbs);
        Integer i = (Integer)kbs.getValueFactory().getValue();

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertFalse(theStage.isShowing());

        Book b = controller.getBook();
        assertEquals("Testni autor", b.getAuthor());
        assertEquals("Testni naslov", b.getTitle());
        assertEquals("1234", b.getIsbn());
        assertEquals(105, b.getPageCount());
        assertEquals(LocalDate.now(), b.getPublishDate());

        // Spinner treba imati vrijednost 105
        assertEquals(new Integer(105), i); //autoboxing ne radi?
    }

    @Test
    public void testEditDateFormat(FxRobot robot) {
        robot.clickOn("#fldIsbn");
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
    public void testEditCancel(FxRobot robot) {
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
    public void testEditValidateAuthor (FxRobot robot) {
        // Selektujemo postojeću vrijednost kako bi ista bila obrisana
        robot.clickOn("#fldAuthor");
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrl = KeyCode.COMMAND;
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
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
    public void testEditValidateTitle(FxRobot robot) {
        // Selektujemo postojeću vrijednost kako bi ista bila obrisana
        robot.clickOn("#fldTitle");
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrl = KeyCode.COMMAND;
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
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
    public void testEditValidatePublishDate(FxRobot robot) {
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
    public void testEditInvalid (FxRobot robot) {
        // Brišemo autora
        robot.clickOn("#fldAuthor");
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").equals("Mac OS X"))
            ctrl = KeyCode.COMMAND;
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        robot.clickOn("#btnOk");
        // Da li je forma zatvorena?
        assertTrue(theStage.isShowing());
        assertNull(controller.getBook());

        // Vraćamo autora, brišemo naslov
        robot.write("d");
        robot.clickOn("#fldTitle");
        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
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

}