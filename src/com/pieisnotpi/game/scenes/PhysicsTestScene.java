package com.pieisnotpi.game.scenes;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.devices.Keyboard;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.updates.GameUpdate;
import com.pieisnotpi.engine.utility.Timer;
import com.pieisnotpi.game.cameras.TransitionCamera;
import com.pieisnotpi.game.objects.FloorTile;
import com.pieisnotpi.game.objects.PhysicsObject;
import com.pieisnotpi.game.objects.Player;
import com.pieisnotpi.game.objects.Wheel;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A scene used to test the Jbox2D library.
 */

public class PhysicsTestScene extends PauseScene
{
    public World world;
    public Text pps;
    public List<Player> players = new ArrayList<>();
    private List<PhysicsObject> objects = new ArrayList<>();
    private Map<Body, FloorTile> tiles = new HashMap<>();
    private boolean leftStatus = false, rightStatus = false;

    public TransitionCamera camera;

    private GameUpdate physicsUpdate;
    private Text winner;
    private Timer timer = new Timer(false, 2000), resetTimer = new Timer(true, 5000);
    private int size = 20;
    private static final float objZ = -0.2f;

    public void init()
    {
        super.init();

        name = "Physics Testing";
        world = new World(new Vec2(0, -9.81f));

        cameras.add(camera = new TransitionCamera(90, 0.075f, 0, 0.05f, this).setViewport(new Vector2f(0, 0), new Vector2f(1, 1)));

        clearColor.set(0.4f, 0.4f, 1);

        reset();

        pps = new Text("", new Vector3f(0), PiEngine.C_ORTHO2D_ID, this);
        pps.getMesh().setScale(3, 3, 1);
        pps.setAlignment(GameObject.HAlignment.LEFT, GameObject.VAlignment.BOTTOM, 0.05f, 0.05f);

        winner = new Text("", new Vector3f(0, 0.1f, 0.7f), new Color(1, 0, 0), new Color(0, 0, 0), PiEngine.C_ORTHO2D_ID, this);
        winner.getMesh().setScale(4, 4, 1);
        winner.setAlignment(GameObject.HAlignment.CENTER, GameObject.VAlignment.CENTER, 0, 0);

        physicsUpdate = new GameUpdate(physicsPollsPerSecond, this::updatePhysics, () ->
        {
            String time = "" + (float) physicsUpdate.totalTimeTaken/physicsUpdate.updates;
            if(time.length() >= 5) time = time.substring(0, 5);
            pps.setText(String.format("%dpps/%smspp", physicsUpdate.updates, time));
        });

        PiEngine.instance.updates.add(physicsUpdate);
    }

    public void update()
    {
        if(!shouldUpdate) return;

        if(timer.isFinished() && size > 9)
        {
            tiles.forEach((b, f) -> f.destroy());

            size-=2;

            float xOffset = -(size/2f)*FloorTile.scale - PhysicsObject.calcOffset(FloorTile.scale/2), yOffset = -(size/2f)*FloorTile.scale - PhysicsObject.calcOffset(FloorTile.scale/2);

            for(int x = 0; x < size; x++)
            {
                for(int y = 0; y < size; y++)
                {
                    if(x == 0 || x == size - 1 || y == 0 || y == size - 1)
                    {
                        FloorTile t = new FloorTile(xOffset + FloorTile.scale*x, yOffset + FloorTile.scale*y, objZ, this);
                        tiles.put(t.body, t);
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
            if(e != null)
            {
                FloorTile t = tiles.get(e.other);
                if(t != null) { player.destroy(); players.remove(i); }
            }
        }

        super.drawUpdate();
    }

    public void updatePhysics()
    {
        if(!shouldUpdatePhysics || window == null) return;
        //camera.addToZRot(0.1f);

        world.getGravity().x = -camera.getUp().x*9.81f;
        world.getGravity().y = -camera.getUp().y*9.81f;

        if(leftStatus) for (PhysicsObject object : objects)
        {
            Vec2 pos = object.body.getPosition().mul(PiEngine.PIXELS_PER_METER);
            Vec2 force = new Vec2(0.5f/((lastCursorPos.x - pos.x)), 0.5f/((lastCursorPos.y - pos.y)));

            object.body.applyLinearImpulse(force, object.body.getWorldCenter());
        }

        if(rightStatus) for(PhysicsObject object : objects)
        {
            Vec2 pos = object.body.getPosition().mul(PiEngine.PIXELS_PER_METER);
            Vec2 force = new Vec2(lastCursorPos.x, lastCursorPos.y).subLocal(pos).mulLocal(-PiEngine.PIXELS_PER_METER*1.5f);
            object.body.applyLinearImpulse(force, object.body.getWorldCenter());
        }

        world.step(1f/physicsPollsPerSecond, 20, 10);
        gameObjects.forEach(GameObject::physicsUpdate);

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
            camera.transitionOrthoZoom(1.05f/dist);
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

            //objects.add(new Crate(cursorXToWorldX(pos.x, Crate.scale/2), cursorYToWorldY(pos.y, Crate.scale/2), objZ, this));
            objects.add(new Wheel(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), objZ, this));
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

            players.add(new Player(cursorXToWorldX(pos.x, Wheel.radius), cursorYToWorldY(pos.y, Wheel.radius), objZ, player, this));
        }
        else if(key == Keyboard.KEY_SPACE)
        {
            for(PhysicsObject object : objects)
            {
                Vec2 pos = object.body.getPosition().mul(PiEngine.PIXELS_PER_METER), force = new Vec2(lastCursorPos.x, lastCursorPos.y).subLocal(pos).mulLocal(-PiEngine.PIXELS_PER_METER*625);
                object.body.applyLinearImpulse(force, object.body.getWorldCenter());
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
        tiles.forEach((b, f) -> f.destroy());
        tiles.clear();

        size = 40;

        timer.stop();
        timer.start();

        shouldUpdate = true;
        shouldUpdatePhysics = true;

        world = new World(new Vec2(0, -9.81f));

        players.add(new Player(-0.5f - PhysicsObject.calcOffset(Player.scale/2), 0.2f, objZ, 0, this));
        //players.add(new Player(0 - PhysicsObject.calcOffset(Player.scale/2), 0.2f, objZ, 2, this));
        players.add(new Player(0.5f - PhysicsObject.calcOffset(Player.scale/2), 0.2f, objZ, 1, this));

        timer.forceFinish();
        update();
    }
}
