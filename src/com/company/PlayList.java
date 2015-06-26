package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PlayList {

    public static String readTextFile(String fileName) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer);
                result.append("\n");
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public PlayList(String filename) {

        String xmlSource=readTextFile(filename);



    }

    public void testReadNode() {

    }

}
