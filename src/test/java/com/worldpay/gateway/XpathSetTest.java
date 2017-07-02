package com.worldpay.gateway;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link XpathSet}.
 */
public class XpathSetTest {

    private static DocumentBuilder documentBuilder;

    @BeforeClass
    public static void beforeAll() throws Exception{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    @Test
    public void singleElement() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        assertThat(set, hasSize(1));
        assertThat(set, hasItem("/xml/with/one/element"));
    }

    @Test
    public void multipleElements() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an><element/></an><another><element/></another></with></xml>"
        ));

        assertThat(set, hasItems(
                "/xml/with/an/element",
                "/xml/with/another/element"
        ));
    }

    @Test
    public void singleAttribute() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one attribute='foo'/></with></xml>"
        ));

        assertThat(set, hasSize(1));
        assertThat(set, hasItem("/xml/with/one/@attribute"));
    }

    @Test
    public void multipleAttributes() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo' anotherAttribute='bar'/></with></xml>"
        ));

        assertThat(set, hasItems(
                "/xml/with/an/@attribute",
                "/xml/with/an/@anotherAttribute"
        ));
    }

    @Test
    public void multipleElementAttributes() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo'/><another attribute='bar'/></with></xml>"
        ));

        assertThat(set, hasItems(
                "/xml/with/an/@attribute",
                "/xml/with/another/@attribute"
        ));
    }

    @Test
    public void mixedElementsAndAttributes() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        ));

        assertThat(set, hasItems(
                "/xml/with/an/element",
                "/xml/with/an/@attribute",
                "/xml/with/another/@attribute"
        ));
    }

    @Test
    public void mixedElementsAndAttributesInOrder() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        ), true);

        assertThat(set.toArray(), arrayContaining(
                "/xml/with/an/@attribute",
                "/xml/with/an/element",
                "/xml/with/another/@attribute"
        ));
    }

    @Test
    public void toArrayReturnsCorrectlySizedArray() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        ), true);

        assertThat(set.toArray(new String[3]), arrayContaining(
                "/xml/with/an/@attribute",
                "/xml/with/an/element",
                "/xml/with/another/@attribute"
        ));
    }

    @Test
    public void isEmptyOnEmptyDocument() throws Exception {
        XpathSet set = new XpathSet(documentBuilder.newDocument());

        assertTrue(set.isEmpty());
    }

    @Test
    public void containsSpecifiedXpath() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        ));

        assertTrue(set.contains("/xml/with/an/@attribute"));
    }

    @Test
    public void containsSpecifiedMultipleXpaths() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        ));

        assertTrue(set.containsAll(Arrays.asList(
                "/xml/with/an/@attribute",
                "/xml/with/an/element"
        )));
    }

    @Test
    public void carriageReturnsDontThrowNullPointerExceptions() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml>\n<with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        ), true);

        assertThat(set.toArray(new String[3]), arrayContaining(
                "/xml/with/an/@attribute",
                "/xml/with/an/element",
                "/xml/with/another/@attribute"
        ));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void clearThrowsException() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        set.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrowsException() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        set.remove(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAllThrowsException() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        set.removeAll(new HashSet<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAllThrowsException() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        set.retainAll(new HashSet<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllThrowsException() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        set.addAll(new HashSet<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addThrowsException() throws Exception {
        XpathSet set = new XpathSet(buildDocument(
                "<xml><with><one><element/></one></with></xml>"
        ));

        set.add("foo");
    }

    private Document buildDocument(String xml) throws Exception {
        return documentBuilder.parse(IOUtils.toInputStream(xml));
    }
}