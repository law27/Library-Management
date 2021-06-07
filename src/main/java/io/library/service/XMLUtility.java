package io.library.service;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLUtility {
    private static DocumentBuilder documentBuilder;
    private static File file;

    public static void init() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        file = new File("src/main/xml/borrow.xml");
    }

    public static void writeToXML(String bookId, String userName, String borrowDate, String returnDate) {
        Document document = null;
        Document newDocument = documentBuilder.newDocument();
        NodeList nodeList = null;
        try {
            document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            nodeList = document.getElementsByTagName("borrow");
        } catch (Exception e) {
            System.out.println("here");
            e.printStackTrace();
        }
        try {

            Node root = newDocument.createElement("borrows");
            newDocument.appendChild(root);
            if (nodeList != null && nodeList.getLength() != 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node borrowNode = newDocument.createElement("borrow");
                    root.appendChild(borrowNode);

                    Element element = (Element) nodeList.item(i);

                    Node bookNode = newDocument.createElement("book-id");
                    bookNode.appendChild(newDocument.createTextNode(element.getElementsByTagName("book-id").item(0).getTextContent()));
                    borrowNode.appendChild(bookNode);

                    Node userNode = newDocument.createElement("user");
                    userNode.appendChild(newDocument.createTextNode(element.getElementsByTagName("user").item(0).getTextContent()));
                    borrowNode.appendChild(userNode);

                    Node borrowDateNode = newDocument.createElement("borrow-date");
                    borrowDateNode.appendChild(newDocument.createTextNode(element.getElementsByTagName("borrow-date").item(0).getTextContent()));
                    borrowNode.appendChild(borrowDateNode);

                    Node returnDateNode = newDocument.createElement("return-date");
                    returnDateNode.appendChild(newDocument.createTextNode(element.getElementsByTagName("return-date").item(0).getTextContent()));
                    borrowNode.appendChild(returnDateNode);
                }
            }

            Node borrowNode = newDocument.createElement("borrow");
            root.appendChild(borrowNode);

            Node bookNode = newDocument.createElement("book-id");
            bookNode.appendChild(newDocument.createTextNode(bookId));
            borrowNode.appendChild(bookNode);

            Node userNode = newDocument.createElement("user");
            userNode.appendChild(newDocument.createTextNode(userName));
            borrowNode.appendChild(userNode);

            Node borrowDateNode = newDocument.createElement("borrow-date");
            borrowDateNode.appendChild(newDocument.createTextNode(borrowDate));
            borrowNode.appendChild(borrowDateNode);

            Node returnDateNode = newDocument.createElement("return-date");
            returnDateNode.appendChild(newDocument.createTextNode(returnDate));
            borrowNode.appendChild(returnDateNode);


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(newDocument);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
