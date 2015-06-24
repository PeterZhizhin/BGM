package com.company.Utils;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Utils {

    public static void processError(Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    public static void processError(String s) {
        processError(new Exception(s));
    }

    public static void checkForGLError() {
        int errCode = glGetError();
        if (errCode != GL_NO_ERROR) {
            String errString = "Unknown error code: " + errCode;

            switch (errCode) {
                case GL11.GL_INVALID_ENUM:
                    errString = "GL_INVALID_ENUM";
                    break;
                case GL11.GL_INVALID_VALUE:
                    errString = "GL_INVALID_VALUE";
                    break;
                case GL11.GL_INVALID_OPERATION:
                    errString = "GL_INVALID_OPERATION";
                    break;
                case GL11.GL_STACK_OVERFLOW:
                    errString = "GL_STACK_OVERFLOW";
                    break;
                case GL11.GL_STACK_UNDERFLOW:
                    errString = "GL_STACK_UNDERFLOW";
                    break;
                case GL11.GL_OUT_OF_MEMORY:
                    errString = "GL_OUT_OF_MEMORY";
                    break;
            }

            processError("OpenGL error occurred: " + errString);
        }
    }
}
