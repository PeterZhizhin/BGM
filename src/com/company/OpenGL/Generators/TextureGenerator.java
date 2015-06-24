package com.company.OpenGL.Generators;


import com.company.Utils.Buffers;
import com.company.Utils.BicycleDebugger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureGenerator {
    private static final String TAG = "TextureGenerator";
    public static final String TEXTURE_PATH = "resources/textures/";

    public static int loadTextureFromRes(String relativeName, boolean isInfinite) {
        return loadTexture(TEXTURE_PATH+relativeName, isInfinite);
    }

    public static int loadTexture(String filePath, boolean isInfinite) {
        try {
            return loadTexture(ImageIO.read(new FileInputStream(filePath)),isInfinite);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int loadTexture(BufferedImage bitmap, boolean isInfinite) {
        int textureObjectId = glGenTextures();
        if (textureObjectId==0) {
            BicycleDebugger.w(TAG, "Could not generate new a new OpenGL texture object.");
            return 0;
        }

        //Загружаем текстуру
        glBindTexture(GL_TEXTURE_2D, textureObjectId);

        //Настраиваем стандартные параметры отрисовки
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        if (isInfinite)
        {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        }
        else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        //Загружаем Bitmap
        texImage2D(bitmap);

        //Генерирем Mipmap
        glGenerateMipmap(GL_TEXTURE_2D);

        //Сбрасываем текстуру так как действия с ней пока закончились
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectId;

    }

    private static void texImage2D(BufferedImage bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixels[] = new int[width * height];
        bitmap.getRGB(0, 0,width, height, pixels, 0, width);

        int[] data = new int[width * height];
        for(int i = 0; i < width * height; i++){
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffers.createIntBuffer(data));
    }

    public static int[] loadTextures(BufferedImage[] bitmaps) {
        int[] result = new int[bitmaps.length];

        for (int i=0; i<result.length; i++)
            result[i] = loadTexture(bitmaps[i], false);

        return result;
    }


}
