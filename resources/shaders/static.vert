
#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;

out vec2 uvCoord;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	uvCoord = tc;
}