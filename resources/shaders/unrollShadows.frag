#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

vec4 getShadow (float angle, float dist)
{
	float moAngle=angle/6.2831853;
	while (moAngle>1.0) {
		moAngle-=1.0;
	}
	while (moAngle<0) {
		moAngle+=1.0;
	}
	vec4 texColor=texture(tex, vec2(moAngle, 0));
	if (texColor.a>dist && dist>0.25) {
	    vec4 res=texColor;
	    res.a=1.0;
		return res;
	} else return vec4(0.0);
}

void main()
{

	float dx=fs_in.tc.x*2-1;
	float dy=fs_in.tc.y*2-1;

	float angle=atan(dy, dx);
	float length=sqrt(dx*dx+dy*dy);
	float dAngle=0.002f;

	vec4 sum = vec4(0.0);

 	sum += getShadow(angle - 7.0*dAngle, length) * 0.125          *0.125;
    sum += getShadow(angle - 6.0*dAngle, length) * 0.25           *0.125;
    sum += getShadow(angle - 5.0*dAngle, length) * 0.375          *0.125;
 	sum += getShadow(angle - 4.0*dAngle, length) * 0.5            *0.125;
    sum += getShadow(angle - 3.0*dAngle, length) * 0.625          *0.125;
    sum += getShadow(angle - 2.0*dAngle, length) * 0.75           *0.125;
    sum += getShadow(angle - 1.0*dAngle, length) * 0.875          *0.125;
    sum += getShadow(angle, length) * 1.0                         *0.125;
    sum +=  getShadow(angle + 1.0*dAngle, length) * 0.875         *0.125;
    sum +=  getShadow(angle + 2.0*dAngle, length) * 0.75          *0.125;
    sum +=  getShadow(angle + 3.0*dAngle, length) * 0.625         *0.125;
    sum +=  getShadow(angle + 4.0*dAngle, length) * 0.5           *0.125;
    sum +=  getShadow(angle + 5.0*dAngle, length) * 0.375         *0.125;
    sum +=  getShadow(angle + 6.0*dAngle, length) * 0.25          *0.125;
    sum +=  getShadow(angle + 7.0*dAngle, length) * 0.125         *0.125;

	color=sum;
}