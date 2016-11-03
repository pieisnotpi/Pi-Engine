package com.pieisnotpi.engine;

import com.pieisnotpi.engine.audio.AudioPlayer;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Monitor;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.updates.GameUpdate;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public abstract class GameInstance
{
    public List<Window> windows = new ArrayList<>();
    public List<GameUpdate> updates = new ArrayList<>();
    public AudioPlayer player;

    public void init()
    {
        player = new AudioPlayer();
    }

    public void start()
    {
        long startTime = System.currentTimeMillis();

        while(true)
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
                if(update.lastUpdateTime <= 0) update.lastUpdateTime = System.currentTimeMillis() - update.frequency - 1;
                update.update(System.currentTimeMillis());
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
                    windows.forEach(Window::destroy);
                    break;
                }
            }
        }

        player.destroy();
    }

    public void onMonitorConnect(Monitor monitor)
    {
        Logger.SYSTEM.log("Monitor connected with ID " + monitor.monitorID);
    }

    public void onMonitorDisconnect(Monitor monitor)
    {
        Logger.SYSTEM.log("Monitor disconnected with ID " + monitor.monitorID);
    }
}
