package com.company.OpenGL.Generators;

import java.io.*;


public class TextResourceReader {
    public static String readTextFile(String fileName) {
        StringBuilder body = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                    body.append(nextLine.replaceAll("[/]+.*",""));
                    body.append("\n");
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File "+fileName+" not found ");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.print("File "+fileName+" could not be readed");
        }

        return body.toString();
    }
}
