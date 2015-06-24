package com.company.OpenGL.Shaders;

import com.company.OpenGL.Generators.ShaderGenerator;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class FontShader implements Shader {
    private int programId;

    private int uMatrix;
    public void setMatrix(FloatBuffer matrix) {
        glUniformMatrix4fv(uMatrix, false, matrix);
    }

    private int aPosition;
    public int get_aPosition() { return aPosition; }


    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    private int aTextureCoordinates;
    public int get_aTextureCoordinates() { return aTextureCoordinates; }

    private int uTextureUnit;
    public void setTexture(int textureUnit) {
        glUniform1i(uTextureUnit, textureUnit);
    }

    private int uColor;
    public void setColor(float r, float g, float b, float a) {
        glUniform4f(uColor, r, g, b, a);
    }

    private int uSymbolDimensions;
    public void setSymbolDimensions(float width, float height) {glUniform2f(uSymbolDimensions, width, height);}

    private int uCharPosition;
    public void setCharPosition(float x, float y) {glUniform2f(uCharPosition, x, y);}

    public void use() {
        glUseProgram(programId);
    }

    public void delete() {
        glDeleteProgram(programId);
    }

    public FontShader() {
        programId = ShaderGenerator.createProgram(
                Shader.SHADER_PATH+"font_vertex_shader.glsl",
                Shader.SHADER_PATH+"font_fragment_shader.glsl"
        );
        uMatrix = glGetUniformLocation(programId, "u_Matrix");
        aPosition = glGetAttribLocation(programId, "a_Position");
        aTextureCoordinates = glGetAttribLocation(programId, "a_TextureCoordinates");
        uTextureUnit = glGetUniformLocation(programId, "u_TextureUnit");
        uColor = glGetUniformLocation(programId, "u_Color");
        uSymbolDimensions = glGetUniformLocation(programId, "u_SymbolDimensions");
        uCharPosition = glGetUniformLocation(programId, "u_CharPosition");
    }
}
