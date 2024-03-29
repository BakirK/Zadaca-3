package ba.unsa.etf.rs.zadaca3;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private SimpleStringProperty author = new SimpleStringProperty();
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty isbn = new SimpleStringProperty();
    private SimpleIntegerProperty pageCount  = new SimpleIntegerProperty();
    private SimpleObjectProperty<LocalDate> publishDate = new SimpleObjectProperty<>();

    public Book(int id, String author, String title, String isbn, int pageCount, LocalDate publishDate) {
        this.id = id;
        setAuthor(author);
        setTitle(title);
        setIsbn(isbn);
        setPageCount(pageCount);
        setPublishDate(publishDate);
    }


    public Book(String author, String title, String isbn, int pageCount, LocalDate publishDate) {
        setAuthor(author);
        setTitle(title);
        setIsbn(isbn);
        setPageCount(pageCount);
        setPublishDate(publishDate);
    }

    public Book() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    protected String getIsbn() {
        return isbn.get();
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public int getPageCount() {
        return pageCount.get();
    }

    public SimpleIntegerProperty pageCountProperty() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount.set(pageCount);
    }

    public LocalDate getPublishDate() {
        return publishDate.get();
    }

    public SimpleObjectProperty<LocalDate> publishDateProperty() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate.set(publishDate);
    }

    @Override
    public String toString(){
        return author.get() + ", " + title.get() + ", " + isbn.get() + ", " + pageCount.get() + ", "
                + publishDate.get().format(dateFormat);
    }

    public  static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd. MM. yyyy");

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Book)) {
            if (o instanceof String) {
                String s1 = this.getAuthor() + ", " + this.getTitle();
                return s1.equals((String) o);
            }
            return false;
        }
        Book other = (Book) o;
        return (this.id == other.id) ;
    }

    @Override
    public int hashCode() {
        return 7 + 5*id; // 5 and 7 are random prime numbers
    }
}
