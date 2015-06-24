package com.company;

import com.company.Math.Matrix4f;
import com.company.OpenGL.Shaders.TextureShader;

public class World {
    private static TextureShader textureShader;
    public static TextureShader getTextureShader() {return textureShader;}
    private static Matrix4f ortho;
    public static void init(int width,int height) {
        Square.initVAO();
        textureShader = new TextureShader();
        ortho=Matrix4f.orthographic(0, width, height, 0, -1, 1);
    }

    public static void update(int deltaTime) {

    }

    public static void draw() {

    }

    public static void destroy() {
        Square.destroy();
    }
}
