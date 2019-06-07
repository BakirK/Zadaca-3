package ba.unsa.etf.rs.zadaca3;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;

public class XMLFormat {
    private static ArrayList<String> author = new ArrayList<>();
    private static ArrayList<String> isbn = new ArrayList<>();
    private static ArrayList<String> title = new ArrayList<>();
    private static ArrayList<Integer> pageCount = new ArrayList<>();
    private static ArrayList<LocalDate> publishDate = new ArrayList<>();

    public static void readElements(Element element) throws Exception {
        //System.out.print("Element "+element.getTagName()+", ");
        //int brAtributa = element.getAttributes().getLength();
        if (element.getTagName().equals("book")) {
            if (!element.getAttribute("pageCount").isEmpty()) {
                pageCount.add(Integer.parseInt(element.getAttribute("pageCount").replace("[","").replace("]","")));
                //System.out.println("page:" + pageCount);
            }
        } else if (element.getTagName().equals("author")) {
            NodeList djecaTemp = element.getChildNodes();

            if (djecaTemp.getLength() == 1) {
                author.add(element.getTextContent().replace("[","").replace("]",""));
                //System.out.println("author:" + author);
            }

        } else if (element.getTagName().equals("title")) {
            NodeList djecaTemp = element.getChildNodes();

            if (djecaTemp.getLength() == 1) {
                title.add(element.getTextContent().replace("[","").replace("]",""));
                //System.out.println("title:" + title);
            }

        } else if (element.getTagName().equals("isbn")) {
            NodeList djecaTemp = element.getChildNodes();

            if (djecaTemp.getLength() == 1) {
                isbn.add(element.getTextContent().replace("[","").replace("]",""));
                //System.out.println("isbn:" + isbn);
            }
        } else if (element.getTagName().equals("publishDate")) {
            NodeList djecaTemp = element.getChildNodes();
            if (djecaTemp.getLength() == 1) {
                publishDate.add(LocalDate.parse(element.getTextContent().replace("[","").replace("]",""), Book.dateFormat));
                //System.out.println("publishDate:" + publishDate.toString());
            }
        } else {
            throw new Exception();
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
        if(!getFileExtension(file).equals("xml") || file.length() == 0){
            throw new FileNotFoundException ();
        }
        ArrayList<Book> books = new ArrayList<>();
        Document xmldoc = null;
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmldoc = docReader.parse(file);
            Element docEle = xmldoc.getDocumentElement();
            if(!docEle.getTagName().equals("library")) {
                throw new Exception();
            }
            NodeList djeca = docEle.getChildNodes();

            for(int i = 0; i < djeca.getLength(); i++) {
                Node dijete = djeca.item(i);
                if (dijete instanceof Element) {
                    readElements((Element) dijete);
                }
            }
            int size = author.size();
            if (title.size() != size || isbn.size() != size || pageCount.size() != size || publishDate.size() != size) {
                throw new Exception();
            }
            for (int i = 0; i < title.size(); i++) {
                books.add(new Book(author.get(i), title.get(i), isbn.get(i), pageCount.get(i), publishDate.get(i)));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
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
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("library");
            document.appendChild(root);
            for(Book book: books) {
                Element bookElement = document.createElement("book");
                root.appendChild(bookElement);

                Attr pageCountAttr = document.createAttribute("pageCount");
                pageCountAttr.setValue(String.valueOf(book.getPageCount()));
                bookElement.setAttributeNode(pageCountAttr);

                Element authorElement = document.createElement("author");
                authorElement.appendChild(document.createTextNode(book.getAuthor()));
                bookElement.appendChild(authorElement);

                Element titleElement = document.createElement("title");
                titleElement.appendChild(document.createTextNode(book.getTitle()));
                bookElement.appendChild(titleElement);

                Element isbnElement = document.createElement("isbn");
                isbnElement.appendChild(document.createTextNode(book.getIsbn()));
                bookElement.appendChild(isbnElement);

                Element publishDateElement = document.createElement("publishDate");
                publishDateElement.appendChild(document.createTextNode(Book.dateFormat.format(book.getPublishDate())));
                bookElement.appendChild(publishDateElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
