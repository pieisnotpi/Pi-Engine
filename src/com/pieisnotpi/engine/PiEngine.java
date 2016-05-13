package com.pieisnotpi.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class PiEngine
{
    // Current game instance
    public static GameInstance instance;
    // Matrix IDs
    public static final int ORTHO_ID = 0, CAMERA_3D_ID = 1, CAMERA_2D_ID = 2;
    // Shader IDs (initialized in Window)
    public static int TEXTURE_ID, COLOR_ID, TEXT_ID, TEXTURE_C;
    // Rendering to physics coordinate conversion
    public static final float PIXELS_PER_METER = (0.1f/2);
    // Current monitor
    public static int monitor = 0;
    // Debug mode
    public static boolean debug = false;
    // GL Version
    public static int glMajor = -1, glMinor = -1;
    // GL Capabilities
    public static GLCapabilities capabilities;

    /**
     * Initializes the Pi Engine
     * @param inst A game instance, necessary for the engine to work
     */

    public static void start(GameInstance inst)
    {
        assert inst != null;

        GLFWErrorCallback errorCallback;
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        long window = glfwCreateWindow(100, 100, "", NULL, NULL);
        glfwMakeContextCurrent(window);
        capabilities = GL.createCapabilities();

        String glVersion = glGetString(GL_VERSION);
        int index = glVersion.indexOf('.');
        glMajor = Integer.parseInt(glVersion.substring(0, index));
        glMinor = Integer.parseInt(glVersion.substring(index + 1, glVersion.indexOf('.', index + 1)));

        if(glMajor == 1) throw new IllegalStateException("OpenGL version 2.0 not supported, program will not work");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, PiEngine.glMajor);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, PiEngine.glMinor);

        if(glMajor > 3 || (glMajor == 3 && glMinor > 1))
        {
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }

        glfwDestroyWindow(window);

        instance = inst;
        instance.init();
        instance.start();

        errorCallback.free();
        glfwTerminate();
    }
}
