package com.pieisnotpi.engine;

import com.pieisnotpi.engine.audio.AudioPlayer;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.updates.GameUpdate;
import com.pieisnotpi.engine.utility.ShaderReloadUtility;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class GameInstance
{
    private boolean run = true;

    public List<Window> windows = new ArrayList<>();
    public AudioPlayer player;
    private List<GameUpdate> updates = new ArrayList<>();
    protected ShaderReloadUtility shaderReload;

    public void init() throws Exception
    {
        player = new AudioPlayer();
    }

    public void start() throws Exception
    {
        long startTime = System.currentTimeMillis();

        while(run)
        {
            boolean allClosed = true;

            for(int i = 0; i < windows.size(); i++)
            {
                Window window = windows.get(i);

                if(!glfwWindowShouldClose(window.handle) && window.isAlive()) allClosed = false;
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
                update.update();
                if(update.updates != update.frequency) finished = false;
            }

            if(finished || System.currentTimeMillis() - startTime >= 1000)
            {
                for(GameUpdate update : updates)
                {
                    update.runPerSecondAction();
                    update.updates = 0;
                }

                startTime = System.currentTimeMillis();
            }
            else
            {
                long time = System.currentTimeMillis(), sleepTime = Long.MAX_VALUE;

                for(GameUpdate update : updates) sleepTime = Long.min(sleepTime, (update.lastUpdateTime + update.period) - time);

                if(sleepTime > 0 && sleepTime != Long.MAX_VALUE)
                try { Thread.sleep(sleepTime); }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
    
    public void registerUpdate(GameUpdate update)
    {
        updates.add(update);
        update.lastUpdateTime = System.currentTimeMillis();
    }
    
    public void unregisterUpdate(GameUpdate update)
    {
        updates.remove(update);
    }

    public void onMonitorConnect(Monitor monitor)
    {
        Logger.SYSTEM.log("Monitor connected with ID " + monitor.monitorID);
    }

    public void onMonitorDisconnect(Monitor monitor)
    {
        Logger.SYSTEM.log("Monitor disconnected with ID " + monitor.monitorID);
    }

    public void close()
    {
        run = false;
    }
    
    public void onClose()
    {
        if(player != null) player.destroy();
        windows.forEach(Window::destroy);
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    
        GL.destroy();
    }
}
