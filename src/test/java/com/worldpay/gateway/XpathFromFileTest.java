package com.worldpay.gateway;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link XpathFromFile}.
 */
public class XpathFromFileTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }

    @Test
    public void printXpaths_printsInOrder() throws Exception {
        InputStream is = IOUtils.toInputStream(
                "<xml><with><an attribute='foo'><element/></an><another attribute='bar'/></with></xml>"
        );

        new XpathFromFile().printXpaths(is);

        assertEquals(
                "/xml/with/an/@attribute\n" +
                "/xml/with/an/element\n" +
                "/xml/with/another/@attribute\n",
                outContent.toString()
        );
    }
}