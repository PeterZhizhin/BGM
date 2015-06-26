package com.company;

import com.company.Graphics.*;
import com.company.Math.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Background extends FBOTexture {

    private Matrix4f model;

    public Matrix4f getModel() {
        return model;
    }

    private void blur(AbstractTexture texture, Shader blur) {
        Matrix4f projection = Matrix4f.orthographic(-0.5f,0.5f,0.5f,-0.5f,-1,1);
        bindForWriting();
        glClearColor(0,0,0,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        blur.enable();


        Camera.useViewCamera();
        Camera.useProjectionCamera(projection);
        blur.setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,
                Matrix4f.IDENTITY);
        texture.bind();
        Square.draw();
        texture.unbind();

        blur.disable();
        unbindForWriting();
    }

    private Background(int sizeX, int sizeY, AbstractTexture texture, Shader blur) {
        super(sizeX, sizeY);
        blur(texture,blur);
    }

    public Background(float x, float y, float width, float height, int sizeX, int sizeY) {
        super(sizeX, sizeY);
        model = Matrix4f.translate(x,y,0).multiply(Matrix4f.scale(width,height,1));
        Texture texture = new Texture("lightmap.png");
        Background vblured = new Background(sizeX,sizeY,texture,Shader.vblur);
        Background blured = new Background(sizeX,sizeY,vblured,Shader.hblur);
        Matrix4f projection = Matrix4f.orthographic(-0.5f,0.5f,0.5f,-0.5f,-1,1);
        bindForWriting();
        glClearColor(0,0,0,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader.additiveBlend.enable();

        Camera.useViewCamera();
        Camera.useProjectionCamera(projection);
        Shader.additiveBlend.setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,
                Matrix4f.IDENTITY);

        texture.bind();
        blured.bindToSecond();

        //Shader.additiveBlend.setUniform4f(Shader.additiveBlend.getUniform("colorTex1"), 0, 0, 1, 1);
        //Shader.additiveBlend.setUniform4f(Shader.additiveBlend.getUniform("colorTex2"),0,0,1,1);

        Square.draw();

        texture.unbind();
        blured.unbind();


        Shader.additiveBlend.disable();

        unbindForWriting();

    }
}
