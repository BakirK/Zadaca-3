package ba.unsa.etf.rs.zadaca3;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;

public class LibraryDAO {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private SimpleObjectProperty<Book> currentBook  = null;
    private static LibraryDAO instance = null;
    private Connection conn;
    private PreparedStatement getBooksStatement, addBookStatement, deleteCurrentBookStatement, updateBook,
            deleteAllBooksStatement, getMaxBookId;


    public ObservableList<Book> getBooks() {
        return books;
    }

    public void setBooks(ObservableList<Book> books) {
        this.books = books;
    }

    public Book getCurrentBook() {
        return currentBook.get();
    }

    public SimpleObjectProperty<Book> currentBookProperty() {
        return currentBook;
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = new SimpleObjectProperty<>(currentBook);
    }

    public static LibraryDAO getInstance() {
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

    public LibraryDAO() {
        prepareStatements();
        loadBooks();
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
            addBookStatement = conn.prepareStatement("INSERT INTO BOOKS(id, author, title, isbn, pagecount, publishdate)" +
                    " VALUES (?,?,?,?,?,?); COMMIT;");
            deleteCurrentBookStatement = conn.prepareStatement("DELETE FROM books WHERE id=?; COMMIT;");
            updateBook = conn.prepareStatement("UPDATE books SET author = ?, title = ?, isbn = ?, pagecount = ?," +
                    "publishdate = ? WHERE id = ?; COMMIT");

            deleteAllBooksStatement = conn.prepareStatement("DELETE FROM books WHERE 1 = 1; COMMIT;");

            getBooksStatement = conn.prepareStatement("SELECT id, author, title, isbn, pagecount, publishdate FROM books;");
            getMaxBookId = conn.prepareStatement("SELECT MAX(id) + 1 FROM books;");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nije pronadjen driver za konekciju");
            e.printStackTrace();
        }
    }


    public void addBook(Book book) {
        try {
            ResultSet resultSet = getMaxBookId.executeQuery();
            resultSet.next();
            addBookStatement.setInt(1, resultSet.getInt(1));
            addBookStatement.setString(2, book.authorProperty().get());
            addBookStatement.setString(3, book.titleProperty().get());
            addBookStatement.setString(4, book.isbnProperty().get());
            addBookStatement.setInt(5, book.pageCountProperty().get());
            addBookStatement.setDate(6, Date.valueOf(book.publishDateProperty().get()));
            addBookStatement.executeUpdate();
            books.add(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void deleteBook() {
        try {
            deleteCurrentBookStatement.setInt(1, currentBook.get().getId());
            deleteCurrentBookStatement.executeUpdate();
            books.remove(currentBook.get());
            currentBook = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateCurrentBook(Book book) {
        try {
            updateBook.setString(1, book.authorProperty().get());
            updateBook.setString(2, book.titleProperty().get());
            updateBook.setString(3, book.authorProperty().get());
            updateBook.setString(4, book.isbnProperty().get());
            updateBook.setDate(5, Date.valueOf(book.publishDateProperty().get()));
            updateBook.setInt(6, book.getId());
            updateBook.executeUpdate();
            int index = books.indexOf(currentBook);
            books.set(index, book);
            currentBook.set(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearAll() {
        try {
            deleteAllBooksStatement.executeUpdate();
            books.clear();
            currentBook = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void defaultData() {
        addBook(new Book("Meša Selimović", "Tvrđava", "abcd", 500, LocalDate.now()));
        addBook(new Book("Ivo Andrić", "Travnička hronika", "abcd", 500, LocalDate.now()));
        addBook(new Book("J. K. Rowling", "Harry Potter", "abcd", 500, LocalDate.now()));
    }

    public void loadBooks() {
        try {
            ResultSet set = getBooksStatement.executeQuery();
            while(set.next()) {
                books.add(new Book(set.getInt(1), set.getString(2),
                        set.getString(3), set.getString(4),
                        set.getInt(5), set.getDate(6).toLocalDate()));
            }
            if (books.size() > 0) {
                currentBook = new SimpleObjectProperty<>(books.get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Book> getBookList() {
        return books;
    }
}
