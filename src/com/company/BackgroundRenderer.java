package com.company;

import com.company.Effects.Glow;
import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.Shader;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.company.Utils.Utils.checkForGLError;

public class BackgroundRenderer {
    private AbstractTexture titlesLayer;
    private AbstractTexture titlesLayerBlured;
    private AbstractTexture texture;
    private AbstractTexture bluredTexture;
    private Matrix4f model;

    public BackgroundRenderer(Matrix4f matrix, String pathNormal, String pathBlured, String[] titles) {
        model = matrix;
        texture = new Texture(pathNormal);
        bluredTexture = new Texture(pathBlured);

        BufferedImage test=new BufferedImage(1600, 1200, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2= (Graphics2D) test.getGraphics();

        Color black=new Color(0, 0, 0);
        g2.setColor(black);

        /*BufferedImage titlesTex=null;
        try {
            titlesTex = BackgroundUtils.drawTitles(
                    test,
                    titles,
                    new int[]{20,20,20,20},
                    new int[]{100,200,300,400},
                    new Color(255, 255, 255),
                    74f
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        titlesLayer = new Texture(titlesTex);
        titlesLayerBlured = new Texture(BackgroundUtils.blur(titlesTex,100));

        System.out.println("Background rendered");       */


    }



    public void render(float r, float g, float b,
                       float rT, float gT, float bT) {
        AbstractTexture glowed = Glow.getGlowed(texture,bluredTexture,r,g,b);
        checkForGLError();
        Shader.defaultShader.enable();
        Camera.useCamera();
        glowed.bind();
        Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, model);
        Square.draw();
        glowed.unbind();
        Shader.defaultShader.disable();

        /*Shader.defaultShader.enable();
        Camera.useCamera();
        Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, model);
        glowed = Glow.getGlowed(titlesLayer, titlesLayerBlured, rT, gT, bT);
        glowed.bind();
        Square.draw();
        glowed.unbind();
        Shader.defaultShader.disable(); */
    }
}
