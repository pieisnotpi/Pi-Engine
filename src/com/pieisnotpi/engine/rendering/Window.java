package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.InputManager;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.shaders.types.ColorShader;
import com.pieisnotpi.engine.rendering.shaders.types.TextShader;
import com.pieisnotpi.engine.rendering.shaders.types.TextureShader;
import com.pieisnotpi.engine.rendering.shaders.types.TexturedCShader;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.joml.Vector2i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GLCapabilities;

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

    private int vsync, refreshRate;
    private long share;
    private boolean alive = true, fullscreen, staticRefreshRate, initialized = false;

    private GameUpdate drawUpdate, inputUpdate;

    protected Logger logger;
    protected Monitor curMonitor;
    protected GLFWMonitorCallback monitorCallback;
    protected GLFWWindowFocusCallback focusCallback;
    protected GLFWWindowPosCallback windowPosCallback;
    protected GLFWWindowSizeCallback windowSizeCallback;

    public Scene scene;
    public String name;
    public List<Monitor> monitors = new ArrayList<>();
    public InputManager inputManager;
    public GLCapabilities capabilities;
    public List<ShaderProgram> shaders = new ArrayList<>();
    public Vector2i res = new Vector2i(0, 0), originalRes = new Vector2i(res), pos = new Vector2i(-1, -1), originalPos = new Vector2i(pos);

    /**
     * Used to initialize a standard window
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync)
    {
        this(name, width, height, fullscreen, vsync, NULL);
    }

    /**
     * Used to initialize a window with a shared GL context. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param share Window to share context with. Should be first (alive) window
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, long share)
    {
        this.fullscreen = fullscreen;
        this.vsync = vsync;
        this.staticRefreshRate = false;
        this.name = name;

        this.share = share;

        res.set(width, height);
        originalRes.set(width, height);
        logger = new Logger("WINDOW_" + name.toUpperCase().replaceAll(" ", "_"));
    }

    /**
     * Used to initialize a standard window with a specific refresh rate
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param refreshRate Sets the refresh rate of the window
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, int refreshRate)
    {
        this(name, width, height, fullscreen, vsync, refreshRate, NULL);
    }

    /**
     * Used to initialize a window with a shared GL context and specific refresh rate. Shared GL context is necessary for multiple windows.
     * @param name Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param fullscreen Enables/disables fullscreen for the window
     * @param vsync Sets window swap interval. Value of 0 disables vsync
     * @param refreshRate Sets the refresh rate of the window
     * @param share Window to share context with. Should be first (alive) window
     */

    public Window(String name, int width, int height, boolean fullscreen, int vsync, int refreshRate, long share)
    {
        this.fullscreen = fullscreen;
        this.vsync = vsync;
        this.refreshRate = refreshRate;
        this.staticRefreshRate = true;
        this.name = name;
        this.share = share;

        res.set(width, height);
        originalRes.set(width, height);
        logger = new Logger("WINDOW_" + name.toUpperCase().replaceAll(" ", "_"));
    }

    public void init()
    {
        if(initialized) return;

        windowID = glfwCreateWindow((int) res.x, (int) res.y, name, NULL, share);
        if(windowID == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(windowID);
        glfwFocusWindow(windowID);

        PointerBuffer monitorBuffer = glfwGetMonitors();

        for(int i = 0; i < monitorBuffer.limit(); i++) monitors.add(new Monitor(monitorBuffer.get(i)));

        if(PiEngine.monitor >= monitorBuffer.limit() || PiEngine.monitor < 0)
        {
            long m = glfwGetPrimaryMonitor();

            for (int i = 0; i < monitors.size(); i++) if(monitors.get(i).monitorID == m) PiEngine.monitor = i;
        }

        curMonitor = monitors.get(PiEngine.monitor);

        if(!staticRefreshRate) refreshRate = curMonitor.getRefreshRate();

        glfwSetMonitorCallback(monitorCallback = GLFWMonitorCallback.create((monitorID, event) ->
        {
            if(event == GLFW_CONNECTED) monitors.add(new Monitor(monitorID));
            else for(int i = 0; i < monitors.size(); i++) if(monitors.get(i).monitorID == monitorID)
            {
                if(monitors.get(i).isPointInMonitor(pos))
                {
                    if(i == 0 && monitors.size() > 1) glfwSetWindowPos(windowID, (int) monitors.get(1).getPosition().x, (int) monitors.get(1).getPosition().y);
                    else if(i != 0) glfwSetWindowPos(windowID, (int) monitors.get(0).getPosition().x, (int) monitors.get(0).getPosition().y);
                }

                monitors.remove(i);
                return;
            }
        }));

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
            if(!focused) inputManager.hideCursor = false;
        }));

        inputManager = new InputManager(this);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);
        glClearDepth(1.0f);

        PiEngine.TEXTURE_ID = shaders.size();
        TextureShader textureShader = new TextureShader();
        textureShader.init();
        shaders.add(textureShader);

        PiEngine.TEXTURE_C = shaders.size();
        TexturedCShader texturedCShader = new TexturedCShader();
        texturedCShader.init();
        shaders.add(texturedCShader);

        PiEngine.COLOR_ID = shaders.size();
        ColorShader colorShader = new ColorShader();
        colorShader.init();
        shaders.add(colorShader);

        PiEngine.TEXT_ID = shaders.size();
        TextShader textShader = new TextShader();
        textShader.init();
        shaders.add(textShader);

        drawUpdate = new GameUpdate(1, () ->
        {
            draw();
            glfwPollEvents();
        }, () ->
        {
            String time = "" + (float) drawUpdate.totalTimeTaken /drawUpdate.updates;
            if(time.length() >= 5) time = time.substring(0, 5);
            scene.fps.setText(drawUpdate.updates + "fps/" + time + "mspf");
        });
        inputUpdate = new GameUpdate(1, () -> inputManager.pollInputs());

        inputManager.keybinds.add(new Keybind(GLFW_KEY_F11, false, (value) -> setFullscreen(!fullscreen), null));

        setWindowPos(curMonitor.position.x + (curMonitor.size.x - originalRes.x)/2, curMonitor.position.y + (curMonitor.size.y - originalRes.y)/2);
        setRefreshRate(refreshRate);
        setFullscreen(fullscreen);
        setVsync(vsync);

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

        ratio = (float) res.x/res.y;

        if(glfwGetCurrentContext() != windowID) glfwMakeContextCurrent(windowID);
        glClearColor(scene.clearColor.red, scene.clearColor.green, scene.clearColor.blue, scene.clearColor.alpha);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        int prevShaderID = -1;

        for(Camera camera : scene.cameras)
        {
            camera.setRatio(ratio);
            camera.update();

            double rot = Math.toRadians(camera.rot.x);

            scene.renderables.sort((o1, o2) ->
            {
                if(o1.transparent && o2.transparent)
                {
                    float z1 = o1.points[0].z, z2 = o2.points[0].z;
                    if(o1.getMatrixID() == PiEngine.CAMERA_3D_ID) z1 *= Math.cos(rot);
                    if(o2.getMatrixID() == PiEngine.CAMERA_3D_ID) z2 *= Math.cos(rot);

                    if(z1 > z2) return 1;
                    else if(z1 < z2) return -1;
                    else return 0;
                }
                else if(o1.transparent) return 1;
                else if(o2.transparent) return -1;
                else if(o1.getShaderID() > o2.getShaderID()) return -1;
                else if(o1.getShaderID() < o2.getShaderID()) return 1;
                else return 0;
            });

            float viewX = camera.localX*res.x, viewY = camera.localY*res.y, viewWidth = camera.localWidth*res.x, viewHeight = camera.localHeight*res.y, viewRatio;

            glViewport((int) viewX, (int) viewY, (int) viewWidth, (int) viewHeight);

            for(Renderable renderable : scene.renderables)
            {
                ShaderProgram shader = shaders.get(renderable.getShaderID());

                if(renderable.getShaderID() == prevShaderID || prevShaderID == -1) shader.addVertex(renderable);
                else
                {
                    shaders.get(prevShaderID).draw(camera);
                    shader.addVertex(renderable);
                }

                prevShaderID = renderable.getShaderID();
            }

            if(prevShaderID != -1 && shaders.get(prevShaderID).buffer.size() > 0) shaders.get(prevShaderID).draw(camera);
        }

        glfwSwapBuffers(windowID);
    }

    public void show()
    {
        glfwShowWindow(windowID);

        PiEngine.instance.updates.add(drawUpdate);
        PiEngine.instance.updates.add(inputUpdate);
    }

    public void hide()
    {
        glfwHideWindow(windowID);

        PiEngine.instance.updates.remove(drawUpdate);
        PiEngine.instance.updates.remove(inputUpdate);
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
    }

    public void setRefreshRate(int refreshRate)
    {
        staticRefreshRate = true;
        this.refreshRate = refreshRate;

        drawUpdate.setFrequency(refreshRate);
        inputUpdate.setFrequency(refreshRate);
    }

    public void setVsync(int vsync)
    {
        this.vsync = vsync;

        if(!staticRefreshRate && vsync > 0) refreshRate = curMonitor.getRefreshRate()/vsync;

        glfwSwapInterval(vsync);
    }

    public void setFullscreen(boolean fullscreen)
    {
        this.fullscreen = fullscreen;

        if(fullscreen) glfwSetWindowMonitor(windowID, curMonitor.monitorID, 0, 0, 1920, 1080, refreshRate);
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
        for(int i = 0; i < monitors.size(); i++)
        {
            Monitor monitor = monitors.get(i);

            if(monitor.equals(curMonitor)) continue;

            if(monitor.isPointInMonitor(pos))
            {
                PiEngine.monitor = i;
                curMonitor = monitors.get(i);
            }
        }
    }
}
