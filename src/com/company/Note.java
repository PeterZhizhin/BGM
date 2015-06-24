package com.company;

import com.company.OpenGL.Generators.TextureGenerator;
import com.company.Utils.Buffers;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL13.*;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Note {
    private Matrix4f model;
    private static int noteTexture;
    public static void loadTexture() {
        noteTexture = TextureGenerator.loadTextureFromRes("note.png",false);
    }

    public Note(int x, int y, int width, int height) {
        model = new Matrix4f().scale(width,height,1).translate(x,y,0);
    }

    public static void initDraw() {
        World.getTextureShader().use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, noteTexture);
        World.getTextureShader().setTexture(0);
        Square.bind();
    }

    public void draw(Matrix4f ortho) {
        FloatBuffer matrix = ByteBuffer.allocateDirect(16 * Buffers.FLOAT_BYTE_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        ortho.mul(model).get(matrix);
        World.getTextureShader().setMatrix(matrix);
        Square.draw();
    }
}
