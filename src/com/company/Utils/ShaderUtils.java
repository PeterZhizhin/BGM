package com.company.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public class ShaderUtils {

    private static String toLengthedString(String source, int limit, char filler) {
        StringBuilder s=new StringBuilder();
        s.append(source);
        while (s.length()<limit) {
            s.append(filler);
        }
        return s.toString();
    }

    private static String numerizeLines(String s) {
        String[] lines=s.split("\n");
        int lengthInDec=String.valueOf(lines.length+1).length();
        lengthInDec=Math.max(6, lengthInDec);
        StringBuilder sb=new StringBuilder();
        sb.append('\n');
        sb.append(toLengthedString("LINES", lengthInDec, '-'))
                .append("|-CODE--------------------------\n");
        for (int i=0; i<lines.length; i++) {
            sb.append(toLengthedString(String.valueOf(i+1), lengthInDec, ' '))
                    .append("| ")
                    .append(lines[i])
                    .append('\n');
        }
        return sb.toString();
    }

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

    public static int loadShader(String vertPath, String fragPath){

        String vert = readTextFile(vertPath);
        String frag = readTextFile(fragPath);

        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            new Exception("Failed to compile vertex shader "+vertPath+":\n"+
                    glGetShaderInfoLog(vertID)+numerizeLines(vert)).printStackTrace();
            System.exit(1);
        }

        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            new Exception("Failed to compile fragment shader "+fragPath+":\n"+
                    glGetShaderInfoLog(fragID)+numerizeLines(frag)).printStackTrace();
            System.exit(1);
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        return program;
    }

}
