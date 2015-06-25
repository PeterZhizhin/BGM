package com.company.Graphics;

import com.company.Math.Matrix4f;

public class Camera {

    private static Matrix4f projection;
    private static Matrix4f view;

    public static void setProjectionMatrix(Matrix4f projection) {
        Camera.projection=projection;
    }

    public static void setViewMatrix(Matrix4f view) {
        Camera.view=view;
    }

    public static void useCamera() {
        if (Shader.getCurrentShader()==null) {
            new Exception("Shader.getCurrentShader() is null").printStackTrace();
            return;
        }
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().viewMatrixUniformId, view);
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().projectionMatrixUniformId, projection);
    }

    public static void useViewCamera() {
        if (Shader.getCurrentShader()==null) {
            new Exception("Shader.getCurrentShader() is null").printStackTrace();
            return;
        }
        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().viewMatrixUniformId, view);
    }

    public static void useProjectionCamera(Matrix4f projection) {
        if (Shader.getCurrentShader()==null) {
            new Exception("Shader.getCurrentShader() is null").printStackTrace();
            return;
        }

        Shader.getCurrentShader().setUniformMat4f(
                Shader.getCurrentShader().projectionMatrixUniformId, projection);
    }
}
