package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.PhysicsObject;
import com.pieisnotpi.engine.input.devices.Keyboard;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.utility.Timer;
import com.pieisnotpi.game.cameras.TransitionCamera;
import com.pieisnotpi.game.objects.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
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

    private Text winner;
    private Truck truck;
    private Timer timer = new Timer(false, 2000), resetTimer = new Timer(true, 5000);
    private int size = 40;

    public void init()
    {
        super.init();

        name = "Physics Testing";

        cameras.add(camera = new TransitionCamera(0, 0, 1, 1, 90, 0.075f, 0, 0.05f, this));

        clearColor.set(0.4f, 0.4f, 1);

        final float xOffset = -(size/2f)*FloorTile.scale - PhysicsObject.calcOffset(FloorTile.scale/2), yOffset = -(size/2f)*FloorTile.scale - PhysicsObject.calcOffset(FloorTile.scale/2);

        reset();

        winner = new Text("", 12, 0, 0.1f, 0.7f, new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.ORTHO_ID, this);
        winner.alignmentID = Text.CENTERED;
    }

    public void update()
    {
        if(!shouldUpdate) return;

        if(timer.isFinished() && size > 9)
        {
            tiles.stream().filter(tile -> tile.bodyAlive).forEach(tile ->
            {
                world.destroyBody(tile.body);
                tile.bodyAlive = false;
            });

            size-=2;

            float xOffset = -(size/2f)*FloorTile.scale - PhysicsObject.calcOffset(FloorTile.scale/2), yOffset = -(size/2f)*FloorTile.scale - PhysicsObject.calcOffset(FloorTile.scale/2);

            for(int x = 0; x < size; x++)
            {
                for(int y = 0; y < size; y++)
                {
                    if(x == 0 || x == size - 1 || y == 0 || y == size - 1)
                    {
                        tiles.add(new FloorTile(xOffset + FloorTile.scale*x, yOffset + FloorTile.scale*y, 0.2f, this));
                    }
                }
            }
        }

        super.update();
    }

    public void drawUpdate()
    {
        for(int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);

            ContactEdge e = player.body.getContactList();
            if(e != null) tiles.stream().filter(tile -> e.other.equals(tile.body)).forEach(tile ->
            {
                player.destroy();
                players.remove(player);
            });
        }

        super.drawUpdate();
    }

    public void updatePhysics()
    {
        //camera.addToZRot(0.1f);

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

        super.updatePhysics();

        if(players.size() == 1)
        {
            winner.enable();
            winner.setText("Player " + (players.get(0).joystick + 1) + " has won!");
            shouldUpdatePhysics = false;
            shouldUpdate = false;

            resetTimer.start();
        }
        else winner.disable();

        if(resetTimer.isFinished())
        {
            reset();
        }

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
        else
        {
            camera.transitionX(0);
            camera.transitionY(0);
        }
    }

    public void onKeyPressed(int key, int mods)
    {
        if(key == Keyboard.KEY_ENTER)
        {
            Vector2f pos = window.inputManager.localCursorPos;

            //crates.add(new Crate(cursorXToWorldX(pos.x, Crate.scale/2), cursorYToWorldY(pos.y, Crate.scale/2), 0.2f, this));
            wheels.add(new Wheel(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), 0.2f, this));
        }
        else if(key == Keyboard.KEY_LEFT_SHIFT)
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
        else if(key == Keyboard.KEY_SPACE)
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

    private void reset()
    {
        players.forEach(Player::destroy);
        players.clear();
        tiles.forEach(FloorTile::destroy);
        tiles.clear();

        size = 40;

        timer.stop();
        timer.start();

        shouldUpdate = true;
        shouldUpdatePhysics = true;

        world = new World(new Vec2(0, -9.81f));

        players.add(new Player(-0.5f - PhysicsObject.calcOffset(Player.scale/2), 0.2f, 0.2f, 0, this));
        //players.add(new Player(0 - PhysicsObject.calcOffset(Player.scale/2), 0.2f, 0.2f, 2, this));
        players.add(new Player(0.5f - PhysicsObject.calcOffset(Player.scale/2), 0.2f, 0.2f, 1, this));

        timer.forceFinish();
        update();
    }
}
