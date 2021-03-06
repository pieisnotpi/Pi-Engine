package com.pieisnotpi.engine;

import com.pieisnotpi.engine.output.ErrorWindow;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.window.GLInstance;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.system.MemoryUtil.NULL;

public class PiEngine
{
    // GLFW monitor pointers
    public static PointerBuffer monitorPointers;
    // System monitors
    public static Map<Long, Monitor> monitorMap = new HashMap<>();
    // Current game instance
    public static GameInstance gameInstance;
    // Debug mode
    public static boolean debug = false, gl_debug = false;
    // GL Version
    public static int glMajor = -1, glMinor = -1;
    // Current GL Instance
    public static GLInstance glInstance;

    /**
     * Initializes the Pi Engine
     * @param inst A game instance, necessary for the engine to work
     */

    public static void start(GameInstance inst)
    {
        try
        {
            if(inst == null) throw new IllegalArgumentException("Must provide a non-null game-instance");

            setDebug("true".equals(System.getProperty("piengine.debug")));
            setGlDebug("true".equals(System.getProperty("piengine.gl_debug")));

            glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
            if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

            long window = glfwCreateWindow(100, 100, "", NULL, NULL);
            if(window == NULL) throw new ExceptionInInitializerError("Failed to initialize GLFW window");

            glfwMakeContextCurrent(window);
            GLCapabilities capabilities = GL.createCapabilities();

            String glVersion = glGetString(GL_VERSION);
            int index = glVersion.indexOf('.');
            glMajor = Integer.parseInt(glVersion.substring(0, index));
            glMinor = Integer.parseInt(glVersion.substring(index + 1, glVersion.indexOf('.', index + 1)));

            if(!capabilities.OpenGL31) throw new IllegalStateException("OpenGL version 3.1 not supported, engine will not run");

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
                    Monitor m;
                    monitorMap.put(monitorID, m = new Monitor(monitorID));
                    gameInstance.onMonitorConnect(m);
                }
                else
                {
                    Monitor m = monitorMap.get(monitorID);
                    monitorMap.remove(monitorID);
                    gameInstance.onMonitorDisconnect(m);
                }
            }));

            Logger.SYSTEM.log(String.format("LWJGL Version  '%s'", Version.getVersion()));
            Logger.SYSTEM.log(String.format("OpenGL Version '%s'", glVersion));

            glfwDestroyWindow(window);

            gameInstance = inst;
            gameInstance.init();
            gameInstance.start();
            gameInstance.onClose();
        }
        catch(Exception e) { closeWithException(e); }
    }

    public static void close()
    {
        gameInstance.close();
    }

    public static void closeWithException(Exception e)
    {
        close();
        ErrorWindow.create(e);
    }

    private static void setDebug(boolean value)
    {
        debug = value;
        Logger.SYSTEM.log("Debug mode " + (value ? "enabled" : "disabled"));
    }

    private static void setGlDebug(boolean value)
    {
        gl_debug = value;
        Logger.SYSTEM.log("OpenGL debug mode " + (value ? "enabled" : "disabled"));
    }

    public static Monitor getMonitor(int monitor)
    {
        Monitor m = monitorMap.get(monitorPointers.get(monitor));
        if(m != null) return m;
        else return monitorMap.get(monitorPointers.get(0));
    }
}
