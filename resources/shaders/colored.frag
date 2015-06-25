#version 330 core

layout (location = 0) out vec4 color;

in float resColor;
in float resLength;

void main()
{

    float h=resColor*6;
    while (h>=6.0) h-=6.0;
    float x = 1.0 - abs(mod(h, 2.0) - 1.0);

    if (0.0 <= h && h < 1.0) {
    		color = vec4(1.0, x, 0.0, resLength);
    	} else if (1.0 <= h && h < 2.0) {
    		color = vec4(x, 1.0, 0.0, resLength);
    	} else if (2.0 <= h && h < 3.0) {
    		color = vec4(0.0, 1.0, x, resLength);
    	} else if (3.0 <= h && h < 4.0) {
    		color = vec4(0.0, x, 1.0, resLength);
    	} else if (4.0 <= h && h < 5.0) {
    		color = vec4(x, 0.0, 1.0, resLength);
    	} else if (5.0 <= h && h < 6.0) {
    		color = vec4(1.0, 0.0, x, resLength);
    	} else {
    		color = vec4(0.0, 0.0, 0.0, 1.0);
    	}


}