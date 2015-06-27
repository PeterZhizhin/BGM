package com.company;

import com.company.Effects.Glow;
import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.Shader;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;

import static com.company.Utils.Utils.checkForGLError;

public class BackgroundRenderer {
    private AbstractTexture texture;
    private AbstractTexture bluredTexture;
    private Matrix4f model;

    public BackgroundRenderer(Matrix4f matrix, String pathNormal, String pathBlured) {
        model = matrix;
        texture = new Texture(pathNormal);
        bluredTexture = new Texture(pathBlured);
    }



    public void render(float r, float g, float b) {
        AbstractTexture glowed = Glow.getGlowed(texture,r,g,b);
        checkForGLError();
        Shader.defaultShader.enable();
        Camera.useCamera();
        glowed.bind();
        Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, model);
        Square.draw();
        glowed.unbind();
        Shader.defaultShader.disable();

    }
}
