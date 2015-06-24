package com.company.Utils;

import com.company.OpenGL.Generators.TextResourceReader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public class ShaderUtils {

    public static int loadShader(String vertPath, String fragPath){

        String vert = TextResourceReader.readTextFile(vertPath);
        String frag = TextResourceReader.readTextFile(fragPath);

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
