package com.pieisnotpi.engine;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Monitor;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class PiEngine
{
    // GLFW monitor pointers
    public static PointerBuffer monitorPointers;
    // System monitors
    public static Map<Long, Monitor> monitorMap = new HashMap<>();
    // Current game instance
    public static GameInstance instance;
    // Matrix IDs
    public static final int C_ORTHO2D_ID = 0, C_PERSPECTIVE = 1, C_ORTHO = 2;
    // Shader IDs (initialized in Window)
    public static int S_TEXTURE_ID, S_COLOR_ID, S_TEXT_ID, S_TEXTURE_C;
    // Rendering to physics coordinate conversion
    public static final float PIXELS_PER_METER = (0.1f/2);
    // Debug mode
    public static boolean debug = false;
    // GL Version
    public static int glMajor = -1, glMinor = -1;
    // GL Capabilities
    public static GLCapabilities capabilities;
    // Current GLFW window
    public static long currentWindow = -1;

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

        monitorPointers = glfwGetMonitors();

        for(int i = 0; i < monitorPointers.limit(); i++)
        {
            long l = monitorPointers.get(i);
            monitorMap.put(l, new Monitor(l));
        }

        glfwSetMonitorCallback(GLFWMonitorCallback.create((monitorID, event) ->
        {
            monitorPointers = glfwGetMonitors();

            if(event == GLFW_CONNECTED)
            {
                Monitor m = new Monitor(monitorID);
                monitorMap.put(monitorID, m);
                instance.onMonitorConnect(m);
            }
            else
            {
                Monitor m = monitorMap.get(monitorID);
                monitorMap.remove(monitorID);
                instance.onMonitorDisconnect(m);
            }
        }));

        Logger.SYSTEM.log(String.format("LWJGL Version  '%s'", Version.getVersion()));
        Logger.SYSTEM.log(String.format("OpenGL Version '%s'", glVersion));

        glfwDestroyWindow(window);

        instance = inst;
        instance.init();
        instance.start();

        errorCallback.free();
        glfwTerminate();
    }
}
