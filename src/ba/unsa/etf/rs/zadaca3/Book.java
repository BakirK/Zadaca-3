package ba.unsa.etf.rs.zadaca3;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Book {
    private int id = 0;
    private SimpleStringProperty author = new SimpleStringProperty();
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty isbn = new SimpleStringProperty();
    private SimpleIntegerProperty pageCount  = new SimpleIntegerProperty();
    private SimpleObjectProperty<LocalDate> publishDate = new SimpleObjectProperty<>();

    private Book(int id, String author, String title, String isbn, int pageCount, LocalDate publishDate) {
        this.id = id;
        setAuthor(author);
        setTitle(title);
        setIsbn(isbn);
        setPageCount(pageCount);
        setPublishDate(publishDate);
    }


    private Book(String author, String title, String isbn, int pageCount, LocalDate publishDate) {
        setAuthor(author);
        setTitle(title);
        setIsbn(isbn);
        setPageCount(pageCount);
        setPublishDate(publishDate);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    private void setAuthor(String author) {
        this.author.set(author);
    }

    private String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    private void setTitle(String title) {
        this.title.set(title);
    }

    private String getIsbn() {
        return isbn.get();
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    private void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    private int getPageCount() {
        return pageCount.get();
    }

    public SimpleIntegerProperty pageCountProperty() {
        return pageCount;
    }

    private void setPageCount(int pageCount) {
        this.pageCount.set(pageCount);
    }

    private LocalDate getPublishDate() {
        return publishDate.get();
    }

    public SimpleObjectProperty<LocalDate> publishDateProperty() {
        return publishDate;
    }

    private void setPublishDate(LocalDate publishDate) {
        this.publishDate.set(publishDate);
    }

    @Override
    public String toString(){
        return author.get() + ", " + title.get() + ", " + isbn.get() + ", " + pageCount.get() + ", "
                + publishDate.get().format(dateFormat);
    }

    public  static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd. MM. yyyy");




}
