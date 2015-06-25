package com.company;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input extends GLFWKeyCallback {
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


    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key>=0 && key<65535) {
            keys[key] = action != GLFW_RELEASE;
            if (action == GLFW_RELEASE) {
                pKeys[key] = true;
            }
        }
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

    public static boolean isKeyUp(int keycode){
        return pKeys[keycode];
    }

    private static DoubleBuffer mouseXBuffer=BufferUtils.createDoubleBuffer(1),
            mouseYBuffer=BufferUtils.createDoubleBuffer(1);

    public static void updateInput(long window, float w, float h) {
        glfwGetCursorPos(window,mouseXBuffer,mouseYBuffer);
        mouseX = mouseXBuffer.get(0)*6/h+5-6*w/h;
        mouseY = 4-(mouseYBuffer.get(0)*6/h-1);//documented feature of glfw - inverted Y axis of mouse

        isMousePressed=1==glfwGetMouseButton(window,GLFW_MOUSE_BUTTON_1);
        wasMousePressed = prevMouseState & !isMousePressed;
        prevMouseState=isMousePressed;
    }
}
