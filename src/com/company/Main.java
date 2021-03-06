package com.company;

import com.company.Audio.OpenALContextHandler;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static com.company.Utils.Utils.checkForGLError;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
public class Main {

    // We need to strongly reference callback instances.
    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long l, int i, int i1, int i2, int i3) {
            Input.keyPressed(l, i, i1, i2, i3);
        }
    };

    private static GLFWFramebufferSizeCallback resizeCallback = new GLFWFramebufferSizeCallback() {
        @Override
        public void invoke(long window, int width, int height) {
            Input.resized(window,width,height);
            windowHeight = height;
            windowWidth  = width;
        }
    };

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
            resizeCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            OpenALContextHandler.destroy();
            glfwTerminate();
            errorCallback.release();
        }
        System.gc();
        System.exit(0);
    }

    private static boolean isFullscreen;
    private static void setCallbacks() {
        glfwSetKeyCallback(window, keyCallback);
        glfwSetFramebufferSizeCallback(window,resizeCallback);
    }
    private static void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
        System.out.println(glfwGetVersionString());

        OpenALContextHandler.create();

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        // Create the window
        isFullscreen = new RunConfigs().getIsFullScreen();
        if (isFullscreen)
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
        setCallbacks();

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        GLContext currentContext = GLContext.createFromCurrent();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glBlendEquation(GL14.GL_FUNC_ADD);


        System.out.println("Supported OpenGL version: " + glGetString(GL_VERSION));

    }

    private static void loop() {

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        initializeWorld();

        long previousUpdateTime = System.currentTimeMillis();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            Input.updateInput(window);
            long currentTime = System.currentTimeMillis();
            update((int) (currentTime - previousUpdateTime));
            previousUpdateTime = currentTime;
            draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

        destroy();

    }

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
