package com.company;

import com.company.Audio.FFT;
import com.company.Audio.Sound;
import com.company.GUI.Button;
import com.company.Graphics.Shader;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;
import org.lwjgl.Sys;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static com.company.Utils.Utils.checkForGLError;

public class Note extends Button {

    private Matrix4f model;//matrix contains floatbuffer, not float[]
    private Matrix4f position;//not-cleanable matrix for multiplying

    private static final Texture noteTexture= new Texture("note.png");
    private static final Texture tickTexture= new Texture("angle.png");

    private Thread loadSound;
    private boolean isLoading;
    private void loadedCallback() {
        sound.playSound();
        isLoading = false;
        loadSound = null;
    }
    private Sound sound;

    public Note(String soundPath, Runnable listener, float x, float y) throws FileNotFoundException {
        super(x,y,1,1);
        loadSound = new Thread(() -> {
                try {
                    sound = Sound.getSoundUsingJAVE(soundPath);
                } catch (FileNotFoundException e) {
                    System.err.println("File "+soundPath+" not found!");
                    e.printStackTrace();
                }
                System.out.println(soundPath+" loaded!");
            loadedCallback();

        });
        model = Matrix4f.translate(x,y,0);
        position=model.clone();
        //model = Matrix4f.scale(width,height,1).multiply(Matrix4f.translate(x, y, 0));
        setListener(listener);
        checkForGLError();
    }

    public void play() {
        loadSound.run();
        System.out.println("Start loading sound");
    }

    private static float getNoteByFrequence(float frequency) {
        return (float) (96*Math.log(frequency/27.5)/Math.log(2));
    }

    public float[] getFFT() {
        int[][] samples=sound.getNextSamples(1024);
        if (samples==null) {
            return null;
        }
        float[] fft=FFT.fft(samples[0]);

        float[] data=new float[424];
        for (int i=0; i<data.length; i++)
            data[i]=0;
        for (int i=0; i<512; i++) {
            float note=getNoteByFrequence(i+27.5f);

            int floor= (int) Math.floor(note);
            float a= note-floor;
            float b=1f-a;

            if (floor<data.length) {
                data[floor]+=fft[i]*a;
            } else {
                System.err.println("Used note "+floor);
            }
            if (floor+1<data.length) {
                data[floor + 1] += fft[i] * b;
            } else {
                System.err.println("Used note "+floor);
            }
        }

        for (int i=0; i<data.length; i++) {
            data[i]*=(0.4f+i*0.6f/data.length);
        }

        for (int i=0; i<data.length; i++) {
            data[i]*=0.001f;
        }

        float max=0;
        for (int i=0; i<data.length; i++) {
            max=Math.max(data[i], max);
        }

        if (max>2) {
            for (int i=0; i<data.length; i++) {
                data[i]*=2/max;
            }
        }

        return data;
    }


    public void stopPlaying() {sound.stopSound();}

    private static float function(double sourceValue) {
        return (float) (2*Math.PI*(Math.sin(sourceValue/2-Math.PI/2)/2+0.5));
    }

    private void drawSelection() {
        double[] angles= getSegmentAngles();
        if (angles!=null) {

            int from= (int) (function(angles[0])*segmentsNumber/2/Math.PI);
            int to= (int) (function(angles[1])*segmentsNumber/2/Math.PI);

            tickTexture.bind();

            for (int i=from; i<to; i++) {

                Shader.getCurrentShader().setUniformMat4f(
                        Shader.getCurrentShader().modelMatrixUniformId,
                        position.multiply(Matrix4f.getRotated((float) (i * 2 * Math.PI / segmentsNumber)))
                );
                Square.draw();

            }
            tickTexture.unbind();
        }
    }

    public void draw() {
        checkForGLError();

        noteTexture.bind();
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,model
        );
        Square.draw();
        noteTexture.unbind();

        drawSelection();
    }

    public void draw(float angle) {
        checkForGLError();

        noteTexture.bind();
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().modelMatrixUniformId,position.multiply(Matrix4f.getRotated(angle))
        );
        Square.draw();
        noteTexture.unbind();

        drawSelection();


    }

    public double[] getSegmentAngles() {
        long ctm=System.currentTimeMillis();

        double startAngle=0;
        double endAngle=0;
        boolean drawSector=false;

        if (ctm-selectionStartTime>selectionTimeInMS) {
            endAngle=Math.PI*2;
        } else {
            endAngle=Math.PI*2*(ctm-selectionStartTime)/selectionTimeInMS;
        }

        if (getIsHOver()) {
            startAngle=0;
            drawSector=true;
        } else {
            if (ctm-selectionStopTime<selectionTimeInMS) {
                startAngle=Math.PI*2*(ctm-selectionStopTime)/selectionTimeInMS;
                drawSector=true;
            }
        }

        if (drawSector) {
            return new double[]{startAngle, endAngle};
        }
        return null;
    }


    private static final long segmentsNumber = 50;
    private static final long selectionTimeInMS = 800;

    private long selectionStartTime=0;
    private long selectionStopTime=0;

    private boolean wasHoverInLastUpdate=false;

    private void startSelection() {
        selectionStartTime=System.currentTimeMillis();

    }

    private void discardSelection() {
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

    public void destroy() {
        if (sound!=null)
            sound.dispose();
    }
}
