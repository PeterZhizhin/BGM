package com.company.Graphics;

import com.company.Utils.Buffers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static com.company.Utils.Utils.checkForGLError;
import static com.company.Utils.Utils.processError;
import static org.lwjgl.opengl.GL11.*;

public class Texture extends AbstractTexture {

	public Texture(String relativePath) {
		super(load(AbstractTexture.TEXTURE_PATH+relativePath));
		checkForGLError();
		System.out.println(AbstractTexture.TEXTURE_PATH+relativePath+" loaded!");
	}



	private static int load(String path){
		int[] pixels = null;
		int width=0;
		int height=0;
		try{
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0,width, height, pixels, 0, width);
		} catch (IOException e){
			processError(e);
		}
		
		int[] data = new int[width * height];
		for(int i = 0; i < width * height; i++){
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffers.createIntBuffer(data));

		glBindTexture(GL_TEXTURE_2D, 0);
		checkForGLError();
		return result;
	}

	public void dispose() {
		glDeleteTextures(getTextureId());
	}

}
