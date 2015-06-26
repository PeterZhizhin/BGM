#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;

uniform vec4 colorTex1;
uniform vec4 colorTex2;

out vec4  inColor1;
out vec4  inColor2;

out vec2 uvCoord;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	uvCoord = tc;
	inColor1 = colorTex1;
	inColor2 = colorTex2;
}