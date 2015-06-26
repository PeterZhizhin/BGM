package com.company.Effects;

import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.FBOTexture;
import com.company.Graphics.Shader;
import com.company.Math.Matrix4f;
import com.company.Square;

import static org.lwjgl.opengl.GL11.*;

public class Blur extends FBOTexture {

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

    private Blur(int sizeX, int sizeY, AbstractTexture texture, Shader blur) {
        super(sizeX, sizeY);
        blur(texture,blur);
    }


    public Blur(int width, int height, AbstractTexture texture) {
        super(width, height);
        AbstractTexture hblur = new Blur(width,height,texture,Shader.hblur);
        blur(hblur,Shader.vblur);
    }
}
