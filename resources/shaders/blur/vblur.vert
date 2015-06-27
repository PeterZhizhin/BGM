#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;

#define itemsNumber 8

out DATA
{
	vec2 tc;
	vec2 v_blurTexCoords[itemsNumber];
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;

    for (int i=0; i<itemsNumber/2; i++) {
        vs_out.v_blurTexCoords[i] = tc + vec2(0.0, -(itemsNumber/2-i)*0.002);
    }

    for (int i=0; i<itemsNumber/2; i++) {
        vs_out.v_blurTexCoords[i+itemsNumber/2] = tc + vec2(0.0, (i+1)*0.002);
    }

	vs_out.tc = tc;
}