package ba.unsa.etf.rs.zadaca3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class XMLFormat {
    private static ArrayList<String> author = new ArrayList<>();
    private static ArrayList<String> isbn = new ArrayList<>();
    private static ArrayList<String> title = new ArrayList<>();
    private static ArrayList<Integer> pageCount = new ArrayList<>();
    private static ArrayList<LocalDate> publishDate = new ArrayList<>();

    public static void readElements(Element element) {
        //System.out.print("Element "+element.getTagName()+", ");
        //int brAtributa = element.getAttributes().getLength();
        if (element.getTagName().equals("book")) {
            if (!element.getAttribute("pageCount").isEmpty()) {
                pageCount.add(Integer.parseInt(element.getAttribute("pageCount").replace("[","").replace("]","")));
                System.out.println("page:" + pageCount);
            }
        } else if (element.getTagName().equals("author")) {
            NodeList djecaTemp = element.getChildNodes();

            if (djecaTemp.getLength() == 1) {
                author.add(element.getTextContent().replace("[","").replace("]",""));
                System.out.println("author:" + author);
            }

        } else if (element.getTagName().equals("title")) {
            NodeList djecaTemp = element.getChildNodes();

            if (djecaTemp.getLength() == 1) {
                title.add(element.getTextContent().replace("[","").replace("]",""));
                System.out.println("title:" + title);
            }

        } else if (element.getTagName().equals("isbn")) {
            NodeList djecaTemp = element.getChildNodes();

            if (djecaTemp.getLength() == 1) {
                isbn.add(element.getTextContent().replace("[","").replace("]",""));
                System.out.println("isbn:" + isbn);
            }
        } else if (element.getTagName().equals("publishDate")) {
            NodeList djecaTemp = element.getChildNodes();
            if (djecaTemp.getLength() == 1) {
                publishDate.add(LocalDate.parse(element.getTextContent().replace("[","").replace("]",""), Book.dateFormat));
                System.out.println("publishDate:" + publishDate.toString());
            }
        }
        NodeList djeca = element.getChildNodes();

        for(int i = 0; i < djeca.getLength(); i++) {
            Node dijete = djeca.item(i);
            if (dijete instanceof Element) {
                readElements((Element)dijete);
            }
        }
    }

    public static ArrayList<Book> read(File file) throws Exception {
        if(!getFileExtension(file).equals("xml")){
            throw new FileNotFoundException ();
        }
        ArrayList<Book> books = new ArrayList<>();
        Document xmldoc = null;
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmldoc = docReader.parse(file);
            Element docEle = xmldoc.getDocumentElement();
            NodeList djeca = docEle.getChildNodes();

            for(int i = 0; i < djeca.getLength(); i++) {
                Node dijete = djeca.item(i);
                if (dijete instanceof Element) {
                    readElements((Element) dijete);
                }
            }

            for (int i = 0; i < title.size(); i++) {
                books.add(new Book(author.get(i), title.get(i), isbn.get(i), pageCount.get(i), publishDate.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
