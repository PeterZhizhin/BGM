package com.company;


import com.company.GUI.FontRenderer;
import com.company.Graphics.Camera;
import com.company.Graphics.FBOTexture;
import com.company.Graphics.Shader;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;
import static org.lwjgl.opengl.GL11.*;

public class RecordTexture extends FBOTexture {
    private static final FontRenderer fr = new FontRenderer(0.45f, 0.25f);

    public RecordTexture(int value) {
        super(512,512);
        Texture record = new Texture("note.png");
        Matrix4f projection = Matrix4f.orthographic(-0.5f,0.5f,0.5f,-0.5f,-1,1);
        bindForWriting();
        glClearColor(0,0,0,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Shader.defaultShader.enable();
            Camera.useViewCamera();
            Camera.useProjectionCamera(projection);
            Shader.defaultShader.setUniformMat4f(
                    Shader.getCurrentShader().modelMatrixUniformId,
                    Matrix4f.IDENTITY);
            record.bind();
            Square.draw();
            record.unbind();
            fr.render(String.valueOf(value),-0.36f,-0.24f);



            Shader.defaultShader.disable();
        record.bind();
        record.unbind();

        unbindForWriting();
    }


}
