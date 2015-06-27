package com.company.Effects;

import com.company.Graphics.*;
import com.company.Math.Matrix4f;
import com.company.Square;

import static com.company.Utils.Utils.checkForGLError;
import static org.lwjgl.opengl.GL11.*;

public class Glow {
    private static final int color = Shader.additiveBlend.getUniform("colorTex");
    private static final FBOTexture texture = new FBOTexture(1024,1024);

    public static AbstractTexture getGlowed(AbstractTexture source, float r, float g, float b) {
        Matrix4f projection = Matrix4f.orthographic(-0.5f,0.5f,0.5f,-0.5f,-1,1);
        AbstractTexture blured = Blur.getBlured(source);
        texture.bindForWriting();
        checkForGLError();
        glClearColor(0,0,0,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader.additiveBlend.enable();

        Camera.useViewCamera();
        Camera.useProjectionCamera(projection);
        Shader.additiveBlend.setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,
                Matrix4f.IDENTITY);

        source.bind();
        blured.bindToSecond();

        Shader.additiveBlend.setUniform3f(color, r, g, b);

        Square.draw();

        source.unbind();
        blured.unbindFromSecond();


        Shader.additiveBlend.disable();

        texture.unbindForWriting();

        return texture;

    }
}
