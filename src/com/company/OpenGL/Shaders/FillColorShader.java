package com.company.OpenGL.Shaders;

import com.company.OpenGL.Generators.ShaderGenerator;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class FillColorShader implements Shader {

    private int programId;
    public void use() {
        glUseProgram(programId);
    }

    public void delete() {
        glDeleteProgram(programId);
    }

    private int uMatrix;
    public void setMatrix(FloatBuffer matrix) {
        glUniformMatrix4fv(uMatrix, false, matrix);
    }

    private int aPosition;
    public int get_aPosition() { return aPosition; }

    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    private int uColor;
    public void setColor(float r, float g, float b, float a) {
        glUniform4f(uColor, r, g, b, a);
    }

    public FillColorShader() {
        programId = ShaderGenerator.createProgram(
                Shader.SHADER_PATH+"fillcolor_vertex_shader.glsl",
                Shader.SHADER_PATH+"fillcolor_fragment_shader.glsl"
        );
        uMatrix = glGetUniformLocation(programId, "u_Matrix");
        aPosition = glGetAttribLocation(programId, "a_Position");
        uColor = glGetUniformLocation(programId, "u_Color");
    }




}
