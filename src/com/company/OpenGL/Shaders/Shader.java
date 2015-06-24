package com.company.OpenGL.Shaders;

import java.nio.FloatBuffer;

public interface Shader {

    void setMatrix(FloatBuffer matrix);
    void validate();
    void use();
    void delete();

    int VERTEX_ATTRIB = 0;
    int UV_COORDS = 1;
    String SHADER_PATH="resources/shaders/";



}
