package com.company.Graphics;


import com.company.Utils.Buffers;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {
	
	private int VAO, VBO, IBO, TCBO;
	
	private int count;
	
	public VertexArray(float[] vertices, int[] indices, float[] textureCoordinates){
		count = indices.length;

		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, Buffers.createFloatBuffer(vertices),  GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);

		TCBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, TCBO);
		glBufferData(GL_ARRAY_BUFFER, Buffers.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.UV_COORDS, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.UV_COORDS);

		IBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Buffers.createIntBuffer(indices), GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void bind(){
		glBindVertexArray(VAO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
	}
	
	public void unbind(){
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void draw(){
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
	}
	
	public void render(){
		bind();
		draw();
		unbind();
	}
	
	
	
}
