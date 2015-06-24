package com.company.OpenGL.Shaders;


import com.company.OpenGL.Generators.ShaderGenerator;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class TextureShader implements Shader  {

    private int programId;

    private int uMatrix;
    public void setMatrix(FloatBuffer matrix) {
        glUniformMatrix4fv(uMatrix, false, matrix);
    }

    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    private int uTextureUnit;
    public void setTexture(int textureUnit) {
        glUniform1i(uTextureUnit, textureUnit);
    }

    private int uColor;
    public void setColor(float r,float g,float b, float a) {
        glUniform4f(uColor, r, g, b, a);
    }

    public void use() {
        glUseProgram(programId);
    }

    public void delete() {
        glDeleteProgram(programId);
    }

    public TextureShader() {
        programId = ShaderGenerator.createProgram(
                Shader.SHADER_PATH+"texture_vertex_shader.glsl",
                Shader.SHADER_PATH+"texture_fragment_shader.glsl"
        );
        uMatrix = glGetUniformLocation(programId, "u_Matrix");
        uTextureUnit = glGetUniformLocation(programId, "u_TextureUnit");
        uColor = glGetUniformLocation(programId, "u_Color");
    }

}
