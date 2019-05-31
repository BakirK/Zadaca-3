package ba.unsa.etf.rs.zadaca3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class MainController {
    @FXML
    private TableView tblBooks;
    @FXML
    private TableColumn colAuthor, colTitle, colPublishDate;
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
                    updateSelectedDriver();
                }
                tblBooks.refresh();
            }


        });

        tblBooks.requestFocus();
        tblBooks.getSelectionModel().selectFirst();



    }

    private void setTextPropetryUnbind() {
    }

    private void setTextPropetryBind() {
    }

    private void updateSelectedDriver() {
        if(model.getCurrentBook() == null) {
            System.out.println("NULL book");
        }
        Book b = (Book) tblBooks.getSelectionModel().getSelectedItem();
        setTextPropetryUnbind();
        model.setCurrentBook(b);
        setTextPropetryBind();
    }



}
