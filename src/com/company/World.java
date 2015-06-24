package com.company;

import com.company.Math.Matrix4f;
import com.company.Graphics.Shader;

import static org.lwjgl.opengl.GL11.glViewport;

public class World {
    private static Matrix4f ortho;

    private static Note[][] notes;
    private static Note mouse;
    private static int height;
    private static int width;

    public static void init(int widthA,int heightA) {
        width = widthA; height = heightA;
        glViewport(0,0,width,height);
        float leftPlaceInLeft = 5-6.0f*width/height;
        ortho = Matrix4f.orthographic(leftPlaceInLeft,5,-1,5,-1,1);
        notes = new Note[4][4];
        float x,y=0.5f;
        for (int i = 0; i < 4; i++) {
            x=0.5f;
            for (int j=0; j<4; j++) {
                final int iF=i;
                final int jF=j;
                notes[i][j] = new Note(() -> System.out.println("Pressed "+iF+" : "+jF),x, y);
                x+=1f;
            }
            y+=1f;
        }

    }

    public static void update(int deltaTime) {
        boolean mousePressedRightNow=Input.isWasMousePressed();
        if (mousePressedRightNow) {
            System.out.println("PRESSED: "+Input.getMouseX()+" :: "+Input.getMouseY());
        }
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                notes[i][j].update(deltaTime);
            }
        }

    }

    public static void draw() {
        Shader.defaultShader.enable();
        Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().viewMatrixUniformId,Matrix4f.IDENTITY);
        Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().projectionMatrixUniformId, ortho);
        Note.initDraw();
        for(Note[] n1 : notes)
            for(Note note : n1)
                note.draw();

        Note.disableDraw();
        Shader.defaultShader.disable();

    }

    public static void destroy() {

    }



    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
}
