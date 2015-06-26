#version 330 core

layout (location = 0) out vec4 color;


in vec2 uvCoord;


uniform sampler2D tex;
uniform sampler2D tex2;

uniform vec4 colorTex1;
uniform vec4 colorTex2;


vec3 sum(vec3 a, vec3 b) {
	vec3 res=a+b;
	if (res.r>=1.0) res.r = 1;
	if (res.g>=1.0) res.g = 1;
	if (res.b>=1.0) res.b = 1;
	return res;
}

void main()
{
	vec4 color1 = texture(tex, uvCoord);
	color1.rgb*=colorTex1.rgb;
	vec4 color2 = texture(tex2, uvCoord);
	color2.rgb*=colorTex2.rgb;
	color.rgb = sum(color1.rgb,color2.rgb);
	color.a = color1.a+color2.a;
	color.a/=2;
}