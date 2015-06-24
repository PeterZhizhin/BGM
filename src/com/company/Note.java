package com.company;

import com.company.GUI.Button;
import com.company.Graphics.Shader;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;
import org.lwjgl.Sys;
import sun.security.provider.SHA;

import static com.company.Utils.Utils.checkForGLError;

public class Note extends Button {
    private Matrix4f model;
    private static Texture noteTexture;

    public static void loadTexture() {
        noteTexture = new Texture("note.png");
    }

    public Note(Runnable listener, float x, float y) {
        super(x,y,1,1);
        model = Matrix4f.translate(x,y,0);
        //model = Matrix4f.scale(width,height,1).multiply(Matrix4f.translate(x, y, 0));
        setListener(listener);
        checkForGLError();
    }

    public static void initDraw() {
        noteTexture.bind();
    }

    public void draw() {
        checkForGLError();
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,model
        );
        Square.draw();
    }

    public void draw(float x, float y) {
        checkForGLError();
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId, Matrix4f.translate(x, y, 0)
        );
        Square.draw();


        long ctm=System.currentTimeMillis();

        double startAngle;
        double endAngle;
        boolean drawSector=false;

        if (getIsHOver()) {
            startAngle=0;
            if (selectionStartTime-ctm>selectionTimeInMS) {
                endAngle=Math.PI*2;
            } else {
                endAngle=Math.PI*2*(selectionStartTime-ctm)/selectionTimeInMS;
            }
            drawSector=true;
        } else {




        }


    }

    private static final long selectionTimeInMS = 1000;

    private static long selectionStartTime=0;
    private static long selectionStopTime=0;

    private boolean wasHoverInLastUpdate=false;

    private void startSelection() {
        System.out.println("START SELECTION");
        selectionStartTime=System.currentTimeMillis();

    }

    private void discardSelection() {

        System.out.println("DISCARD SELECTION");
        selectionStopTime=System.currentTimeMillis();
    }

    public void update(int deltaTime) {
        super.update(deltaTime);

        boolean isHover=getIsHOver();
        if (isHover && !wasHoverInLastUpdate) {
            startSelection();
        }   else
            if (!isHover && wasHoverInLastUpdate) {
                discardSelection();
            }
        wasHoverInLastUpdate=isHover;


    }

    public static void disableDraw() {
        noteTexture.unbind();
    }
}
