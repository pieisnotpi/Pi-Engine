package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.ControlCamera;

public abstract class PauseScene extends Scene
{
    public void togglePause()
    {
        paused = !paused;

        for(Camera camera : cameras)
        {
            ControlCamera c = (ControlCamera) camera;

            for(int i = 0; i < c.joybinds.size(); i++)
                if(i != c.pauseSlot && i != c.fullscreenSlot)
                    c.joybinds.get(i).enabled = !paused;
        }

        shouldUpdate = !paused;
        shouldUpdatePhysics = !paused;

        if(paused) pausedText.enable();
        else pausedText.disable();

        tint.toggle();
    }
}
