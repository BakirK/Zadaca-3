package ba.unsa.etf.rs.zadaca3;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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


@ExtendWith(ApplicationExtension.class)
class MainControllerTest {
    Stage theStage;
    LibraryDAO model;
    MainController controller;

    @Start
    public void start (Stage stage) throws Exception {
        model = LibraryDAO.getInstance();
        model.clearAll();
        model.defaultData();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainform.fxml"));
        controller = new MainController(model);
        loader.setController(controller);
        Parent root = loader.load();
        stage.setTitle("Library");
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
        stage.toFront();

        theStage = stage;
    }

    @Test
    public void testStatusMsg (FxRobot robot) {
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertNotNull(statusMsg);
        assertEquals("Program started.", statusMsg);
    }

    @Test
    public void testTableViewColumns (FxRobot robot) {
        TableView tableView = robot.lookup("#tblBooks").queryAs(TableView.class);
        assertNotNull(tableView);
        ObservableList<TableColumn> columns = tableView.getColumns();
        assertEquals("Title", columns.get(1).getText());
        assertEquals("Author", columns.get(0).getText());
        assertEquals("Publish date", columns.get(2).getText());
    }

    @Test
    public void testCancelDelete (FxRobot robot) {
        // Edit > Delete
        robot.clickOn("#tbDelete");
        // Neće se ništa desiti jer nijedna knjiga nije selektovana

        robot.clickOn("#tblBooks");

        // Selektujemo Ivu Andrića
        robot.clickOn("Ivo Andrić");

        robot.clickOn("#tbDelete");

        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Cancel
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        robot.clickOn(cancelButton);

        // Da li je knjiga obrisana?
        String expected = "Ivo Andrić";
        assertTrue(model.getBookList().contains(expected));

        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book not deleted.", statusMsg);
    }

    @Test
    public void testDelete (FxRobot robot) {
        // Edit > Delete
        robot.clickOn("#tbDelete");
        // Neće se ništa desiti jer nijedna knjiga nije selektovana

        robot.clickOn("#tblBooks");

        // Selektujemo Ivu Andrića
        robot.clickOn("Ivo Andrić");

        robot.clickOn("#tbDelete");

        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();
        //DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);

        // Zatvaramo dijalog
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        // Da li je knjiga obrisana?
        String expected = "Ivo Andrić";
        assertFalse(model.getBookList().contains(expected));

        // Druga vrsta provjere
        boolean test = false;
        for(Book b : model.getBooks())
            if (b.getAuthor().equals("Ivo Andrić")) test = true;
        assertFalse(test);

        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book deleted.", statusMsg);

        // Vraćamo bazu u polazno stanje
        model.clearAll();
        model.defaultData();
    }

    @Test
    public void testAdd (FxRobot robot) {
        robot.clickOn("#tbAdd");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#fldAuthor").tryQuery().isPresent();

        robot.clickOn("#fldAuthor");
        robot.write("Testni autor");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("Testni naslov");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("1234");

        // Zatvaramo prozor
        robot.clickOn("#btnOk");

        // Da li je knjiga dodana?
        String expected = "Testni autor, Testni naslov";
        System.out.println(model.getBookList());

        assertTrue(model.getBookList().contains(expected));

        // Druga vrsta provjere
        boolean test = false;
        for(Book b : model.getBooks())
            if (b.getAuthor().equals("Testni autor") && b.getTitle().equals("Testni naslov")) test = true;
        assertTrue(test);

        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book added.", statusMsg);

        // Vraćamo bazu u polazno stanje
        model.clearAll();
        model.defaultData();
    }

    @Test
    public void testCancelAdd (FxRobot robot) {
        robot.clickOn("#tbAdd");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#fldAuthor").tryQuery().isPresent();

        robot.clickOn("#fldAuthor");
        robot.write("Testni autor");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("Testni naslov");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("1234");

        // Zatvaramo prozor
        robot.clickOn("#btnCancel");

        // Da li je knjiga dodana?
        String expected = "Testni autor, Testni naslov";
        System.out.println(model.getBookList());

        assertFalse(model.getBookList().contains(expected));

        // Druga vrsta provjere
        boolean test = false;
        for(Book b : model.getBooks())
            if (b.getAuthor().equals("Testni autor") && b.getTitle().equals("Testni naslov")) test = true;
        assertFalse(test);

        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book not added.", statusMsg);
    }

    @Test
    public void testChange (FxRobot robot) {
        robot.clickOn("#tblBooks");

        // Selektujemo Ivu Andrića
        robot.clickOn("Ivo Andrić");
        robot.clickOn("#tbChange");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#fldAuthor").tryQuery().isPresent();

        // Statusna poruka u ovom trenutku mora biti "Mijenjam knjigu."
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();

        // Travnička hronikaaa
        robot.clickOn("#fldTitle");
        robot.press(KeyCode.END).release(KeyCode.END);
        robot.write("aaa");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Zatvaramo prozor
        robot.clickOn("#btnOk");

        // Da li je stara statusna poruka bila dobra?
        assertEquals("Editing book.", statusMsg);

        // Da li je knjiga izmijenjena?
        String expected = "Travnička hronikaaaa,";
        assertTrue(model.getBookList().contains(expected));

        // Nova statusna poruka
        statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book edited.", statusMsg);
    }


    @Test
    public void testCancelChange (FxRobot robot) {
        robot.clickOn("#tblBooks");

        // Selektujemo Ivu Andrića
        robot.clickOn("Ivo Andrić");
        robot.clickOn("#tbChange");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#fldAuthor").tryQuery().isPresent();

        // Statusna poruka u ovom trenutku mora biti "Mijenjam knjigu."
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();

        // Travnička hronikaaa
        robot.clickOn("#fldTitle");
        robot.press(KeyCode.END).release(KeyCode.END);
        robot.write("aaa");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Zatvaramo prozor
        robot.clickOn("#btnCancel");

        // Da li je stara statusna poruka bila dobra?
        assertEquals("Editing book.", statusMsg);

        // Da li je knjiga izmijenjena?
        String expected = "Travnička hronikaaaa,";
        assertFalse(model.getBookList().contains(expected));
        expected = "Travnička hronika,";
        assertTrue(model.getBookList().contains(expected));

        // Nova statusna poruka
        statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Book not edited.", statusMsg);
    }

    @Test
    public void testAbout (FxRobot robot) {
        robot.clickOn("#tblBooks");

        // Help > About (Alt-H-A)
        robot.press(KeyCode.ALT).press(KeyCode.H).release(KeyCode.H).press(KeyCode.A).release(KeyCode.A).release(KeyCode.ALT);


        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Da li sadrži sliku
        ImageView imgAbout = robot.lookup("#imgAbout").queryAs(ImageView.class);
        assertNotNull(imgAbout);
        // Zatvori dijalog
        Platform.runLater( () -> {
            Stage stage = (Stage) imgAbout.getScene().getWindow();
            stage.close();
        });
    }
/*
    @Test
    public void testQuit (FxRobot robot) {
        robot.clickOn("#tblBooks");
        // File -> Exit (ALT-F-E)
        robot.press(KeyCode.ALT).press(KeyCode.F).release(KeyCode.F).press(KeyCode.E).release(KeyCode.E).release(KeyCode.ALT);
        assertFalse(theStage.isShowing());
    }*/
}