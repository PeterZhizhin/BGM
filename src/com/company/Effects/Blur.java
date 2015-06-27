package com.company.Effects;

import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.FBOTexture;
import com.company.Graphics.Shader;
import com.company.Math.Matrix4f;
import com.company.Square;
import com.company.Utils.Buffers;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Blur {

    private static FBOTexture hblur = new FBOTexture(1024,1024);
    private static FBOTexture vblur = new FBOTexture(1024,1024);

    public static AbstractTexture getBlured(AbstractTexture texture) { hblur.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        texture.bind();
        Shader.hblur.enable();
        Camera.useViewCamera();
        Camera.useProjectionCamera(Matrix4f.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1f, 1f));
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId, Matrix4f.IDENTITY);
        Square.draw();
        Shader.hblur.disable();
        texture.unbind();
        hblur.unbindForWriting();

        vblur.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        hblur.bind();
        Shader.vblur.enable();

        Camera.useViewCamera();
        Camera.useProjectionCamera(Matrix4f.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1f, 1f));

        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId, Matrix4f.IDENTITY);
        Square.draw();
        Shader.vblur.disable();
        hblur.unbind();
        vblur.unbindForWriting();

        return vblur;
    }
}
