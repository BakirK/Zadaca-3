package ba.unsa.etf.rs.zadaca3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class MainController {
    @FXML
    private TableView tblBooks;
    @FXML
    private TableColumn colAuthor, colTitle, colPublishDate;
    @FXML
    private Button tbDelete, tbChange, tbAdd;
    private LibraryDAO model;


    public MainController(LibraryDAO model) {
        this.model = model;
    }


    @FXML
    public void initialize() {
        colAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        colPublishDate.setCellValueFactory(new PropertyValueFactory<LocalDate, String>("publishDate"));
        tblBooks.setItems(model.getBooks());


        tblBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book oldBook, Book newBook) {
                if (oldBook != null) {
                    setTextPropetryUnbind();
                }
                if (newBook == null) {

                } else {
                    updateSelectedBook();
                }
                tblBooks.refresh();
            }
        });

        tblBooks.requestFocus();
        tblBooks.getSelectionModel().selectFirst();
    }


    //FIXME
    private void setTextPropetryUnbind() {

    }

    //FIXME
    private void setTextPropetryBind() {
    }

    @FXML
    private void updateSelectedBook() {
        if(model.getCurrentBook() == null) {
            System.out.println("NULL book");
        }
        Book b = (Book) tblBooks.getSelectionModel().getSelectedItem();
        setTextPropetryUnbind();
        model.setCurrentBook(b);
        setTextPropetryBind();
    }

    @FXML
    private void changeBook(ActionEvent actionEvent) {
    }

    @FXML
    private void deleteBook(ActionEvent actionEvent) {
    }

    @FXML
    private void addBook(ActionEvent actionEvent) {

    }
}
