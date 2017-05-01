package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.image.Image;
import com.pieisnotpi.engine.input.InputManager;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.updates.GameUpdate;
import com.pieisnotpi.engine.utility.GLDebugUtility;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWImage;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART;
import static org.lwjgl.opengl.GL31.glPrimitiveRestartIndex;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    public float ratio;
    public long handle;
    public boolean focused = true;

    public static HintInitializer hintInitializer = new HintInitializer() {};
    public static ShaderInitializer shaderInitializer = new ShaderInitializer(){};

    private int vsync = 0, refreshRate;
    private long time = 0;
    private boolean alive = true, fullscreen, initialized = false;

    private GameUpdate drawUpdate, inputUpdate;

    protected Logger logger;

    public Scene scene;
    public String name;
    public Monitor monitor;
    public GLInstance glInstance;
    public InputManager inputManager;
    protected Vector2i windowedRes = new Vector2i(0, 0), fullscreenRes, pos = new Vector2i(), originalPos = new Vector2i(), middle = new Vector2i(), bufferRes = new Vector2i();

    /**
     * Used to initialize a window with a shared GL context. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     */

    public Window(String name, int width, int height)
    {
        this(name, width, height, PiEngine.getMonitor(0));
    }

    /**
     * Used to initialize a window with a shared GL context and specific refresh rate. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param monitor The ID of the monitor to use
     */

    public Window(String name, int width, int height, Monitor monitor)
    {
        this.name = name;
        this.monitor = monitor;

        hintInitializer.init();

        handle = glfwCreateWindow(width, height, name, NULL, NULL);
        if(handle == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glInstance = new GLInstance(this).initShaders(shaderInitializer);

        glfwSwapInterval(0);

        windowedRes.set(width, height);
        fullscreenRes = new Vector2i(monitor.size);
        refreshRate = monitor.getRefreshRate();

        inputManager = new InputManager(this);
        logger = new Logger("WINDOW_" + name.toUpperCase().replaceAll(" ", "_"));

        drawUpdate = new GameUpdate(refreshRate, this::draw, timeStep ->
        {
            if(scene == null) return;

            String time = Float.toString((float) this.time/drawUpdate.updates);
            if(scene.fps != null) scene.fps.setText(String.format("%dfps/%smspf", drawUpdate.updates, time.length() > 4 ? time.substring(0, 4) : time));

            this.time = 0;
        }).setName("DRAW");

        inputUpdate = new GameUpdate(60, (timeStep) -> inputManager.pollInputs(timeStep)).setName("INPUT");
    }

    public Window init()
    {
        if(initialized) return this;

        glfwSetWindowSizeCallback(handle, (windowID, width, height) ->
        {
            if(!fullscreen) windowedRes.set(width, height);
            middle.set(pos.x + width/2, pos.y + height/2);
            if(scene != null) scene.onWindowResize(getWindowRes());
        });

        glfwSetFramebufferSizeCallback(handle, (windowID, width, height) -> bufferRes.set(width, height));

        glfwSetWindowPosCallback(handle, (windowID, xPos, yPos) ->
        {
            if(!fullscreen) originalPos.set(xPos, yPos);

            pos.set(xPos, yPos);

            Vector2i res = getWindowRes();
            middle.set(xPos + res.x/2, yPos + res.y/2);

            setCurrentMonitor();
        });

        glfwSetWindowFocusCallback(handle, (window, focused) -> this.focused = focused);

        glInstance.bind();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);

        glEnable(GL_PRIMITIVE_RESTART);
        glPrimitiveRestartIndex(ShaderProgram.primitiveRestart);
        if(glInstance.capabilities.OpenGL43 && PiEngine.gl_debug)
        {
            glEnable(GL_DEBUG_OUTPUT);
            glDebugMessageCallback(GLDebugUtility.createCallback(), 0);
            glDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_OTHER, GL_DEBUG_SEVERITY_NOTIFICATION, (int[]) null, false);
        }

        center();

        initialized = true;

        return this;
    }

    public void draw(float timeStep)
    {
        if(scene == null) return;

        long t = System.currentTimeMillis();

        Vector2i res = getWindowRes();
        ratio = (float) res.x/res.y;

        glInstance.bind();

        glClearColor(scene.clearColor.red, scene.clearColor.green, scene.clearColor.blue, scene.clearColor.alpha);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene.drawUpdate(timeStep);

        for(Camera camera : scene.cameras) camera.drawToBuffer();

        glClearColor(0, 0, 0, 1);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        for(Camera camera : scene.cameras) camera.drawView(bufferRes);

        glfwSwapBuffers(handle);
        glfwPollEvents();

        time += System.currentTimeMillis() - t;
    }

    public String getName() { return name; }
    public Vector2i getMiddle() { return middle; }
    public Vector2i getWindowRes() { if(fullscreen) return fullscreenRes; else return windowedRes; }
    public Vector2i getWindowPos() { return pos; }
    public int getRefreshRate() { return refreshRate; }
    public int getVsync() { return vsync; }
    public boolean isAlive() { return alive; }
    public boolean isFullscreen() { return fullscreen; }

    public Window setScene(Scene scene)
    {
        if(scene.equals(this.scene)) return this;

        if(this.scene != null) this.scene.setWindow(null);
        this.scene = scene;

        glInstance.getShaders().forEach((i, s) -> s.unsortedMeshes.clear());

        if(!scene.isInitialized()) scene.init();

        scene.setWindow(this);

        return this;
    }

    public Window show()
    {
        if(!alive) return this;

        glfwShowWindow(handle);

        PiEngine.gameInstance.registerUpdate(inputUpdate);
        PiEngine.gameInstance.registerUpdate(drawUpdate);

        return this;
    }

    public Window hide()
    {
        if(!alive) return this;

        glfwHideWindow(handle);

        PiEngine.gameInstance.unregisterUpdate(inputUpdate);
        PiEngine.gameInstance.unregisterUpdate(drawUpdate);

        return this;
    }

    public Window center()
    {
        Vector2i res = getWindowRes();
        return setWindowPos(monitor.position.x + (monitor.size.x - res.x)/2, monitor.position.y + (monitor.size.y - res.y)/2);
    }

    public Window setMonitor(Monitor monitor)
    {
        this.monitor = monitor;
        return center();
    }

    /**
     * Sets the position of the window
     * @param xPos The new x position of the window
     * @param yPos The new y position of the window
     */

    public Window setWindowPos(int xPos, int yPos)
    {
        boolean temp = false;

        if(fullscreen)
        {
            temp = true;
            setFullscreen(false);
        }

        glfwSetWindowPos(handle, xPos, yPos);

        setCurrentMonitor();

        if(temp) setFullscreen(true);

        return this;
    }

    /**
     * Sets the resolution of the window
     * @param width The new width of the window, in pixels
     * @param height The new height of the window, in pixels
     */

    public Window setWindowRes(int width, int height)
    {
        glfwSetWindowSize(handle, width, height);

        return this;
    }

    public Window setFullscreenRes(int width, int height)
    {
        fullscreenRes.set(width, height);

        return this;
    }

    /**
     * Sets the refresh rate of the window
     * @param refreshRate The new refresh rate, in frames-per-second
     */

    public Window setRefreshRate(int refreshRate)
    {
        this.refreshRate = refreshRate;

        drawUpdate.setFrequency(refreshRate);

        return this;
    }

    /**
     * Sets the vsync value
     * NOTE: A value of zero disables vsync
     * NOTE: Vsync does not work with fullscreen due to performance issues
     * @param vsync The new vsync value
     */

    public Window setVsync(int vsync)
    {
        this.vsync = vsync;

        if(vsync > 0) setRefreshRate(monitor.getRefreshRate()/vsync);

        glfwSwapInterval(vsync);

        return this;
    }

    /**
     * Sets the fullscreen status of the window
     * NOTE: Fullscreen does not work with vsync due to performance issues
     * @param fullscreen The new fullscreen value
     */

    public Window setFullscreen(boolean fullscreen)
    {
        this.fullscreen = fullscreen;

        if(fullscreen) glfwSetWindowMonitor(handle, monitor.monitorID, 0, 0, fullscreenRes.x, fullscreenRes.y, refreshRate);
        else glfwSetWindowMonitor(handle, NULL, originalPos.x, originalPos.y, windowedRes.x, windowedRes.y, refreshRate);

        return this;
    }

    /**
     * Sets the name of the window
     * @param name The new name of the window
     */

    public Window setName(String name)
    {
        if(this.name.contentEquals(name)) return this;

        this.name = name;
        glfwSetWindowTitle(handle, name);

        return this;
    }
    
    public Window setAttribute(int attribute, int value)
    {
        glfwSetWindowAttrib(handle, attribute, value);
        return this;
    }
    
    public Window setIcon(Image image_16, Image image_32)
    {
        GLFWImage.Buffer buffer = Image.getGLFWImage(image_16, image_32);
        glfwSetWindowIcon(handle, buffer);
        buffer.clear();
        return this;
    }

    /**
     * Destroys the window, making it unusable
     */

    public void destroy()
    {
        if(alive)
        {
            alive = false;

            PiEngine.gameInstance.unregisterUpdate(drawUpdate);
            PiEngine.gameInstance.unregisterUpdate(inputUpdate);

            glfwFreeCallbacks(handle);
            glfwDestroyWindow(handle);
        }
    }

    /*private void bind()
    {
        if(handle == Window.lastWindow) return;

        glfwMakeContextCurrent(Window.lastWindow = handle);
        GL.setCapabilities(capabilities);
    }*/

    private void setCurrentMonitor()
    {
        if(PiEngine.monitorPointers.limit() == 1 || monitor.isPointInMonitor(middle)) return;

        for(int i = 0; i < PiEngine.monitorPointers.limit(); i++)
        {
            long pointer = PiEngine.monitorPointers.get(i);
            if(pointer == monitor.monitorID) continue;

            Monitor monitor = PiEngine.monitorMap.get(pointer);
            if(monitor.isPointInMonitor(middle)) this.monitor = monitor;
        }
    }
}
