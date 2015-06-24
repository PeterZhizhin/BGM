package com.company;

import com.company.OpenGL.Shaders.TextureShader;
import org.joml.Matrix4f;

public class World {
    private static TextureShader textureShader;
    public static TextureShader getTextureShader() {return textureShader;}
    private static Matrix4f ortho = new Matrix4f();

    private static Note note = new Note(100,100,100,100);

    public static void init(int width,int height) {
        Square.initVAO();
        textureShader = new TextureShader();
        ortho.ortho(0, width, height, 0, -1, 1);

    }

    public static void update(int deltaTime) {

    }

    public static void draw() {
        Note.initDraw();
        note.draw(ortho);
    }

    public static void destroy() {
        Square.destroy();
    }
}
