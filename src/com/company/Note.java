package com.company;

import com.company.Math.Matrix4f;
import com.company.OpenGL.Generators.TextureGenerator;

import static org.lwjgl.opengl.GL13.*;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

public class Note {
    private Matrix4f model;
    private static int noteTexture;
    public static void loadTexture() {
        noteTexture = TextureGenerator.loadTextureFromRes("note.png",false);
    }

    public Note(int x, int y, int width, int height) {
        model = new Matrix4f().multiply(Matrix4f.scale(width, height, 1)).multiply(Matrix4f.translate(x, y, 0));
    }

    public static void initDraw() {
        World.getTextureShader().use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, noteTexture);
        World.getTextureShader().setTexture(0);
    }

    public void draw(Matrix4f ortho) {
        FloatBuffer matrix = FloatBuffer.allocate(16);
        ortho.mul(model).get(matrix);
        World.getTextureShader().setMatrix(matrix);

    }
}
