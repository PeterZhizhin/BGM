package com.company.Effects;

import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.FBOTexture;
import com.company.Graphics.Shader;
import com.company.Math.Matrix4f;
import com.company.Square;

import static org.lwjgl.opengl.GL11.*;

public class Glow extends FBOTexture {


    private static final int color1 = Shader.additiveBlend.getUniform("colorTex1");
    private static final int color2 = Shader.additiveBlend.getUniform("colorTex2");

    public Glow(int width, int height, AbstractTexture texture,
                float r1, float g1, float b1,
                float r2, float g2, float b2) {
        super(width, height);
        Matrix4f projection = Matrix4f.orthographic(-0.5f,0.5f,0.5f,-0.5f,-1,1);
        Blur blured = new Blur(width,height,texture);
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

        Shader.additiveBlend.setUniform4f(color1,r1,g1,b1,1);
        Shader.additiveBlend.setUniform4f(color2,r2,g2,b2,1);

        Square.draw();

        texture.unbind();
        blured.unbind();


        Shader.additiveBlend.disable();

        unbindForWriting();
    }
}
