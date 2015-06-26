package com.company.Graphics;

import com.company.Math.Matrix4f;
import com.company.Utils.ShaderUtils;
import static com.company.Utils.Utils.checkForGLError;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int UV_COORDS = 1;

    private int ID;

    public static final Shader additiveBlend =
            new Shader("resources/shaders/additiveBlend.vert",
                    "resources/shaders/additiveBlend.frag")
                    .bindFirstTexture().bindSecondTexture();

    public static final Shader defaultShader =
            new Shader("resources/shaders/static.vert",
                    "resources/shaders/static.frag")
                    .bindFirstTexture();

    public static final Shader coloredShader =
            new Shader("resources/shaders/colored.vert",
                    "resources/shaders/colored.frag");

    public static final Shader unrollShadows =
            new Shader("resources/shaders/unrollShadows.vert",
                    "resources/shaders/unrollShadows.frag");

    public static final Shader hblur =
            new Shader("resources/shaders/blur/hblur.vert", "resources/shaders/blur/blur.frag")
                    .bindFirstTexture()
            ;

    public static final Shader vblur =
            new Shader("resources/shaders/blur/vblur.vert", "resources/shaders/blur/blur.frag")
                    .bindFirstTexture()
            ;

    private static Shader currentShader=null;

    public final int modelMatrixUniformId;
    public final int viewMatrixUniformId;
    public final int projectionMatrixUniformId;

    public Shader(String vertex, String fragment){
        ID = ShaderUtils.loadShader(vertex, fragment);
        checkForGLError();
        System.out.println("Shader "+vertex+"+"+fragment+" binded to id "+ID);

        modelMatrixUniformId =getUniform("ml_matrix");
        viewMatrixUniformId =getUniform("vw_matrix");
        projectionMatrixUniformId =getUniform("pr_matrix");
    }

    public static Shader getCurrentShader() {
        return currentShader;
    }


    public Shader bindFirstTexture() {
        enable();
        setUniform1i(getUniform("tex"), 0);
        disable();
        return this;
    }

    public Shader bindSecondTexture() {
        enable();
        setUniform1i(getUniform("tex2"), 1);
        disable();
        return this;
    }

    public int getUniform(String name){
        int result = glGetUniformLocation(ID, name);
        if(result == -1) {
            new Exception("Could not find uniform variable'" + name + "'!\n" +
                    "Shader id: "+ID).printStackTrace();
            System.exit(1);
            return -1;
        } else
            return result;
    }

    public void setUniform1i(int id, int value) {
        forceEnable();
        glUniform1i(id, value);
    }

    public void setUniform1f(int id, float value) {
        forceEnable();
        glUniform1f(id, value);
    }
    public void setUniform4f(int id, float x,float y, float z, float w) {
        forceEnable();
        glUniform4f(id, x,y,z,w);
    }

    public void setUniformMat4f(int id, Matrix4f matrix) {
        forceEnable();
        glUniformMatrix4fv(id, false, matrix.toFloatBuffer());
    }

    public void forceEnable() {
        if (currentShader==null || currentShader.ID!=ID) {
            new Exception("Force enabling shader!").printStackTrace();
            enable();
        }
    }

    public void enable() {
        glUseProgram(ID);
        currentShader=this;
    }

    public void disable() {
        glUseProgram(0);
        currentShader=null;
    }

}
