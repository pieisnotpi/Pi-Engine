package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.scene.Scene;
import com.pieisnotpi.game.objects.Ball;
import com.pieisnotpi.game.objects.Crate;
import com.pieisnotpi.game.objects.FloorTile;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PhysicsTestScene extends Scene
{
    private List<Ball> balls = new ArrayList<>();
    private List<Crate> crates = new ArrayList<>();
    private List<FloorTile> tiles = new ArrayList<>();
    private boolean leftStatus = false, rightStatus = false;

    public void init()
    {
        super.init();

        cameras.add(new Camera(0, 0, 1, 1, 90, this));

        clearColor.set(0.4f, 0.4f, 1);

        float xOffset = -16*FloorTile.scale;

        for(int i = 0; i < 30; i++) tiles.add(new FloorTile(xOffset += FloorTile.scale, -0.8f, 0, this));
    }

    public void updatePhysics()
    {
        if(leftStatus) for(Crate crate : crates)
        {
            Vec2 pos = crate.body.getPosition().mul(PiEngine.PIXELS_PER_METER);
            Vec2 force = new Vec2(0.5f/((lastCursorPos.x - pos.x)), 0.5f/((lastCursorPos.y - pos.y)));

            crate.body.applyLinearImpulse(force, crate.body.getWorldCenter());
        }

        if(rightStatus) for(Crate crate : crates)
        {
            Vec2 pos = crate.body.getPosition().mul(PiEngine.PIXELS_PER_METER);
            Vec2 force = new Vec2(lastCursorPos.x, lastCursorPos.y).subLocal(pos).mulLocal(-PiEngine.PIXELS_PER_METER *1.5f);
            crate.body.applyLinearImpulse(force, crate.body.getWorldCenter());
        }

        super.updatePhysics();
    }

    public void onKeyPressed(int key, int mods)
    {
        if(key == 0x101)
        {
            Vector2f pos = window.inputManager.localCursorPos;

            crates.add(new Crate(pos.x - Crate.scale/2, pos.y - Crate.scale/2, 0.2f, this));
            //balls.add(new Ball(pos.x, pos.y, 0.2f, this));
        }
        else if(key == 0x20)
        {
            for(Crate crate : crates)
            {
                Vec2 pos = crate.body.getPosition().mul(PiEngine.PIXELS_PER_METER), force = new Vec2(lastCursorPos.x, lastCursorPos.y).subLocal(pos).mulLocal(-PiEngine.PIXELS_PER_METER*625);
                crate.body.applyLinearImpulse(force, crate.body.getWorldCenter());
            }
        }
    }

    public void onLeftClick()
    {
        leftStatus = true;
    }

    public void onLeftRelease()
    {
        leftStatus = false;
    }

    public void onRightClick()
    {
        rightStatus = true;
    }

    public void onRightRelease()
    {
        rightStatus = false;
    }
}
