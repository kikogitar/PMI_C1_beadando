package org.example;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<Book> books = readBooksFromXml("src/main/resources/books.xml");

        int choice = -1;
        while (choice != 0) {
            switch (choice) {
                case 1 -> listBooks(books);
                case 2 -> addNewBook(books);
                case 3 -> modifyBook(books);
                case 4 -> deleteBook(books);
                case 5 -> saveUsersToXml(books, "src/main/resources/books.xml");
            }
            System.out.println("1 - Könyvek listája\n2 - Új könyv hozzáadása\n3 - Meglévő könyv módosítása\n4 - Könyv törlése\n5 - Mentés\n0 - Mentés és kilépés\n");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 0 || choice > 5) {
                    System.out.println("Nem érvényes opció");
                }
            } catch (InputMismatchException e) {
                System.out.println("Nem érvényes opció");
                scanner.nextLine();
            }
        }

        saveUsersToXml(books, "src/main/resources/books.xml");
    }




//xml olvas
    public static ArrayList<Book> readBooksFromXml(String filepath) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder =
                    documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);

            Element rootElement = document.getDocumentElement();

            NodeList childNodesList = rootElement.getChildNodes();

            int numberOfElementNodes = 0;
            Node node;
            for (int i = 0; i < childNodesList.getLength(); i++) {
                node = childNodesList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    numberOfElementNodes++;
                    NodeList childNodesOfUserTag = node.getChildNodes();
                    String id = "", szerzo = "", cim = "", oldal = "";
                    for (int j = 0; j < childNodesOfUserTag.getLength(); j++) {

                        if (childNodesOfUserTag.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodesOfUserTag.item(j).getNodeName()) {
                                case "id" -> id = childNodesOfUserTag.item(j).getTextContent();
                                case "szerzo" -> szerzo = childNodesOfUserTag.item(j).getTextContent();
                                case "cim" -> cim = childNodesOfUserTag.item(j).getTextContent();
                                case "oldal" -> oldal = childNodesOfUserTag.item(j).getTextContent();
                            }
                        }
                    }
                    books.add(new Book(Integer.parseInt(id), szerzo, cim,
                            Integer.parseInt(oldal)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    private static void listBooks(ArrayList<Book> books) {
        for (int i =0; i < books.size(); i++){
            System.out.println(books.get(i).getId() + " " + books.get(i).getSzerzo() + " " + books.get(i).getCim() + " " + books.get(i).getOldal());
        }
        System.out.println("");
    }

//új könyv hozzáadása
    private static void addNewBook(ArrayList<Book> books) {
        int id = books.size();
        try { System.out.print("Add meg a könyv szerzőjét: ");
            String szerzo = scanner.nextLine();
            //System.out.println(szerzo);
            System.out.print("Add meg a könyv címét: ");
            String cim = scanner.nextLine();
            //System.out.println(cim);
            System.out.print("Add meg a könyv oldalszámát:");
            int oldal = scanner.nextInt();
            books.add(new Book(id, szerzo, cim, oldal));
        } catch (InputMismatchException ex) {
            //ex.printStackTrace();
            String trash = scanner.nextLine();
            System.out.println("Érvénytelen adat\n");
            return;
        }
        System.out.println("");
    }

//meglévő könyv módosítása
    private static void modifyBook(ArrayList<Book> books) {
        System.out.print("Add meg a módósítandó könyv id-jét: ");
        int id = scanner.nextInt();
        String trash = scanner.nextLine();
        for (int i=0; i<books.size(); i++ ) {
            if (books.get(i).getId()==id) {
                try {
                    System.out.print("Add meg a könyv szerzőjét: ");
                    books.get(i).setSzerzo(scanner.nextLine());
                    System.out.print("Add meg a könyv címét: ");
                    books.get(i).setCim(scanner.nextLine());
                    System.out.print("Add meg a könyv olodalszámát: ");
                    books.get(i).setOldal(scanner.nextInt());
                    System.out.println("Sikeres változtatás\n");
                    return;
                } catch (InputMismatchException ex) {
                    trash = scanner.nextLine();
                    System.out.println("Érvénytelen adat\n");
                    return;
                }
            }
        }
        System.out.println("Nincs ilyen azonosítójú könyv\n");
    }

// könyv törlése
    private static void deleteBook(ArrayList<Book> books) {
        System.out.print("add meg a módósítandó könyv id-jét: ");
        int id = scanner.nextInt();
        for (int i=0; i<books.size(); i++ ) {
            if (books.get(i).getId()==id) {
                books.remove(i);
                while (i < books.size()){
                    books.get(i).setId(books.get(i).getId()-1);
                    i++;
                }
                System.out.println("Sikerse törlés\n");
                return;
            }
        }
        System.out.println("Nincs ilyen azonosítójú könyv\n");
    }

//mentés
    public static void saveUsersToXml(ArrayList<Book> books, String filepath) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("Books");
            document.appendChild(rootElement);

            for (Book book : books) {
                Element bookElement = document.createElement("book");
                rootElement.appendChild(bookElement);
                createChildElement(document, bookElement, "id", String.valueOf(book.getId()));
                createChildElement(document, bookElement, "szerzo", book.getSzerzo());
                createChildElement(document, bookElement, "cim", book.getCim());
                createChildElement(document, bookElement, "oldal", String.valueOf(book.getOldal()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("Sikeres mentés\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void createChildElement(Document document, Element parent,
                                           String tagName, String value) {
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }
}