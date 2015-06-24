package com.company;

import com.company.OpenGL.Shaders.Shader;
import com.company.Utils.Buffers;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;

public class Square {

    private static final FloatBuffer vertexes = Buffers.createFloatBuffer(new float[] {
            //Stride = 4*sizeof(float)
            //Vertex(0 pointer) Texture(2*sizeof(float) pointer)
            -0.5f, -0.5f,       0f, 1f,
            -0.5f, 0.5f,        0f, 0f,
            0.5f, 0.5f,         1f, 0f,
            0.5f,-0.5f,         1f, 1f,
    });

    private static int vertexArrayObject;
    private static int vertexBufferObject;
    private static int textureCoordinatesBuffer;
    public static void bind() {
        glBindVertexArray(vertexArrayObject);
    }
    public static void draw() {
        glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_INT, 0);
    }
    public static void initVAO() {
        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);


        vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBufferData(GL_ARRAY_BUFFER,vertexes,GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 2, GL_FLOAT,false,4*Buffers.FLOAT_BYTE_SIZE,0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);


        textureCoordinatesBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,textureCoordinatesBuffer);
        glBufferData(GL_ARRAY_BUFFER,vertexes,GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.UV_COORDS, 2, GL_FLOAT,false,4*Buffers.FLOAT_BYTE_SIZE,2*Buffers.FLOAT_BYTE_SIZE);
        glEnableVertexAttribArray(Shader.UV_COORDS);


        glBindBuffer(GL_ARRAY_BUFFER,0);
        glBindVertexArray(0);
    }
    public static void destroy() {
        glDeleteBuffers(vertexBufferObject);
        glDeleteBuffers(textureCoordinatesBuffer);
        glDeleteVertexArrays(vertexArrayObject);
    }


}
