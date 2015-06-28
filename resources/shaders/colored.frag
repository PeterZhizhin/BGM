#version 330 core

layout (location = 0) out vec4 outColor;

//in float resColor;
//in float resLength;


uniform float color;
uniform float length;


void main()
{

    float h=color*6;
    while (h>=6.0) h-=6.0;
    float x = 1.0 - abs(mod(h, 2.0) - 1.0);

    if (0.0 <= h && h < 1.0) {
    		outColor = vec4(1.0, x, 0.0, length);
    	} else if (1.0 <= h && h < 2.0) {
    		outColor = vec4(x, 1.0, 0.0, length);
    	} else if (2.0 <= h && h < 3.0) {
    		outColor = vec4(0.0, 1.0, x, length);
    	} else if (3.0 <= h && h < 4.0) {
    		outColor = vec4(0.0, x, 1.0, length);
    	} else if (4.0 <= h && h < 5.0) {
    		outColor = vec4(x, 0.0, 1.0, length);
    	} else if (5.0 <= h && h < 6.0) {
    		outColor = vec4(1.0, 0.0, x, length);
    	} else {
    		outColor = vec4(0.0, 0.0, 0.0, 1.0);
    	}


}