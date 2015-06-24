package com.company;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
public class Main {

    // We need to strongly reference callback instances.
    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback keyCallback;

    // The window handle
    private static long window;

    private static void run() {
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }

    private static void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // Create the window
        if (IS_FULLSCREEN)
        {
            ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            windowWidth = GLFWvidmode.width(vidmode);
            windowHeight = GLFWvidmode.height(vidmode);
            window = glfwCreateWindow(windowWidth,windowHeight,"Hello World!", glfwGetPrimaryMonitor(), NULL);
        }
        else
        {
            windowWidth = DESIRED_WIDTH;
            windowHeight = DESIRED_HEIGHT;
            window = glfwCreateWindow(DESIRED_WIDTH, DESIRED_HEIGHT, "Hello World!", NULL, NULL);
        }
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
            }
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }

    private static void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        initializeWorld();

        long previousUpdateTime = System.currentTimeMillis();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            Input.updateInput(window, windowWidth, windowHeight);
            long currentTime = System.currentTimeMillis();
            update((int)(currentTime- previousUpdateTime));
            previousUpdateTime = currentTime;
            draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

        destroy();

    }

    private static final boolean IS_FULLSCREEN = false;
    private static final int DESIRED_WIDTH = 1024;
    private static final int DESIRED_HEIGHT = 768;
    private static int windowWidth,windowHeight;

    private static void initializeWorld() {
        World.init(windowWidth,windowHeight);
    }

    private static void update(int deltaTime) {
        World.update(deltaTime);
    }

    private static void draw() {
        World.draw();
    }

    private static void destroy() {
        World.destroy();
    }

    public static void main(String[] args) {
        run();
    }
}
