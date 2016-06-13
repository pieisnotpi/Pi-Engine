package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.shapes.types.colored.ColorQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.cameras.ControlCamera;
import org.joml.Vector2i;

/**
 * A small class used to easily allow for pausing. Meant to be paired with the control camera
 */

public abstract class PauseScene extends Scene
{
    public ColorQuad tint;
    public Text pausedText;
    public boolean paused = false;

    public void init()
    {
        super.init();

        pausedText = new Text("PAUSED", 16, 0, 0, 0.8f, new Color(1, 0, 0), new Color(0, 0, 0, 1), PiEngine.C_ORTHO2D_ID, this);
        pausedText.setX(pausedText.getX() - pausedText.getWidth()/2);
        pausedText.setY(pausedText.getY() - pausedText.getHeight()/2);
        pausedText.disable();

        tint = new ColorQuad(0, 0, 0, 0, 0, 0, new Color(0, 0, 0, 0.4f), PiEngine.C_ORTHO2D_ID, this);
        tint.transparent = true;
        tint.unregister();
    }

    public void onWindowResize(Vector2i res)
    {
        float ratio = (float) res.x/res.y;

        tint.points[0].set(-ratio, -ratio, 0.7f);
        tint.points[1].set(ratio, -ratio, 0.7f);
        tint.points[2].set(-ratio, ratio, 0.7f);
        tint.points[3].set(ratio, ratio, 0.7f);

        super.onWindowResize(res);
    }

    public void togglePause()
    {
        paused = !paused;

        for(Camera camera : cameras)
        {
            if(!camera.getClass().isInstance(ControlCamera.class)) continue;

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
