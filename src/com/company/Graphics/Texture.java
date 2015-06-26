package com.company.Graphics;

import com.company.Utils.Buffers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.FileInputStream;
import java.io.IOException;

import static com.company.Utils.Utils.checkForGLError;
import static com.company.Utils.Utils.processError;
import static org.lwjgl.opengl.GL11.*;

public class Texture extends AbstractTexture {

	private static int[] sizes=new int[2];


	public Texture(String relativePath) {
		super(load(AbstractTexture.TEXTURE_PATH+relativePath, sizes));
		width=sizes[0];
		height=sizes[1];
		checkForGLError();
		System.out.println(AbstractTexture.TEXTURE_PATH+relativePath+" loaded!");
	}

	public Texture(String relativePath, int radius) {
		super(loadBlurred(AbstractTexture.TEXTURE_PATH+relativePath, sizes, radius));
		width=sizes[0];
		height=sizes[1];
		checkForGLError();
		System.out.println(AbstractTexture.TEXTURE_PATH+relativePath+" loaded!");
	}

	private static float[] createBlurMatrix(int radius) {
		int square = radius*radius;
		float sum = 0;
		float[] matrix = new float[square];
		for (int i = 0; i < square; i++) {
			int dx = i % radius - radius / 2;
			int dy = i / radius - radius / 2;
			matrix[i] = (float) (radius - Math.sqrt(dx * dx + dy * dy));
			sum += matrix[i];
		}
		for (int i = 0; i < square; i++) {
			matrix[i] /= sum;
		}
		return matrix;
	}


	private int width=0;
	private int height=0;

	private static int loadBlurred(String path, int[] sizesCallback, int blurRadius){
		int[] pixels = null;
		try{
			BufferedImage image = ImageIO.read(new FileInputStream(path));

			BufferedImageOp op=new ConvolveOp(new Kernel(
					blurRadius ,blurRadius, createBlurMatrix(blurRadius)
			), ConvolveOp.EDGE_NO_OP, null)     ;
			image = op.filter(image,null);

			sizesCallback[0] = image.getWidth();
			sizesCallback[1] = image.getHeight();
			pixels = new int[sizesCallback[0] * sizesCallback[1]];
			image.getRGB(0, 0,sizesCallback[0], sizesCallback[1], pixels, 0, sizesCallback[0]);
		} catch (IOException e){
			processError(e);
		}

		int[] data = new int[sizesCallback[0] * sizesCallback[1]];
		for(int i = 0; i < sizesCallback[0] * sizesCallback[1]; i++){
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

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sizesCallback[0], sizesCallback[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffers.createIntBuffer(data));

		glBindTexture(GL_TEXTURE_2D, 0);
		checkForGLError();
		return result;
	}

	private static int load(String path, int[] sizesCallback){
		int[] pixels = null;
		try{
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			sizesCallback[0] = image.getWidth();
			sizesCallback[1] = image.getHeight();
			pixels = new int[sizesCallback[0] * sizesCallback[1]];
			image.getRGB(0, 0,sizesCallback[0], sizesCallback[1], pixels, 0, sizesCallback[0]);
		} catch (IOException e){
			processError(e);
		}
		
		int[] data = new int[sizesCallback[0] * sizesCallback[1]];
		for(int i = 0; i < sizesCallback[0] * sizesCallback[1]; i++){
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
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sizesCallback[0], sizesCallback[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffers.createIntBuffer(data));

		glBindTexture(GL_TEXTURE_2D, 0);
		checkForGLError();
		return result;
	}

	public void dispose() {
		glDeleteTextures(getTextureId());
	}

	@Override
	public int getWidthInPX() {
		return 0;
	}

	@Override
	public int getHateInPX() {
		return 0;
	}

}
