package com.company;

import com.company.Graphics.VertexArray;

public class BigSquare {
    public static float width = 4f;
    public static float height = 4f;

    private static final float[] vertices = new float[]{
            -width/2, -height/2, 0.2f,
            -width/2,  height/2, 0.2f,
            width/2,  height/2, 0.2f,
            width/2, -height/2, 0.2f
    };

    private static final float[] texCoords = new float[]{
            0, 1,
            0, 0,
            1, 0,
            1, 1
    };

    private static final int[] indices = new int[]{
            0,1,2,
            2,3,0
    };

    private static final VertexArray vertexArrayObject = new VertexArray(vertices,indices,texCoords);

    public static void draw() {
        vertexArrayObject.render();
    }
}
