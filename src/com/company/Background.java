package com.company;

import com.company.Effects.Glow;
import com.company.Graphics.*;
import com.company.Math.Matrix4f;

public class Background extends Glow{

    private Matrix4f model;

    public Matrix4f getModel() {
        return model;
    }


    public Background(float x, float y, float width, float height, int sizeX, int sizeY) {
        super(sizeX,sizeY,new Texture("lightmap.png"),
                1,1,1,
                1,1,1);
        model = Matrix4f.translate(x,y,0).multiply(Matrix4f.scale(width,height,1));
    }
}
