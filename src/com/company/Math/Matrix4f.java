package com.company.Math;

import java.nio.FloatBuffer;

import static com.company.Utils.Buffers.createFloatBuffer;

public class Matrix4f {

	public static final Matrix4f IDENTITY=identity();

	public static final int SIZE = 4 * 4;
	private float[] elements = new float[SIZE];

	private static long matricesAlive=0;
	private static long matricesTotal=0;

	public Matrix4f(){
		matricesAlive++;
		matricesTotal++;
	}

	public void finalize() {
		matricesAlive--;
	}
	
	public static Matrix4f identity(){
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 0 * 4] = 1.0f;
		matrix.elements[1 + 1 * 4] = 1.0f;
		matrix.elements[2 + 2 * 4] = 1.0f;
		matrix.elements[3 + 3 * 4] = 1.0f;

		return matrix;
	}

	public static Matrix4f scale(float scaleX, float scaleY, float scaleZ){
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 0 * 4] = scaleX;
		matrix.elements[1 + 1 * 4] = scaleY;
		matrix.elements[2 + 2 * 4] = scaleZ;
		matrix.elements[3 + 3 * 4] = 1.0f;

		return matrix;
	}



	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
		Matrix4f matrix = new Matrix4f();
		
		matrix.elements[0 + 0 * 4] = 2.0f / (right - left);
		matrix.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		matrix.elements[2 + 2 * 4] = 2.0f / (near - far);

		matrix.elements[0 + 3 * 4] = (left + right) / (left - right);
		matrix.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		matrix.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		matrix.elements[3 + 3 * 4] = 1.0f;
		
		return matrix;
	}

	public static Matrix4f translate(float x, float y, float z){
		Matrix4f matrix = identity();
		matrix.elements[0 + 3 * 4] = x;
		matrix.elements[1 + 3 * 4] = y;
		matrix.elements[2 + 3 * 4] = z;
		return matrix;
	}

	private static Matrix4f[] rotations=new Matrix4f[1000];
	static {
		for (int i=0; i<1000; i++) {
			rotations[i]=rotate((float) (2*Math.PI*i/1000));
		}
	}

	public static Matrix4f getRotated(float angle) {
		float normalised= (float) (angle/Math.PI/2);
		while (normalised<0)
			normalised++;
		normalised%=1;
		return rotations[((int) (normalised * 1000))];
	}

	public static Matrix4f rotate(float angle){
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		
		matrix.elements[0 + 0 * 4] = cos;
		matrix.elements[1 + 0 * 4] = sin;
		matrix.elements[0 + 1 * 4] = -sin;
		matrix.elements[1 + 1 * 4] = cos;		
		return matrix;
	}

	public Matrix4f multiply(Matrix4f matrix){
		Matrix4f result = new Matrix4f();
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				float sum = 0.0f;
				for(int e = 0; e < 4; e++){
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
				}
				result.elements[x + y * 4] = sum;
			}
		}
		return result;
	}

	private FloatBuffer floatBuffer=null;
	static long invokedTimes=0;
	static long nonCachedTimes=0;
	public FloatBuffer toFloatBuffer() {
		invokedTimes++;
		if (floatBuffer==null) {
			nonCachedTimes++;
			floatBuffer = createFloatBuffer(elements);
			elements=null;
		}
		if (invokedTimes%1000000==0) {
			System.out.println("Matrix.toFloatBuffer invoked "+invokedTimes+" times,\n" +
					"	converted to FloatBuffer "+nonCachedTimes+" times\n" +
					"	alive "+matricesAlive+" matrices\n"+
					"	total created "+matricesTotal+" matrices");
		}
		return floatBuffer;
	}

	public Matrix4f clone() {
		Matrix4f copy=new Matrix4f();
		System.arraycopy(elements, 0, copy.elements, 0, SIZE);
		return copy;
	}
	
	
}
