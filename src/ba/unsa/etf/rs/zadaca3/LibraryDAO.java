package ba.unsa.etf.rs.zadaca3;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LibraryDAO {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private SimpleObjectProperty<Book> currentBook  = null;
    private static LibraryDAO instance = null;
    private Connection conn;
    private PreparedStatement getBooksStatement, addBookStatement;


    private ObservableList<Book> getBooks() {
        return books;
    }

    private void setBooks(ObservableList<Book> books) {
        this.books = books;
    }

    private Book getCurrentBook() {
        return currentBook.get();
    }

    public SimpleObjectProperty<Book> currentBookProperty() {
        return currentBook;
    }

    private void setCurrentBook(Book currentBook) {
        this.currentBook.set(currentBook);
    }

    private static LibraryDAO getInstance() {
        if (instance == null) {
            initialize();
        }
        return instance;
    }

    public static void deleteInstance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }


    private static void initialize() {
        instance = new LibraryDAO();
    }

    //TODO: metoda za punjenje liste i prepared statements
    public LibraryDAO() {
        prepareStatements();
    }


    public void printBooks() {
        for(Book b: books) {
            System.out.println(b.toString());
        }
    }


    private void prepareStatements() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:library.db");
            Class.forName("org.sqlite.JDBC");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nije pronadjen driver za konekciju");
            e.printStackTrace();
        }
    }


    public void addBook(Book b) {

    }






}
