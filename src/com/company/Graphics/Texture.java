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
	public static final String TEXTURE_PATH = "resources/textures/";

	private static int[] sizes=new int[2];

	private int width=0;
	private int height=0;

	public Texture(BufferedImage image) {
		super(load(image, sizes));
		width = sizes[0];
		height=sizes[1];
		checkForGLError();
	}

	public Texture(String relativePath) {
		this(load(TEXTURE_PATH + relativePath));
		System.out.println(TEXTURE_PATH+relativePath+" loaded!");
	}

	private static int load(BufferedImage image, int[] sizesCallback) {
		int[] pixels;
		sizesCallback[0] = image.getWidth();
		sizesCallback[1] = image.getHeight();
		pixels = new int[sizesCallback[0] * sizesCallback[1]];
		image.getRGB(0, 0,sizesCallback[0], sizesCallback[1], pixels, 0, sizesCallback[0]);

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

	private static BufferedImage load(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new FileInputStream(path));
		} catch (IOException e) {
			processError(e);
		}
		return image;
	}

	protected void dispose() {
		glDeleteTextures(getTextureId());
	}

	@Override
	public int getWidthInPX() {
		return width;
	}

	@Override
	public int getHateInPX() {
		return height;
	}

}
