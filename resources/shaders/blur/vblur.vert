#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;

out DATA
{
	vec2 tc;
	vec2 v_blurTexCoords[14];
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;

	for(int i=0; i<14; i++) {
	    vs_out.v_blurTexCoords[i] = tc+vec2(0,(i-14)/100);
	}
	vs_out.tc = tc;
}