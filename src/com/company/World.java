package com.company;

import com.company.Audio.Sound;
import com.company.GUI.FontRenderer;
import com.company.Graphics.AbstractTexture;
import com.company.Graphics.Camera;
import com.company.Math.Matrix4f;
import com.company.Graphics.Shader;

import java.io.FileNotFoundException;
import java.util.Random;

import static com.company.Utils.Utils.checkForGLError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class World {
    private static Matrix4f ortho;

    private static Note[][] notes;
    private static Note currentPlayingNote = null;

    private static FontRenderer fr=new FontRenderer(0.4f, 0.25f);

    private static void playNote(Note play) {
        if (currentPlayingNote==null) {
            currentPlayingNote = play;
            currentPlayingNote.play();
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
    private static float leftPlaceInLeft;

    public static void init(int widthA,int heightA) {
        width = widthA;
        height = heightA;
        glViewport(0, 0, width, height);
        leftPlaceInLeft = 5 - 6.0f * width / height;
        Camera.setProjectionMatrix(Matrix4f.orthographic(leftPlaceInLeft, 5, -1, 5, -1, 1));
        Camera.setViewMatrix(Matrix4f.IDENTITY);
        notes = new Note[4][4];
        float x, y = 0.5f;
        for (int i = 0; i < 4; i++) {
            x = 0.5f;
            for (int j = 0; j < 4; j++) {
                final int iF = i;
                final int jF = j;
                try {
                    notes[i][j] = new Note(Sound.AUDIO_PATH + String.valueOf(i + 1) + String.valueOf(j + 1) + ".mp3",
                            () -> playNote(notes[iF][jF]), x, y);
                    //Loaded
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                x += 1f;
            }
            y += 1f;
        }

        /*try {
            Sound music = Sound.getSoundUsingJAVE("resources/audio/music.mp3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
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
        //TODO: If SPACE PRESSED -> stopPlaying
        /*if(Input.isKeyDown())
            stopPlaying();*/

    }

    private static final int barsSize=1000;
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

    public static void draw() {

        bench.tick();

        glClearColor(243/256f, 0f, 53/256f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader.defaultShader.enable();


        Camera.useCamera();

        for(Note[] n1 : notes)
            for(Note note : n1)
                note.draw();

        Shader.defaultShader.disable();


        for (int i=0; i<barsSize; i++)
            arr[i]=arr[i]*0.8f+0.2f*rnd.nextFloat();

        for (int i=0; i<blurAmount; i++) {

            float[] blur=new float[barsSize];
            for (int j=0; j<barsSize; j++) {
                blur[j]=
                        arr[getIndexByIndex(j-1, barsSize)]*0.25f+
                        arr[getIndexByIndex(j, barsSize)]*0.5f+
                        arr[getIndexByIndex(j+1, barsSize)]*0.25f;
            }
            System.arraycopy(blur, 0, arr, 0, barsSize);
        }

        AbstractTexture t=br.renderBars(arr);
        checkForGLError();

        Shader.defaultShader.enable();
            Camera.useCamera();

            t.bind();
            Shader.defaultShader.setUniformMat4f(Shader.defaultShader.modelMatrixUniformId,
                    Matrix4f.translate(2.5f, 2.5f, 0.8f));
            BigSquare.draw();
            t.unbind();


            fr.render("РОЦК ", leftPlaceInLeft, 3.2f);
            fr.render("КИНЦО", leftPlaceInLeft, 2.2f);
            fr.render("АУТИЗМ", leftPlaceInLeft, 1.2f);
            fr.render("SP33DC0RE", leftPlaceInLeft, 0.2f);
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
