package com.company.OpenGL.Shaders;


import com.company.OpenGL.Generators.ShaderGenerator;
import com.company.OpenGL.Shaders.Shader;

import static org.lwjgl.opengl.GL20.*;

public class FillBitmapShader {

    private int programId;

    private int aPosition;
    public int get_aPosition() { return aPosition; }

    private int aTexturePosition;
    public int get_aTexturePosition() { return aTexturePosition; }

    private int uTextureDimensions;
    public void setTextureDimensions(float x, float y) {glUniform2f(uTextureDimensions, x, y);}

    private int uDX;
    public void setDX(float dx) {glUniform1f(uDX, dx);}

    private int uOpacity;
    public void setOpacity(float opacity) {
        glUniform1f(uOpacity, opacity);
    }

    private int uTextureUnit;
    public void setTexture(int textureUnit) {
        glUniform1i(uTextureUnit, textureUnit);
    }

    public void use() {
        glUseProgram(programId);
    }

    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    public void delete() {
        glDeleteProgram(programId);
    }

    public FillBitmapShader() {
        programId = ShaderGenerator.createProgram(Shader.SHADER_PATH+"fillbitmap_vertex_shader.glsl",
                Shader.SHADER_PATH+"fillbitmap_fragment_shader.glsl");
        aPosition = glGetAttribLocation(programId, "a_Position");
        aTexturePosition = glGetAttribLocation(programId, "a_TexturePosition");
        uTextureUnit = glGetUniformLocation(programId, "u_TextureUnit");
        uTextureDimensions = glGetUniformLocation(programId, "u_TextureDimensions");
        uOpacity = glGetUniformLocation(programId, "u_Opacity");
        uDX = glGetUniformLocation(programId, "u_DX");

    }

}
