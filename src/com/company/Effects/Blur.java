package com.company.Effects;

import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.FBOTexture;
import com.company.Graphics.Shader;
import com.company.Math.Matrix4f;
import com.company.Square;

import static org.lwjgl.opengl.GL11.*;

public class Blur {
    private static FBOTexture hblured = new FBOTexture(512,512);
    private static FBOTexture blured = new FBOTexture(512,512);

    public static AbstractTexture getBlured(AbstractTexture texture) {
        blur(hblured,texture,Shader.hblur);
        blur(blured,hblured,Shader.vblur);
        return blured;
    }

    private static void blur(FBOTexture textureToWrite, AbstractTexture texture, Shader blur) {
        Matrix4f projection = Matrix4f.orthographic(-0.5f,0.5f,0.5f,-0.5f,-1,1);
        textureToWrite.bindForWriting();
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
        textureToWrite.unbindForWriting();
    }
}
