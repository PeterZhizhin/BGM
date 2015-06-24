#version 330 core

uniform sampler2D u_TextureUnit;
in vec2 v_TextureCoordinates;
uniform vec4 u_Color;

out vec4 color;

void main()
{
    //Задаем текстурные координаты
    color = texture2D(u_TextureUnit, v_TextureCoordinates);
    //Задаем прозрачность
    color *= u_Color;
    color.rgb *= u_Color.a;
}