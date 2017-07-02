package com.worldpay.gateway;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Takes an XML document and outputs XPaths that match it.
 */
public class XpathFromFile {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        FileInputStream fis = new FileInputStream(args[0]);
        new XpathFromFile().printXpaths(fis);
    }

    public void printXpaths(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        new XpathSet(document, true).forEach(System.out::println);
    }

}

