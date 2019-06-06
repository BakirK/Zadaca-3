package ba.unsa.etf.rs.zadaca3;

import java.beans.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class XMLFormat {
    public static ArrayList<Book> read(File file) throws Exception {
        if (!"xml".equals(getFileExtension(file))) {
            throw new Exception();
        }
        ArrayList<Book> books = new ArrayList<>();
        Book book = null;
        XMLDecoder input = null;
        try {
            input = new XMLDecoder(new FileInputStream(file.getPath()));
            while((book = (Book) input.readObject()) != null) {
                books.add(book);
            }
            input.close();
        } catch(IndexOutOfBoundsException e) {
            return books;
        } catch(Exception e) {
            input.close();
            System.out.println("Greška: "+e);
        }
        return books;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    public static void write(File file, ArrayList<Book> books){
        XMLEncoder output = null;
        try {
            output = new XMLEncoder(new FileOutputStream(file.getPath()));
            output.setPersistenceDelegate(LocalDate.class,
                new PersistenceDelegate() {
                    @Override
                    protected Expression instantiate(Object localDate, Encoder encdr) {
                        return new Expression(localDate,
                                LocalDate.class,
                                "parse",
                                new Object[]{localDate.toString()});
                    }
                });
            for (Book book: books) {
                output.writeObject(book);
            }
            output.close();
        } catch(FileNotFoundException e) {
            output.close();
            System.out.println("Greška: " + e);
        }
    }

}
