package com.company.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public class ShaderUtils {
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
                    glGetShaderInfoLog(vertID)).printStackTrace();
            System.exit(1);
        }

        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            new Exception("Failed to compile fragment shader "+fragPath+":\n"+
                    glGetShaderInfoLog(fragID)).printStackTrace();
            System.exit(1);
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        return program;
    }

}
