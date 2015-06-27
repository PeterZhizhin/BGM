package com.company;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class RunConfigs {

    public RunConfigs() {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document dom = db.parse("configs.xml");

            NodeList themes = dom.getElementsByTagName("guessmelody");
            for (int i = 0; i < themes.getLength(); i++) {
                Element eElement = (Element) themes.item(i);
                String fullscreen = eElement.getElementsByTagName("fullscreen").item(0).getTextContent();
                this.fullscreen=parseBoolean(fullscreen);
            }

        } catch (Exception e) {
            System.err.println("SRS XML SHIT");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private boolean parseBoolean(String fullscreen) {
        fullscreen=fullscreen.toLowerCase();
        return fullscreen.equals("true") ||
                fullscreen.equals("1") ||
                fullscreen.equals("fullscreen");
    }

    private boolean fullscreen=false;

    public boolean getIsFullScreen() {
        return fullscreen;
    }

    @Test
    public void testReadNode() throws Exception {

        assertEquals(true, getIsFullScreen());

    }

}
