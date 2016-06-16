package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.InputManager;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.types.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.TextShader;
import com.pieisnotpi.engine.rendering.shaders.types.TextureShader;
import com.pieisnotpi.engine.rendering.shaders.types.TexturedCShader;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@SuppressWarnings("ALL")
public class Window
{
    public float ratio;
    public long windowID;
    public boolean focused = true;
    public static int lastShaderID = -1, lastTextureID = -1, prefMonitor = 0;

    private int vsync, refreshRate = -1;
    private long share, time = 0;
    private boolean alive = true, fullscreen, initialized = false;

    private GameUpdate drawUpdate, inputUpdate;

    protected Logger logger;
    protected GLFWWindowFocusCallback focusCallback;
    protected GLFWWindowPosCallback windowPosCallback;
    protected GLFWWindowSizeCallback windowSizeCallback;

    public Scene scene;
    public String name;
    public Monitor monitor;
    public InputManager inputManager;
    public List<ShaderProgram> shaders = new ArrayList<>();
    public Vector2i res = new Vector2i(0, 0), originalRes = new Vector2i(res), pos = new Vector2i(-1, -1), originalPos = new Vector2i(pos), middle = new Vector2i();

    /**
     * Used to initialize a window with a shared GL context. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param share Window to share context with. Should be first (alive) window. Set to 0 to disable
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, long share)
    {
        this(name, width, height, fullscreen, vsync, share, glfwGetPrimaryMonitor());
    }


    /**
     * Used to initialize a window with a shared GL context. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param monitorID The ID of the monitor to use
     * @param share Window to share context with. Should be first (alive) window. Set to 0 to disable
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, long monitorID, long share)
    {
        this(name, width, height, fullscreen, vsync, -1, monitorID, share);
        refreshRate = monitor.getRefreshRate();
    }

    /**
     * Used to initialize a window with a shared GL context and specific refresh rate. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param refreshRate Sets the refresh rate of the window
     * @param share Window to share context with. Should be first (alive) window. Set to 0 to disable
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, int refreshRate, long share)
    {
        this(name, width, height, fullscreen, vsync, refreshRate, glfwGetPrimaryMonitor(), NULL);
    }

    /**
     * Used to initialize a window with a shared GL context and specific refresh rate. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param refreshRate Sets the refresh rate of the window
     * @param monitorID The ID of the monitor to use
     * @param share Window to share context with. Should be first (alive) window. Set to 0 to disable
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, int refreshRate, long monitorID, long share)
    {
        this.fullscreen = fullscreen;
        this.vsync = vsync;
        this.refreshRate = refreshRate;
        this.name = name;
        this.share = share;

        res.set(width, height);
        originalRes.set(width, height);
        logger = new Logger("WINDOW_" + name.toUpperCase().replaceAll(" ", "_"));
        monitor = PiEngine.monitorMap.get(monitorID);
    }

    public void init()
    {
        if(initialized) return;

        windowID = glfwCreateWindow(res.x, res.y, name, NULL, share);
        if(windowID == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(windowID);
        glfwFocusWindow(windowID);

        if(refreshRate == -1) refreshRate = monitor.getRefreshRate();

        glfwSetWindowSizeCallback(windowID, windowSizeCallback = GLFWWindowSizeCallback.create((windowID, width, height) ->
        {
            if(!fullscreen) originalRes.set(width, height);

            res.set(width, height);

            if(scene != null) scene.onWindowResize(res);
        }));

        glfwSetWindowPosCallback(windowID, windowPosCallback = GLFWWindowPosCallback.create((windowID, xPos, yPos) ->
        {
            if(!fullscreen) originalPos.set(xPos, yPos);

            pos.set(xPos, yPos);

            setCurrentMonitor();
        }));

        glfwSetWindowFocusCallback(windowID, focusCallback = GLFWWindowFocusCallback.create((window, focused) ->
        {
            this.focused = focused;
        }));

        inputManager = new InputManager(this);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);
        glClearDepth(1.0f);

        PiEngine.S_TEXTURE_ID = shaders.size();
        TextureShader textureShader = new TextureShader();
        textureShader.init();
        shaders.add(textureShader);

        PiEngine.S_TEXTURE_C = shaders.size();
        TexturedCShader texturedCShader = new TexturedCShader();
        texturedCShader.init();
        shaders.add(texturedCShader);

        PiEngine.S_COLOR_ID = shaders.size();
        ColorShader colorShader = new ColorShader();
        colorShader.init();
        shaders.add(colorShader);

        PiEngine.S_TEXT_ID = shaders.size();
        TextShader textShader = new TextShader();
        textShader.init();
        shaders.add(textShader);

        drawUpdate = new GameUpdate(1, this::draw, () ->
        {
            String time = "" + (float) this.time/drawUpdate.updates;
            if(time.length() >= 5) time = time.substring(0, 5);
            scene.fps.setText(drawUpdate.updates + "fps/" + time + "mspf");

            this.time = 0;
        });

        inputUpdate = new GameUpdate(1, () -> inputManager.pollInputs());

        inputManager.keybinds.add(new Keybind(GLFW_KEY_F11, false, (value) -> setFullscreen(!fullscreen), null));

        center();
        setRefreshRate(refreshRate);
        setVsync(vsync);
        setFullscreen(fullscreen);

        initialized = true;
    }

    public void setScene(Scene scene)
    {
        if(scene.equals(this.scene)) return;

        if(this.scene != null)
        this.scene.setWindow(null);
        this.scene = scene;

        scene.setWindow(this);
        scene.onWindowResize(res);

        glClearColor(scene.clearColor.red, scene.clearColor.green, scene.clearColor.blue, scene.clearColor.alpha);
    }

    public void draw()
    {
        if(scene == null) return;

        long t = System.currentTimeMillis();

        ratio = (float) res.x/res.y;

        if(windowID != prefMonitor)
        {
            PiEngine.currentWindow = windowID;
            glfwMakeContextCurrent(windowID);
        }

        glClearColor(scene.clearColor.red, scene.clearColor.green, scene.clearColor.blue, scene.clearColor.alpha);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene.drawUpdate();

        for(Camera camera : scene.cameras)
        {
            double cos = Math.cos(Math.toRadians(camera.getXRot()));

            scene.renderables.sort((o1, o2) ->
            {
                if(o1.transparent && o2.transparent)
                {
                    float z1 = o1.points[0].z, z2 = o2.points[0].z;
                    if(o1.getMatrixID() == PiEngine.C_PERSPECTIVE) z1 *= cos;
                    else if(o1.getMatrixID() == PiEngine.C_ORTHO2D_ID) z1++;
                    if(o2.getMatrixID() == PiEngine.C_PERSPECTIVE) z2 *= cos;
                    else if(o2.getMatrixID() == PiEngine.C_ORTHO2D_ID) z2++;

                    return Float.compare(z1, z2);
                }
                else if(o1.transparent) return 1;
                else if(o2.transparent) return -1;

                int s = Integer.compare(o1.getShaderID(), o2.getShaderID());
                if(s != 0) return s;
                else if(o1.getTexture() != null && o2.getTexture() != null) return Integer.compare(o1.getTexture().getTexID(), o2.getTexture().getTexID());
                else return 0;
            });

            float viewX = camera.localX*res.x, viewY = camera.localY*res.y, viewWidth = camera.localWidth*res.x, viewHeight = camera.localHeight*res.y, viewRatio;

            glViewport((int) viewX, (int) viewY, (int) viewWidth, (int) viewHeight);

            scene.renderables.forEach(renderable -> shaders.get(renderable.getShaderID()).addVertex(renderable));
            shaders.forEach(ShaderProgram::compileVertices);
            scene.renderables.forEach(renderable -> shaders.get(renderable.getShaderID()).drawNext(camera));
            shaders.forEach(ShaderProgram::clear);
        }

        glfwSwapBuffers(windowID);

        time += System.currentTimeMillis() - t;
    }

    public void show()
    {
        glfwShowWindow(windowID);

        PiEngine.instance.updates.add(inputUpdate);
        PiEngine.instance.updates.add(drawUpdate);
    }

    public void hide()
    {
        glfwHideWindow(windowID);

        PiEngine.instance.updates.remove(inputUpdate);
        PiEngine.instance.updates.remove(drawUpdate);
    }

    public void center()
    {
        setWindowPos(monitor.position.x + (monitor.size.x - originalRes.x)/2, monitor.position.y + (monitor.size.y - originalRes.y)/2);
    }

    public int getRefreshRate() { return refreshRate; }
    public int getVsync() { return vsync; }
    public boolean isAlive() { return alive; }
    public boolean isFullscreen() { return fullscreen; }

    public String getName() { return name; }
    public Vector2i getWindowPos() { return pos; }

    public void setWindowPos(int xPos, int yPos)
    {
        boolean temp = false;

        if(fullscreen)
        {
            temp = true;
            setFullscreen(false);
        }

        pos.set(xPos, yPos);
        originalPos.set(xPos, yPos);
        glfwSetWindowPos(windowID, xPos, yPos);

        setCurrentMonitor();

        if(temp) setFullscreen(true);

        middle.set(pos.x + res.x/2, pos.y + res.y/2);
    }

    public void setWindowRes(int width, int height)
    {
        res.set(width, height);
        middle.set(pos.x + res.x/2, pos.y + res.y/2);

        glfwSetWindowSize(windowID, width, height);
    }

    public void setRefreshRate(int refreshRate)
    {
        this.refreshRate = refreshRate;

        drawUpdate.setFrequency(refreshRate);
        inputUpdate.setFrequency(refreshRate);
    }

    public void setVsync(int vsync)
    {
        this.vsync = vsync;

        if(vsync > 0) refreshRate = monitor.getRefreshRate()/vsync;

        glfwSwapInterval(vsync);
    }

    public void setFullscreen(boolean fullscreen)
    {
        this.fullscreen = fullscreen;

        if(fullscreen) glfwSetWindowMonitor(windowID, monitor.monitorID, 0, 0, 1920, 1080, refreshRate);
        else glfwSetWindowMonitor(windowID, NULL, (int) originalPos.x, (int) originalPos.y, (int) originalRes.x, (int) originalRes.y, refreshRate);
    }

    public void setName(String name)
    {
        if(this.name.contentEquals(name)) return;

        this.name = name;
        glfwSetWindowTitle(windowID, name);
    }

    public void destroy()
    {
        if(alive)
        {
            hide();

            alive = false;
            glfwDestroyWindow(windowID);
        }
    }

    private void setCurrentMonitor()
    {
        Vector2i middle = new Vector2i();

        for(int i = 0; i < PiEngine.monitorPointers.limit(); i++)
        {
            long pointer = PiEngine.monitorPointers.get(i);
            if(pointer == monitor.monitorID) continue;

            Monitor monitor = PiEngine.monitorMap.get(pointer);
            if(monitor.isPointInMonitor(middle.set(pos.x + res.x/2, pos.y + res.y/2))) this.monitor = monitor;
        }
    }

    public static Monitor getPrefMonitor()
    {
        return PiEngine.monitorMap.get(PiEngine.monitorPointers.get(prefMonitor));
    }
}
