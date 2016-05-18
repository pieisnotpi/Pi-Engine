package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.game.cameras.TransitionCamera;
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

    public TransitionCamera camera;

    private Text coords;
    private Truck truck;

    public void init()
    {
        super.init();

        coords = new Text("", 12, 0, 0.85f, 0.7f, PiEngine.ORTHO_ID, this);
        coords.alignmentID = Text.RIGHT;
        coords.disable();

        cameras.add(camera = new TransitionCamera(0, 0, 1, 1, 90, 0.075f, 0, 0.05f, this));

        clearColor.set(0.4f, 0.4f, 1);

        float xOffset = -30*FloorTile.scale - PhysicsObject.offset - FloorTile.scale/2;
        for(int i = 0; i < 60; i++) tiles.add(new FloorTile(xOffset += FloorTile.scale, -0.8f, 0, this));
        xOffset = -30*FloorTile.scale - PhysicsObject.offset - FloorTile.scale/2;
        for(int i = 0; i < 60; i++) tiles.add(new FloorTile(xOffset += FloorTile.scale, 0.8f, 0, this));

        players.add(new Player(-0.05f, 0.2f, 0.2f, 0, this));
        truck = new Truck(-0.05f, 0.2f, 0.2f, this);

        //camera.setZRot(90);
    }

    public void update()
    {
        /*String x = "x: " + camera.getX(), y = "y: " + camera.getY();

        int xLength = 8, yLength = 8;

        if(x.contains("-")) xLength++;
        if(y.contains("-")) yLength++;

        if(x.length() > xLength) x = x.substring(0, xLength);
        if(y.length() > yLength) y = y.substring(0, yLength);

        coords.setText(x + ", " + y);*/
        super.update();
    }

    public void updatePhysics()
    {
        camera.addToZRot(0.1f);

        world.getGravity().x = -camera.up.x*9.81f;
        world.getGravity().y = -camera.up.y*9.81f;

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
            float y = players.get(i).getY();

            if(y < -4 || y > 4)
            {
                players.get(i).destroy();
                players.remove(i);
            }
        }

        for(int i = 0; i < wheels.size(); i++)
        {
            if(wheels.get(i).getY() < -4)
            {
                wheels.get(i).destroy();
                wheels.remove(i);
            }
        }

        super.updatePhysics();

        if(players.size() > 0)
        {
            float nx = 0, ny = 0, minX = 1000, maxX = -1000, minY = 1000, maxY = -1000;

            for(Player player : players)
            {
                minX = Float.min(player.getX(), minX);
                maxX = Float.max(player.getX(), maxX);

                minY = Float.min(player.getY(), minY);
                maxY = Float.max(player.getY(), maxY);

                nx += player.getX();
                ny += player.getY();
            }

            float xDif = maxX - minX, yDif = maxY - minY, dist = (float) Math.sqrt(xDif*xDif + yDif*yDif);

            camera.transitionX(nx/players.size() + Player.scale/2);
            camera.transitionY(ny/players.size() + Player.scale/2);
            camera.transitionOrthoZoom(-dist/4);
        }
    }

    public void onKeyPressed(int key, int mods)
    {
        // Enter
        if(key == 257)
        {
            Vector2f pos = window.inputManager.localCursorPos;

            //crates.add(new Crate(cursorXToWorldX(pos.x, Crate.scale/2), cursorYToWorldY(pos.y, Crate.scale/2), 0.2f, this));
            wheels.add(new Wheel(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), 0.2f, this));
        }
        // Left Shift
        else if(key == 340)
        {
            Vector2f pos = window.inputManager.localCursorPos;

            int player = 0;

            for(int i = 0; i < players.size(); i++)
            {
                if(players.get(i).joystick != player) continue;

                player++;
                i = -1;
            }

            players.add(new Player(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), 0.2f, player, this));
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
        return camera.getX() + x - radius - PhysicsObject.calcOffset(radius);
    }

    private float cursorYToWorldY(float y, float radius)
    {
        return camera.getY() + y - radius - PhysicsObject.calcOffset(radius);
    }
}
