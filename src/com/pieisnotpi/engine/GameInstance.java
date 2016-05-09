package com.pieisnotpi.engine;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.lwjgl.Version;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

public abstract class GameInstance
{
    private boolean isRunning = true;
    private long startTime;

    public List<Window> windows;
    public List<GameUpdate> updates;

    public GameInstance()
    {
        windows = new ArrayList<>();
        updates = new ArrayList<>();
    }

    public void init()
    {
        Logger.SYSTEM.log("LWJGL Version  '" + Version.getVersion() + '\'');
        Logger.SYSTEM.log("OpenGL Version '" + glGetString(GL_VERSION) + '\'');
    }

    public void start()
    {
        startTime = System.currentTimeMillis();

        while(isRunning)
        {
            boolean allClosed = true;

            for(int i = 0; i < windows.size(); i++)
            {
                Window window = windows.get(i);

                if(glfwWindowShouldClose(window.windowID) == GLFW_FALSE) allClosed = false;
                else
                {
                    Logger.SYSTEM.log("Window '" + window.name + "' has been terminated");
                    window.destroy();
                    windows.remove(i);
                    i--;
                }
            }

            if(allClosed) break;

            boolean finished = true;

            for(GameUpdate update : updates)
            {
                if(update.updates < update.frequency && update.lastUpdateTime + update.period <= getTime())
                {
                    long t = System.currentTimeMillis();
                    update.update(getTime());
                    update.lastTimeTaken = System.currentTimeMillis() - t;
                }
                if(update.updates != update.frequency) finished = false;
            }

            if(finished || getTime() >= 1000)
            {
                for(GameUpdate update : updates)
                {
                    update.runPerSecondAction();
                    update.lastUpdateTime = -100;
                    update.updates = 0;
                }

                startTime = System.currentTimeMillis();
            }
            else
            {
                long sleepTime = Long.MAX_VALUE;

                for(GameUpdate update : updates) sleepTime = Long.min(sleepTime, update.lastUpdateTime + update.period);

                sleepTime -= getTime();

                if(sleepTime > 0 && sleepTime != Long.MAX_VALUE)
                try { Thread.sleep(sleepTime); }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                    windows.forEach(Window::destroy);
                    isRunning = false;
                }
            }
        }
    }

    public long getTime()
    {
        return System.currentTimeMillis() - startTime;
    }

    public void process()
    {
        for(Window window : windows) window.scene.update();
    }
}
