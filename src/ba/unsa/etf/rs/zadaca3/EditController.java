package ba.unsa.etf.rs.zadaca3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditController {
    @FXML
    private Button btnCancel, btnOk;
    @FXML
    private DatePicker dpPublishDate;
    @FXML
    private Spinner spinPageCount;
    @FXML
    private TextField fldIsbn, fldTitle, fldAuthor;
    private boolean isbnCorrectInput = false, dateCorrectInput = false, titleCorrectInput = false,
            authorCorrectInput = false, validated = false;
    private Book book = null;
    public Book getBook() {
        if (validated) {
            validated = false;
            return book;
        } else {
            return null;
        }
    }

    public EditController(Book book){
        this.book = book;
    }


    @FXML
    public void initialize() {
        if (book == null) {
            fldIsbn.setText("");
            fldIsbn.getStyleClass().add("fieldIncorrect");
            fldTitle.setText("");
            fldTitle.getStyleClass().add("fieldIncorrect");
            fldAuthor.setText("");
            fldAuthor.getStyleClass().add("fieldIncorrect");
            dpPublishDate.getEditor().setText("");
            dpPublishDate.getEditor().getStyleClass().add("fieldIncorrect");

        } else {
            fldIsbn.setText(book.getIsbn());
            fldIsbn.getStyleClass().add("fieldCorrect");
            isbnCorrectInput = true;

            fldTitle.setText(book.getTitle());
            fldTitle.getStyleClass().add("fieldCorrect");
            titleCorrectInput = true;

            fldAuthor.setText(book.getAuthor());
            fldAuthor.getStyleClass().add("fieldCorrect");
            authorCorrectInput = true;

            dpPublishDate.setValue(book.getPublishDate());
            dpPublishDate.getEditor().getStyleClass().add("fieldCorrect");
            dateCorrectInput = true;
        }

        SpinnerValueFactory<Integer> spinPageCountValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 10);
        this.spinPageCount.setValueFactory(spinPageCountValueFactory);


        fldTitle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.trim().isEmpty()) {
                    fldTitle.getStyleClass().removeAll("fieldCorrect");
                    fldTitle.getStyleClass().add("fieldIncorrect");
                    titleCorrectInput = false;
                } else {
                    fldTitle.getStyleClass().removeAll("fieldIncorrect");
                    fldTitle.getStyleClass().add("fieldCorrect");
                    titleCorrectInput = true;
                }
            }
        });


        fldAuthor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.trim().isEmpty()) {
                    fldAuthor.getStyleClass().removeAll("fieldCorrect");
                    fldAuthor.getStyleClass().add("fieldIncorrect");
                    authorCorrectInput = false;
                } else {
                    fldAuthor.getStyleClass().removeAll("fieldIncorrect");
                    fldAuthor.getStyleClass().add("fieldCorrect");
                    authorCorrectInput = true;
                }
            }
        });

        fldIsbn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.trim().isEmpty()) {
                    fldIsbn.getStyleClass().removeAll("fieldCorrect");
                    fldIsbn.getStyleClass().add("fieldIncorrect");
                    isbnCorrectInput = false;
                } else {
                    fldIsbn.getStyleClass().removeAll("fieldIncorrect");
                    fldIsbn.getStyleClass().add("fieldCorrect");
                    isbnCorrectInput = true;
                }
            }
        });

        dpPublishDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                if (t1.isAfter(LocalDate.now()) || t1 == null) {
                    dpPublishDate.getEditor().getStyleClass().removeAll("fieldCorrect");
                    dpPublishDate.getEditor().getStyleClass().add("fieldIncorrect");
                    dateCorrectInput = false;
                } else {
                    dpPublishDate.getEditor().getStyleClass().removeAll("fieldIncorrect");
                    dpPublishDate.getEditor().getStyleClass().add("fieldCorrect");
                    dateCorrectInput = true;
                }
            }
        });
    }

    private boolean validateText() {
        return dateCorrectInput && authorCorrectInput && titleCorrectInput && isbnCorrectInput;
    }

    @FXML
    private void zatvoriProzorPropuhJe(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void validateInput(ActionEvent actionEvent) {
        if(validateText()) {
            book.setTitle(fldTitle.getText());
            book.setAuthor(fldAuthor.getText());
            book.setIsbn(fldIsbn.getText());
            book.setPageCount((Integer) spinPageCount.getValue());
            book.setPublishDate(dpPublishDate.getValue());
            validated = true;
            System.out.println("validated");
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }


}
