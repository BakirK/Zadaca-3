package ba.unsa.etf.rs.zadaca3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

    @FXML
    private void checkDoubleClick(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() >= 2){
                //System.out.println("Double clicked" + mouseEvent.getClickCount());
                try {
                    otvoriNovi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void otvoriNovi() throws Exception {
        Stage myStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/editbook.fxml"));
        myStage.setTitle("Edit selected book");
        myStage.setScene(new Scene(root, 300, 275));
        myStage.show();
    }

}
