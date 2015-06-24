#version 330 core

//Позиция
layout(location=0) in vec4 a_Position;
//Позиция в текстуре
layout(location=1) in vec2 a_TextureCoordinates;
//Для передачи фрагментному шейдеру
out vec2 v_TextureCoordinates;

//Матрица для преобразования в СК OpenGL
uniform mat4 u_Matrix;

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * a_Position;
}