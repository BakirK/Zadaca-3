package ba.unsa.etf.rs.zadaca3;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MainController {
    @FXML
    private TableView tblBooks;
    @FXML
    private TableColumn colAuthor, colTitle, colPublishDate;
    @FXML
    private Button tbDelete, tbChange, tbAdd;
    private LibraryDAO model;
    Stage editBookWindow = new Stage();
    Stage aboutWindow = new Stage();
    @FXML
    private Label statusMsg;

    public MainController(LibraryDAO model) {
        this.model = model;
    }


    @FXML
    public void initialize() {
        statusMsg.setText("Program started.");
        colAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        colPublishDate.setCellValueFactory(new PropertyValueFactory<LocalDate, String>("publishDate"));
        tblBooks.setItems(model.getBooks());


        tblBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book oldBook, Book newBook) {
                if (oldBook != null) {
                }
                if (newBook == null) {

                } else {
                    updateSelectedBook();
                }
                tblBooks.refresh();
            }
        });

        /*tblBooks.requestFocus();
        tblBooks.getSelectionModel().selectFirst();*/
    }

    @FXML
    private void updateSelectedBook() {
        if(model.getCurrentBook() == null) {
            System.out.println("NULL book");
        }
        Book b = (Book) tblBooks.getSelectionModel().getSelectedItem();
        model.setCurrentBook(b);
    }

    @FXML
    private void editBook(ActionEvent actionEvent) {
        try {
            if(!editBookWindow.isShowing() && model.getCurrentBook() != null) {
                statusMsg.setText("Editing book.");
                openNewWindow(model.getCurrentBook());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteBook(ActionEvent actionEvent) {
        statusMsg.setText("Deleting book.");
        if ( model.getCurrentBook() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected book?", ButtonType.OK, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                model.deleteBook();
                updateTableView();
                tblBooks.getSelectionModel().selectFirst();
                statusMsg.setText("Book deleted.");
            } else {
                statusMsg.setText("Book not deleted.");
            }
        }

    }

    @FXML
    private void addBook(ActionEvent actionEvent) {
        statusMsg.setText("Adding new book.");
        if(!editBookWindow.isShowing()) {
            openNewWindow(null);
        }
    }

    @FXML
    private void checkDoubleClick(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() >= 2){
                //System.out.println("Double clicked" + mouseEvent.getClickCount());
                try {
                    if(!editBookWindow.isShowing() && model.getCurrentBook() != null) {
                        openNewWindow(model.getCurrentBook());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openAboutWindow(ActionEvent actionEvent) {
        if(!aboutWindow.isShowing()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/aboutWindow.fxml"));
            AboutWindowController aboutWindowController = new AboutWindowController();
            loader.setController(aboutWindowController);
            Parent root = null;
            try {
                root = loader.load();
                aboutWindow.setTitle("About");
                aboutWindow.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                aboutWindow.setResizable(false);
                aboutWindow.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openNewWindow(Book book) {
        if(!editBookWindow.isShowing()) {
            boolean adding = false;
            if (book == null) {
                adding = true;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editbook.fxml"));
            EditController editController = new EditController(book);
            loader.setController(editController);
            Parent root = null;
            try {
                root = loader.load();
                if (book == null) {
                    editBookWindow.setTitle("Add new book");
                } else {
                    editBookWindow.setTitle("Edit current book");
                }
                editBookWindow.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                editBookWindow.setResizable(false);
                editBookWindow.show();

                boolean finalAdding = adding;
                editBookWindow.setOnHiding(Event -> {
                    Book newBook = editController.getBook();
                    if (newBook != null) {
                        if (finalAdding) {
                            model.addBook(newBook);
                            updateTableView();
                            statusMsg.setText("Book added.");
                            tblBooks.getSelectionModel().selectLast();
                        } else {
                            model.updateCurrentBook(book);
                            statusMsg.setText("Book edited.");
                            updateTableView();
                        }
                    } else {
                        if (finalAdding) {
                            statusMsg.setText("Book not added.");
                        } else {
                            statusMsg.setText("Book not edited.");
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTableView() {
        int index = tblBooks.getSelectionModel().getSelectedIndex();
        tblBooks.getItems().clear();
        model.loadBooks();
        tblBooks.setItems(model.getBooks());
        tblBooks.requestFocus();
        tblBooks.getSelectionModel().select(index);
    }

    @FXML
    private void menuOpen(ActionEvent actionEvent) {
        File file = new File("resources/Data.xml");
        try {
            tblBooks.setItems(FXCollections.observableArrayList(XMLFormat.read(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblBooks.requestFocus();
    }

    @FXML
    private void menuSave(ActionEvent actionEvent) {
        File file = new File("resources/Data.xml");
        XMLFormat.write(file, new ArrayList<Book>(model.getBooks()));
    }

    @FXML
    private void menuPrint(ActionEvent actionEvent) {
        statusMsg.setText("Printing books to standard output.");
        System.out.println("Books are:\n");
        System.out.println(model.getBookList());
    }

    @FXML
    private void computerEndProgram(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
