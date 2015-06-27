package com.company;

import com.company.Audio.Sound;
import com.company.Effects.Glow;
import com.company.GUI.FontRenderer;
import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Graphics.Texture;
import com.company.Math.Matrix4f;
import com.company.Graphics.Shader;
import org.lwjgl.Sys;
import static org.lwjgl.glfw.GLFW.*;

import java.io.FileNotFoundException;
import java.util.Random;

import static com.company.Utils.Utils.checkForGLError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class World {
    public static final String tempFolderPath = "temp/";

    //BACKGROUND START
    private static BackgroundRenderer background;
    //BACKGROUND END


    private static Matrix4f ortho;

    private static Note[][] notes;
    private static Note currentPlayingNote = null;

    private static FontRenderer fr=new FontRenderer(0.3f, 0.2f);
    private static String[] groupNames = new String[4];

    private static long playingStartTime;
    private static void playNote(Note play) {
        if (currentPlayingNote==null) {
            currentPlayingNote = play;
            currentPlayingNote.play();
            playingStartTime=System.currentTimeMillis();
        }
    }
    private static void stopPlaying() {
        if (currentPlayingNote!=null) {
            currentPlayingNote.stopPlaying();
            currentPlayingNote = null;
        }
    }
    private static int height;
    private static int width;
    private static float leftPlaceInLeft = 5 - 6.0f * 4 / 3;

    public static float[] translateScreenToWorld(int x, int y) {

        float[] res=new float[2];

        if ((float)width/height>=4/3f) {
            float dx=(6.0f*width/height-8)/2;

            float worldWidth=(5 + dx)-(leftPlaceInLeft - dx);

            res[0]=worldWidth*x/width+(leftPlaceInLeft - dx);
            res[1]=4-(6f*y/height-1);
        } else {
            float dy=(8.0f*height/width-6)/2;

            float worldHeight=(5+dy)-(-1-dy);
            float worldWidth=5-leftPlaceInLeft;

            res[0]=worldWidth*x/width+leftPlaceInLeft;
            res[1]=worldHeight-(worldHeight*y/height) +(-1-dy);
        }

        return res;
    }

    public static void resize(int widthN, int heightN) {
        width = widthN; height = heightN;
        if ((float)widthN/heightN>=4/3f) {
            float dx=(6.0f*widthN/heightN-8)/2;
            Camera.setProjectionMatrix(Matrix4f.orthographic(leftPlaceInLeft - dx, 5 + dx, -1, 5, -1, 1));
        } else {
            float dy=(8.0f*heightN/widthN-6)/2;
            Camera.setProjectionMatrix(Matrix4f.orthographic(leftPlaceInLeft, 5, -1-dy, 5+dy, -1, 1));
        }

    }

    public static void init(int widthA,int heightA) {
        width = widthA;
        height = heightA;
        glViewport(0, 0, width, height);
        resize(widthA, heightA);
        Camera.setViewMatrix(Matrix4f.IDENTITY);
        notes = new Note[4][4];
        PlayList playList = new PlayList();
        for (int i = 0; i < 4; i++) {
            groupNames[i] = playList.getGroup(i);
        }
        float x =0.5f;
        float y = 3.5f;
        for (int i = 0; i < 4; i++) {
            x = 0.5f;
            for (int j = 0; j < 4; j++) {
                final int iF = i;
                final int jF = j;
                Track currentTrack = playList.getTrack(i,j);
                notes[i][j] = new Note(currentTrack.getUrl(),currentTrack.getPrice(),
                        () -> playNote(notes[iF][jF]), x, y);
                x += 1f;
            }
            y -= 1f;
        }
        background = new BackgroundRenderer(Matrix4f.translate((leftPlaceInLeft+5)/2,2,0).multiply(Matrix4f.scale(5-leftPlaceInLeft,6,1)),
                   "lightmap.png","bluredlightmap.png");
        //((leftPlaceInLeft+5)/2,2,5-leftPlaceInLeft, 6, widthA, heightA);
    }

    public static void update(int deltaTime) {
        boolean mousePressedRightNow=Input.isWasMousePressed();
        if (mousePressedRightNow) {
            System.out.println("PRESSED: "+Input.getMouseX()+" :: "+Input.getMouseY());
        }
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                notes[i][j].update(deltaTime);
            }
        }



        if (currentPlayingNote!=null) {


            currentVolume=Math.max(
                    0.2f+currentPlayingNote.getCurrentVolume()*0.8f,
                    currentVolume-deltaTime/1000f
                    );

            //System.out.println(currentPlayingNote.getCurrentVolume());

            currentPlayingNote.changeAngle((float) (deltaTime*2*Math.PI/1000));

            float[] fft = currentPlayingNote.getFFT();
            if (fft != null) {
                for (int i = 0; i < barsSize; i++) {
                    //arr[i] = Math.max(arr[i]-8.0f*deltaTime/1000, arr[i] * 0.9f + 0.1f * fft[i]);
                    arr[i] = Math.max(arr[i]-3.0f*deltaTime/1000, arr[i] * 0.9f + 0.1f * fft[i]);
                }

                for (int i = 0; i < blurAmount; i++) {

                    float[] blur = new float[barsSize];
                    for (int j = 0; j < barsSize; j++) {
                        blur[j] =
                                arr[getIndexByIndex(j - 1, barsSize)] * 0.25f +
                                        arr[getIndexByIndex(j, barsSize)] * 0.5f +
                                        arr[getIndexByIndex(j + 1, barsSize)] * 0.25f;
                    }
                    System.arraycopy(blur, 0, arr, 0, barsSize);
                }
            }
            else {
                for (int j=0; j<arr.length; j++)
                    arr[j]=0;
            }
        }             else {
            currentVolume=0.2f;
        }


        if(Input.isKeyDown(GLFW_KEY_SPACE))
            stopPlaying();

    }

    private static final int barsSize=424;
    private static final int blurAmount=1;
    private static BarsRenderer br=new BarsRenderer(barsSize);

    private static final Random rnd=new Random();
    private static final float[] arr=new float[barsSize];

    /**
     * Returns the index by given index
     * @param index given index
     * @param limit the limitation of index
     * @return index
     */
    private static int getIndexByIndex(int index, int limit) {
        while (index<0)
            index+=limit;
        index%=limit;
        return index;
    }

    private static Benchmark bench=new Benchmark("World.draw", true);

    private static float currentVolume;

    public static void draw() {

        bench.tick();

        glClearColor(0f,0f,0f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        checkForGLError();



        background.render(currentVolume,currentVolume,currentVolume*4);
        checkForGLError();

        Shader.defaultShader.enable();
        for(Note[] n1 : notes)
            for(Note note : n1)
            if (note!=currentPlayingNote)
                note.draw();

        Shader.defaultShader.disable();


        if (currentPlayingNote!=null) {

            AbstractTexture t = br.renderBars(arr);
            checkForGLError();


            Shader.defaultShader.enable();

            Camera.useCamera();
            t.bind();
            Shader.defaultShader.setUniformMat4f(Shader.defaultShader.modelMatrixUniformId,
                    Matrix4f.translate(currentPlayingNote.getX(), currentPlayingNote.getY(), 0.8f)
            );
            BigSquare.draw();
            t.unbind();

            currentPlayingNote.draw();

            Shader.defaultShader.disable();

        }

        Shader.defaultShader.enable();
            Camera.useCamera();

            float y=3.2f;
            for (String groupName : groupNames) {
                fr.render(groupName, leftPlaceInLeft, y);
                y -= 1;
            }
        Shader.defaultShader.disable();

        bench.tack();

    }

    public static void destroy() {
        for (Note[] n1 : notes)
            for (Note n : n1)
                n.destroy();

    }



    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
}
