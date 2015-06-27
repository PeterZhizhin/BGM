package com.company.Graphics;


import com.company.Math.Matrix4f;
import com.company.Square;
import com.company.World;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static com.company.Utils.Utils.checkForGLError;
import static com.company.Utils.Utils.processError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

public class FBOTexture extends AbstractTexture {

    private static ArrayList<FBOTexture> fboTextures=new ArrayList<>();
    public static void disposeAll() {

        while (fboTextures.size()!=0) {
            fboTextures.get(0).dispose();
        }

    }

    private int fboId;
    private int width;
    private int height;

    private static int createTexture(int width, int height) {

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D,
                0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

        glBindTexture(GL_TEXTURE_2D, 0);

        checkForGLError();

        return textureId;
    }

    public FBOTexture(int width, int height) {
        super(createTexture(width, height));

        fboId=glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, getTextureId(), 0);

        int errCode=glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (errCode !=GL_FRAMEBUFFER_COMPLETE) {
            processError("Cannot create FBO");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        System.out.println("Created FBO texture "+width+"x"+height+", id: "+fboId);
        fboTextures.add(this);
    }

    public FBOTexture(AbstractTexture texture) {
        this(texture.getWidthInPX(),texture.getHateInPX());

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
        texture.bind();
        Square.draw();
        texture.unbind();



        Shader.defaultShader.disable();

        unbindForWriting();
    }

    private static int bindedFBO=0;

    public void bindForWriting(){
        if (bindedFBO!=0) {
            new Exception("FBO already binded!").printStackTrace();
        } else {
            glBindFramebuffer(GL_FRAMEBUFFER, fboId);
            checkForGLError();
            glViewport(0, 0, width, height);
            bindedFBO = fboId;
        }
    }

    public void unbindForWriting(){
        if (bindedFBO==0) {
            new Exception("FBO already unbinded!").printStackTrace();
        } else {
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glViewport(0, 0, World.getWidth(), World.getHeight());
            bindedFBO=0;
        }
    }

    protected void dispose() {
        glDeleteFramebuffers(fboId);
        glDeleteTextures(getTextureId());
        fboTextures.remove(this);
    }

    @Override
    public int getWidthInPX() {
        return width;
    }

    @Override
    public int getHateInPX() {
        return height;
    }
}
