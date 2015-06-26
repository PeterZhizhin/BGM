package com.company;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class PlayList {

    public PlayList() throws Exception {

        tracks=new ArrayList[4];
        for (int i=0; i<4; i++) {
            tracks[i]=new ArrayList<>();
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document dom = db.parse("test.xml");

        NodeList themes=dom.getElementsByTagName("group");
        for (int i=0; i<themes.getLength(); i++) {
            Element eElement = (Element) themes.item(i);
            String name=eElement.getElementsByTagName("title").item(0).getTextContent();
            int id= Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
            groups[id]=name;
        }

        NodeList tracks=dom.getElementsByTagName("track");
        for (int i=0; i<tracks.getLength(); i++) {
            Element eElement = (Element) tracks.item(i);
            String url=eElement.getElementsByTagName("url").item(0).getTextContent();
            int themeId= Integer.parseInt(eElement.getElementsByTagName("groupid").item(0).getTextContent());
            int price= Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent());
            this.tracks[themeId].add(new Track(url, themeId, price));
        }

        for (int i=0; i<4; i++) {
            this.tracks[i].sort((o1, o2) -> Integer.compare(o1.getPrice(), o2.getPrice()));
        }

    }

    private String[] groups=new String[4];

    public String getGroup(int id) {
        return groups[id];
    }

    private ArrayList<Track>[] tracks;

    public Track getTrack(int group, int number) {
        return tracks[group].get(number);
    }

    @Test
    public void testReadNode() throws Exception {

        assertEquals("Аллах над нами", getGroup(0));
        assertEquals("Дуб Степь", getGroup(1));
        assertEquals("Конь", getGroup(2));
        assertEquals("Anime OST", getGroup(3));

        assertEquals("resources/audio/nasheeds/Nasheed - ISIS.mp3", getTrack(0, 0).getUrl());
        assertEquals("resources/audio/dubstep/01 - Make It Bun Dem (Original)-192.mp3", getTrack(1, 1).getUrl());
        assertEquals(30, getTrack(2, 2).getPrice());
        assertEquals(3, getTrack(3, 0).getGroup());

    }

}
