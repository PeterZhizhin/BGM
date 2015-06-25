package com.company;

import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.FBOTexture;
import com.company.Graphics.Shader;
import com.company.Math.Matrix4f;
import org.lwjgl.Sys;

import static org.lwjgl.opengl.GL11.*;

public class BarsRenderer {


    private int barsNumber;
    private Matrix4f[] dists;
    private Matrix4f projection;
    private Matrix4f projection512;

    public BarsRenderer(int barsNumber) {
        this.barsNumber = barsNumber;
        flatBars=new FBOTexture(barsNumber, 1);
        radialBars=new FBOTexture(512, 512);
        dists=new Matrix4f[barsNumber];
        for (int i=0; i<barsNumber; i++) {
            dists[i]=Matrix4f.translate(i+1, 0, 0);
        }
        projection=Matrix4f.orthographic(0, barsNumber, 1, 0, -1, 1);
        projection512=Matrix4f.orthographic(-0.5f, 0.5f, -0.5f, 0.5f, -1, 1);
    }

    private FBOTexture flatBars;
    private FBOTexture radialBars;

    private static final int coloredShaderColorUniformId=Shader.coloredShader.getUniform("color");
    private static final int coloredShaderLengthUniformId=Shader.coloredShader.getUniform("length");

    private float function (float input) {
        float res=input+ (System.currentTimeMillis()%10000)/10000f;
        while (res>1.0f)
            res-=1.0f;
        return res;
    }

    public AbstractTexture renderBars(float[] bars) {

        flatBars.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Shader.coloredShader.enable();
                Camera.useViewCamera();
                Camera.useProjectionCamera(projection);


                for (int i=0; i<barsNumber; i++) {
                    Shader.coloredShader.setUniform1f(coloredShaderColorUniformId,
                            function(i*1f/barsNumber));
                    Shader.coloredShader.setUniform1f(coloredShaderLengthUniformId, bars[i]);
                    Shader.coloredShader.setUniformMat4f(Shader.coloredShader.modelMatrixUniformId,
                            dists[i]);
                    Square.draw();
                }
            Shader.coloredShader.disable();
        flatBars.unbindForWriting();

        radialBars.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            flatBars.bind();
            Shader.unrollShadows.enable();
                Camera.useViewCamera();
                Camera.useProjectionCamera(projection512);
                Shader.unrollShadows.setUniformMat4f(Shader.unrollShadows.modelMatrixUniformId, Matrix4f.IDENTITY);
                Square.draw();
            Shader.unrollShadows.disable();
            flatBars.unbind();

        radialBars.unbindForWriting();

        return radialBars;
    }

}
