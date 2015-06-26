#version 330 core

layout (location = 0) out vec4 color;

in vec2 uvCoord;

uniform sampler2D tex;

void main()
{
	color = texture(tex, uvCoord);
	color.rgb*=color.a;
}