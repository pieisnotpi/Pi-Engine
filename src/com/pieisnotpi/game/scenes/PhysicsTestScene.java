package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.game.objects.*;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene used to test the Jbox2D library.
 */

public class PhysicsTestScene extends PauseScene
{
    public List<Player> players = new ArrayList<>();
    private List<Wheel> wheels = new ArrayList<>();
    private List<Crate> crates = new ArrayList<>();
    private List<FloorTile> tiles = new ArrayList<>();
    private boolean leftStatus = false, rightStatus = false;

    private Text coords;
    private Truck truck;

    public void init()
    {
        super.init();

        coords = new Text("", 12, 0, 0.85f, 0.7f, PiEngine.ORTHO_ID, this);
        coords.alignmentID = Text.RIGHT;
        coords.disable();

        cameras.add(new Camera(0, 0, 1, 1, 90, this));

        clearColor.set(0.4f, 0.4f, 1);

        float xOffset = -30*FloorTile.scale - PhysicsObject.offset - FloorTile.scale/2;
        for(int i = 0; i < 60; i++) tiles.add(new FloorTile(xOffset += FloorTile.scale, -0.8f, 0, this));
        xOffset = -30*FloorTile.scale;
        for(int i = 0; i < 60; i++) tiles.add(new FloorTile(xOffset += FloorTile.scale, 0.8f, 0, this));

        players.add(new Player(-0.05f, 0.2f, 0.2f, 0, this));
        truck = new Truck(-0.05f, 0.2f, 0.2f, this);
    }

    public void update()
    {
        Camera camera = cameras.get(0);
        String x = "x: " + camera.position.x, y = "y: " + camera.position.y;

        int xLength = 8, yLength = 8;

        if(x.contains("-")) xLength++;
        if(y.contains("-")) yLength++;

        if(x.length() > xLength) x = x.substring(0, xLength);
        if(y.length() > yLength) y = y.substring(0, yLength);

        coords.setText(x + ", " + y);
        super.update();
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
            Vec2 force = new Vec2(lastCursorPos.x, lastCursorPos.y).subLocal(pos).mulLocal(-PiEngine.PIXELS_PER_METER*1.5f);
            crate.body.applyLinearImpulse(force, crate.body.getWorldCenter());
        }

        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).getY() < -2)
            {
                players.get(i).destroy();
                players.remove(i);
            }
        }

        for(int i = 0; i < wheels.size(); i++)
        {
            if(wheels.get(i).getY() < -2)
            {
                wheels.get(i).destroy();
                wheels.remove(i);
            }
        }

        super.updatePhysics();

        if(players.size() > 0)
        {
            float nx = 0, ny = 0;

            for(Player player : players)
            {
                nx += player.getX();
                ny += player.getY();
            }

            cameras.get(0).position.x = nx/players.size() + Player.scale/2;
            cameras.get(0).position.y = ny/players.size() + Player.scale/2;
        }
    }

    public void onKeyPressed(int key, int mods)
    {
        // Enter
        if(key == 257)
        {
            Vector2f pos = window.inputManager.localCursorPos;
            float cx = cameras.get(0).position.x, cy = cameras.get(0).position.y;

            //crates.add(new Crate(cursorXToWorldX(pos.x, Crate.scale/2), cursorYToWorldY(pos.y, Crate.scale/2), 0.2f, this));
            wheels.add(new Wheel(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), 0.2f, this));
        }
        // Left Shift
        else if(key == 340)
        {
            Vector2f pos = window.inputManager.localCursorPos;
            float cx = cameras.get(0).position.x, cy = cameras.get(0).position.y;

            players.add(new Player(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), 0.2f, players.size(), this));
        }
        // Space
        else if(key == 32)
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

    private float cursorXToWorldX(float x, float radius)
    {
        return cameras.get(0).position.x + x - radius - PhysicsObject.calcOffset(radius);
    }

    private float cursorYToWorldY(float y, float radius)
    {
        return cameras.get(0).position.y + y - radius - PhysicsObject.calcOffset(radius);
    }
}
