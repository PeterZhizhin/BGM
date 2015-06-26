#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
	vec2 v_blurTexCoords[14];
} fs_in;

uniform sampler2D tex;

#define PI 3.1415926535897932384626433832795
#define sigma sqrt(0.2)
#define b 0
#define c sigma

float max;
float gauss(float value) {
    float res = exp(-pow(((value-max/2)/max*10-b),2)/(2*c*c));
    return res;
}

void main()
{
    max = fs_in.v_blurTexCoords.length();
	color = vec4(0.0);
	for(int i=0; i<14; i++)
	    color += texture2D(tex,fs_in.v_blurTexCoords[i])*gauss(i);
}