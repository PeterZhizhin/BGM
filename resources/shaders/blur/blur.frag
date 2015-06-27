#version 330 core

layout (location = 0) out vec4 color;

#define itemsNumber 8

in DATA
{
	vec2 tc;
	vec2 v_blurTexCoords[itemsNumber];
} fs_in;

uniform sampler2D tex;

void main()
{

	color = vec4(0.0);

    float sum=0.0;
    for (int i=0; i<itemsNumber/2; i++) {
          sum+=1+i;
    }
    sum*=2;
    sum-=itemsNumber;

    for (int i=0; i<itemsNumber/2; i++) {
        color += texture(tex, fs_in.v_blurTexCoords[ i])*(i+1)/sum;
    }
    color += texture(tex, fs_in.tc                 )*itemsNumber/2/sum;
    for (int i=0; i<itemsNumber/2; i++) {
        color += texture(tex, fs_in.v_blurTexCoords[ itemsNumber-i-1])*(i+1)/sum;
    }

}