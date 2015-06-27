package com.company.Graphics;

import com.company.SomethingThatUsesNativeResources;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public abstract class AbstractTexture extends SomethingThatUsesNativeResources {
    private static int[] lastBindedId={0, 0};

    public int getTextureId() {
        return textureId;
    }

    private int textureId;

    public AbstractTexture (int textureId) {
        this.textureId = textureId;
    }


    public void unbindFromSecond(){
        glActiveTexture(GL13.GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, 0);
        lastBindedId[1]=0;
    }

    public void bindToSecond(){
        if (lastBindedId[1]!=0) {
            new Exception("Trying to bind new texture without unbinding last texture,\n" +
                    "current texture: "+textureId+", last binded: "+lastBindedId[1]).printStackTrace();
        }
        glActiveTexture(GL13.GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, textureId);
        lastBindedId[1]=textureId;
    }

    public void bind(){
        if (lastBindedId[0]!=0) {
            new Exception("Trying to bind new texture without unbinding last texture,\n" +
                    "current texture: "+textureId+", last binded: "+lastBindedId[0]).printStackTrace();
        }
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        lastBindedId[0]=textureId;
    }

    public void unbind(){
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
        lastBindedId[0]=0;
    }

    public abstract int getWidthInPX();

    public abstract int getHateInPX();

}
