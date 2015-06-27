package com.company;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    public static boolean[] keys = new boolean[65535];
    public static boolean[] pKeys = new boolean[65535];

    public static double mouseX,mouseY;
    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    private static boolean isMousePressed,prevMouseState=false;
    private static boolean wasMousePressed=false;
    public static boolean isMousePressed() {
        return isMousePressed;
    }


    public static boolean isWasMousePressed() {
        return wasMousePressed;
    }


    public static void keyPressed(long window, int key, int scancode, int action, int mods) {
        if (key>=0 && key<65535) {
            keys[key] = action != GLFW_RELEASE;
            if (action == GLFW_RELEASE) {
                pKeys[key] = true;
            }
        }
    }

    public static void resized(long window, int width, int height) {
        System.out.println("Resized "+width+" "+height);
        World.resize(width,height);

    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

    public static boolean isKeyUp(int keycode){
        return pKeys[keycode];
    }

    private static DoubleBuffer mouseXBuffer=BufferUtils.createDoubleBuffer(1),
            mouseYBuffer=BufferUtils.createDoubleBuffer(1);

    public static void updateInput(long window) {
        glfwGetCursorPos(window,mouseXBuffer,mouseYBuffer);
        float[] coords = World.translateScreenToWorld((int)mouseXBuffer.get(0),(int)mouseYBuffer.get(0));
        mouseX = coords[0];
        mouseY = coords[1];

        isMousePressed=1==glfwGetMouseButton(window,GLFW_MOUSE_BUTTON_1);
        wasMousePressed = prevMouseState & !isMousePressed;
        prevMouseState=isMousePressed;
    }
}
