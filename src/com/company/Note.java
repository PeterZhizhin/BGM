package com.company;

import com.company.Graphics.Shader;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;
import sun.security.provider.SHA;

import static com.company.Utils.Utils.checkForGLError;

public class Note {
    private Matrix4f model;
    private static Texture noteTexture;
    public Note(float x, float y) {
        model = Matrix4f.translate(x,y,0);
        //model = Matrix4f.scale(width,height,1).multiply(Matrix4f.translate(x, y, 0));
        noteTexture = new Texture("note.png");
        checkForGLError();
    }

    public static void initDraw() {
        noteTexture.bind();
    }

    public void draw() {
        checkForGLError();
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,model
        );
        Square.draw();
    }

    public static void disableDraw() {
        noteTexture.unbind();
    }
}
