package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.InputManager;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.types.ads_shader.ADSShader;
import com.pieisnotpi.engine.rendering.shaders.types.color_shader.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c_shader.TexturedCShader;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TextureShader;
import com.pieisnotpi.engine.rendering.shaders.types.text_shader.TextShader;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

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
    public Map<Integer, ShaderProgram> shaders = new HashMap<>();
    protected Vector2i res = new Vector2i(0, 0), originalRes = new Vector2i(), fullscreenRes = new Vector2i(), pos = new Vector2i(-1, -1), originalPos = new Vector2i(), middle = new Vector2i();

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

        glfwSetWindowFocusCallback(windowID, focusCallback = GLFWWindowFocusCallback.create((window, focused) -> this.focused = focused));

        inputManager = new InputManager(this);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);
        glClearDepth(1.0f);

        initShaders();

        drawUpdate = new GameUpdate(1, this::draw, () ->
        {
            String time = "" + (float) this.time/drawUpdate.updates;
            if(time.length() >= 4) time = time.substring(0, 4);
            scene.fps.setText(String.format("%dfps/%smspf", drawUpdate.updates, time));

            this.time = 0;
        });

        inputUpdate = new GameUpdate(1, () -> inputManager.pollInputs());

        inputManager.keybinds.add(new Keybind(GLFW_KEY_F11, false, (value) -> setFullscreen(!fullscreen), null));

        if(fullscreenRes.x == 0 && fullscreenRes.y == 0) fullscreenRes.set(monitor.size);
        center();
        setRefreshRate(refreshRate);
        setVsync(vsync);
        setFullscreen(fullscreen);

        initialized = true;
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
        shaders.forEach((i, s) -> s.compileUnsorted());

        for(Camera camera : scene.cameras)
        {
            if(!camera.shouldDrawView() && !camera.shouldDrawFbo()) return;

            double cos = Math.cos(Math.toRadians(camera.getYRot() - 90));

            scene.sortedBuffer.sort((o1, o2) ->
            {
                float z1 = o1.points[0].z, z2 = o2.points[0].z;

                if(o1.getMatrixID() == PiEngine.C_PERSPECTIVE) z1 = camera.getZ() - (float) (z1*cos);
                else if(o1.getMatrixID() == PiEngine.C_ORTHO2D_ID) z1++;
                if(o2.getMatrixID() == PiEngine.C_PERSPECTIVE) z2 = camera.getZ() - (float) (z2*cos);
                else if(o2.getMatrixID() == PiEngine.C_ORTHO2D_ID) z2++;

                int s = Integer.compare(Float.floatToIntBits(z1), Float.floatToIntBits(z2));
                if(s != 0) return s;

                s = Integer.compare(o1.getShaderID(), o2.getShaderID());
                if(s != 0) return s;

                if(o1.getTexture() != null && o2.getTexture() != null) return Integer.compare(o1.getTexture().getTexID(), o2.getTexture().getTexID());
                else return 0;
            });

            scene.sortedBuffer.forEach(renderable -> renderable.shader.addSortedVertex(renderable));
            shaders.forEach((i, s) -> s.compileSorted());

            if(camera.shouldDrawFbo())
            {
                camera.frameBuffer.bind();
                glViewport(0, 0, camera.frameBuffer.res.x, camera.frameBuffer.res.y);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                shaders.forEach((i, s) -> s.drawUnsorted(camera));

                scene.sortedBuffer.forEach(renderable -> renderable.shader.drawNextSorted(camera));
                camera.frameBuffer.unbind();
            }

            if(camera.shouldDrawView())
            {
                glViewport((int) (res.x*camera.viewPos.x), (int) (res.y*camera.viewPos.y), (int) (res.x*camera.viewSize.x), (int) (res.y*camera.viewSize.y));

                shaders.forEach((i, s) -> s.drawUnsorted(camera));

                scene.sortedBuffer.forEach(renderable -> renderable.shader.drawNextSorted(camera));
            }
        }

        time += System.currentTimeMillis() - t;

        glfwSwapBuffers(windowID);
        glfwPollEvents();
    }

    public void setScene(Scene scene)
    {
        if(scene.equals(this.scene)) return;

        shaders.forEach((i, s) -> s.unsortedBuffer.clear());

        if(this.scene != null) this.scene.setWindow(null);
        this.scene = scene;

        scene.setWindow(this);
        scene.onWindowResize(res);

        glClearColor(scene.clearColor.red, scene.clearColor.green, scene.clearColor.blue, scene.clearColor.alpha);
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
        setWindowPos(monitor.position.x + (monitor.size.x - res.x)/2, monitor.position.y + (monitor.size.y - res.y)/2);
    }

    public String getName() { return name; }
    public Vector2i getMiddle() { return middle; }
    public Vector2i getWindowRes() { return res; }
    public Vector2i getWindowPos() { return pos; }
    public int getRefreshRate() { return refreshRate; }
    public int getVsync() { return vsync; }
    public boolean isAlive() { return alive; }
    public boolean isFullscreen() { return fullscreen; }

    /**
     * Sets the position of the window
     * @param xPos The new x position of the window
     * @param yPos The new y position of the window
     */

    public void setWindowPos(int xPos, int yPos)
    {
        boolean temp = false;

        if(fullscreen)
        {
            temp = true;
            setFullscreen(false);
        }

        glfwSetWindowPos(windowID, xPos, yPos);

        setCurrentMonitor();

        if(temp) setFullscreen(true);

        middle.set(pos.x + res.x/2, pos.y + res.y/2);
    }

    /**
     * Sets the resolution of the window
     * @param width The new width of the window, in pixels
     * @param height The new height of the window, in pixels
     */

    public void setWindowRes(int width, int height)
    {
        glfwSetWindowSize(windowID, width, height);
        middle.set(pos.x + res.x/2, pos.y + res.y/2);
    }

    public void setFullscreenRes(int width, int height)
    {
        fullscreenRes.set(width, height);
    }

    /**
     * Sets the refresh rate of the window
     * @param refreshRate The new refresh rate, in frames-per-second
     */

    public void setRefreshRate(int refreshRate)
    {
        this.refreshRate = refreshRate;

        drawUpdate.setFrequency(refreshRate);
        inputUpdate.setFrequency(refreshRate);
    }

    /**
     * Sets the vsync value
     * NOTE: A value of zero disables vsync
     * NOTE: Vsync does not work with fullscreen due to performance issues
     * @param vsync The new vsync value
     */

    public void setVsync(int vsync)
    {
        this.vsync = vsync;

        if(vsync > 0) refreshRate = monitor.getRefreshRate()/vsync;

        glfwSwapInterval(vsync);
    }

    /**
     * Sets the fullscreen status of the window
     * NOTE: Fullscreen does not work with vsync due to performance issues
     * @param fullscreen The new fullscreen value
     */

    public void setFullscreen(boolean fullscreen)
    {
        this.fullscreen = fullscreen;

        if(fullscreen) glfwSetWindowMonitor(windowID, monitor.monitorID, 0, 0, fullscreenRes.x, fullscreenRes.y, refreshRate);
        else glfwSetWindowMonitor(windowID, NULL, originalPos.x, originalPos.y, originalRes.x, originalRes.y, refreshRate);
    }

    /**
     * Sets the name of the window
     * @param name The new name of the window
     */

    public void setName(String name)
    {
        if(this.name.contentEquals(name)) return;

        this.name = name;
        glfwSetWindowTitle(windowID, name);
    }

    /**
     * Destroys the window, making it unusable
     */

    public void destroy()
    {
        if(alive)
        {
            hide();

            alive = false;
            glfwDestroyWindow(windowID);
        }
    }

    protected void initShaders()
    {
        shaders.put(PiEngine.S_TEXTURE_ID, new TextureShader().init());
        shaders.put(PiEngine.S_COLOR_ID, new ColorShader().init());
        shaders.put(PiEngine.S_TEXT_ID, new TextShader().init());
        shaders.put(PiEngine.S_TEXTURE_C_ID, new TexturedCShader().init());
        shaders.put(PiEngine.S_ADS_ID, new ADSShader().init());
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
