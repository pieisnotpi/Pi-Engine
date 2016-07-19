package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.audio.AudioListener;
import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Joystick;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.Mousebind;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.rendering.ui.text.TextRenderable;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for scenes, the foundation of the engine's user interface.
 * All scenes used by the game should extend this class.
 */

public abstract class Scene
{
    public AudioListener listener;
    public World world;
    public Text fps, pps;
    public Window window;
    public String name = "Scene";
    public Color clearColor = new Color(0.5f, 0.5f, 0.5f);
    public List<Camera> cameras = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>(20);
    public List<Renderable> unsortedBuffer = new ArrayList<>(100);
    public List<Renderable> sortedBuffer = new ArrayList<>(100);
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public Vector2f lastCursorPos = new Vector2f();

    private GameUpdate physicsUpdate, gameUpdate;
    protected int physicsPollsPerSecond = 60, updatesPerSecond = 60;

    protected Scene() {}

    public boolean shouldUpdate = true, shouldUpdatePhysics = false;

    public void onJoystickConnect(Joystick joystick) { gameObjects.forEach(g -> g.onJoystickConnect(joystick)); }
    public void onJoystickDisconnect(Joystick joystick) { gameObjects.forEach(g -> g.onJoystickDisconnect(joystick)); }
    public void onLeftClick() { gameObjects.forEach(GameObject::onLeftClick); }
    public void onRightClick() { gameObjects.forEach(GameObject::onRightClick); }
    public void onLeftRelease() { gameObjects.forEach(GameObject::onLeftRelease); }
    public void onRightRelease() { gameObjects.forEach(GameObject::onRightRelease); }
    public void onMiddleClick() { gameObjects.forEach(GameObject::onMiddleClick); }
    public void onMiddleRelease() { gameObjects.forEach(GameObject::onMiddleRelease); }
    public void onKeyPressed(int key, int mods) { gameObjects.forEach(g -> g.onKeyPressed(key, mods)); }
    public void onKeyReleased(int key, int mods) { gameObjects.forEach(g -> g.onKeyReleased(key, mods)); }
    public void onScroll(float xAmount, float yAmount) { gameObjects.forEach(g -> g.onScroll(xAmount, yAmount)); }
    public void onMouseMovement(Vector2f cursorPos) { gameObjects.forEach(g -> g.onMouseMovement(cursorPos)); lastCursorPos.set(cursorPos); }
    public void onMouseMovementUnscaled(Vector2d cursorPos) { gameObjects.forEach(g -> g.onMouseMovementUnscaled(cursorPos)); }

    public void onWindowResize(Vector2i res)
    {
        gameObjects.forEach(gameObject -> gameObject.onWindowResize(res));
    }

    public void init()
    {
        fps = new Text("", 12, 0, 0.85f, 0.8f, PiEngine.C_ORTHO2D_ID, this);
        pps = new Text("", 12, 0, -0.95f, 0.8f, PiEngine.C_ORTHO2D_ID, this);
        world = new World(new Vec2(0, -9.81f));
        listener = new AudioListener(this);

        fps.setAlignment(GameObject.HAlignment.LEFT, GameObject.VAlignment.TOP, 0.05f, -0.15f);
        pps.setAlignment(GameObject.HAlignment.LEFT, GameObject.VAlignment.BOTTOM, 0.05f, 0.05f);

        physicsUpdate = new GameUpdate(physicsPollsPerSecond, this::updatePhysics, () ->
        {
            String time = "" + (float) physicsUpdate.totalTimeTaken/physicsUpdate.updates;
            if(time.length() >= 5) time = time.substring(0, 5);
            pps.setText(String.format("%dpps/%smspp", physicsUpdate.updates, time));
        });

        gameUpdate = new GameUpdate(60, this::update);

        PiEngine.instance.updates.add(physicsUpdate);
        PiEngine.instance.updates.add(gameUpdate);
    }

    public void update()
    {
        if(!shouldUpdate || window == null) return;

        gameObjects.forEach(GameObject::update);
    }

    public void drawUpdate()
    {
        if(window == null) return;

        gameObjects.forEach(GameObject::drawUpdate);
    }

    public void updatePhysics()
    {
        if(!shouldUpdatePhysics || window == null) return;


        world.step(1f/physicsPollsPerSecond, 20, 10);
        gameObjects.forEach(GameObject::physicsUpdate);
    }

    public void setWindow(Window window)
    {
        gameObjects.forEach(g -> g.onWindowChanged(this.window, window));

        unregisterInputs();
        this.window = window;
        registerInputs();

        if(window != null)
        {
            onWindowResize(window.getWindowRes());
            unsortedBuffer.forEach(r ->
            {
                r.shader = window.shaders.get(r.getShaderID());
                r.shader.addUnsortedVertex(r);
            });
            sortedBuffer.forEach(r -> r.shader = window.shaders.get(r.getShaderID()));
        }
        else
        {
            unsortedBuffer.forEach(r -> r.shader = null);
            sortedBuffer.forEach(r -> r.shader = null);
        }
    }

    public void addRenderable(Renderable renderable)
    {
        if(window != null) renderable.shader = window.shaders.get(renderable.getShaderID());
        else renderable.shader = null;

        if(renderable.getClass().isAssignableFrom(TextRenderable.class)) System.out.println(renderable.shouldBeSorted);

        if(renderable.shouldBeSorted)
        {
            if(!sortedBuffer.contains(renderable)) sortedBuffer.add(renderable);
        }
        else if(!unsortedBuffer.contains(renderable))
        {
            unsortedBuffer.add(renderable);
            if(renderable.shader != null) renderable.shader.addUnsortedVertex(renderable);
        }
    }

    public void removeRenderable(Renderable renderable)
    {
        if(renderable.shouldBeSorted) sortedBuffer.remove(renderable);
        else
        {
            if(renderable.getShaderID() == PiEngine.S_COLOR_ID) System.out.println("yes");
            unsortedBuffer.remove(renderable);
            if(renderable.shader != null) renderable.shader.removeUnsortedVertex(renderable);
        }
    }

    public void addKeybind(Keybind keybind)
    {
        keybinds.add(keybind);
        if(window != null) window.inputManager.keybinds.add(keybind);
    }

    public void addJoybind(Joybind joybind)
    {
        joybinds.add(joybind);
        if(window != null) window.inputManager.joybinds.add(joybind);
    }

    public void addMousebind(Mousebind mousebind)
    {
        mousebinds.add(mousebind);
        if(window != null) window.inputManager.mousebinds.add(mousebind);
    }

    public void removeKeybind(Keybind keybind)
    {
        keybinds.remove(keybind);
        if(window != null) window.inputManager.keybinds.remove(keybind);
    }

    public void removeJoybind(Joybind joybind)
    {
        joybinds.remove(joybind);
        if(window != null) window.inputManager.joybinds.remove(joybind);
    }

    public void removeMousebind(Mousebind mousebind)
    {
        mousebinds.remove(mousebind);
        if(window != null) window.inputManager.mousebinds.remove(mousebind);
    }

    public void setPhysicsPollsPerSecond(int pps)
    {
        physicsPollsPerSecond = pps;
        physicsUpdate.setFrequency(pps);
    }

    public void setUpdatesPerSecond(int ups)
    {
        updatesPerSecond = ups;
        gameUpdate.setFrequency(ups);
    }

    private void registerInputs()
    {
        if(window == null) return;

        window.inputManager.keybinds.addAll(keybinds);
        window.inputManager.joybinds.addAll(joybinds);
        window.inputManager.mousebinds.addAll(mousebinds);
    }

    private void unregisterInputs()
    {
        if(window == null) return;

        window.inputManager.keybinds.removeAll(keybinds);
        window.inputManager.joybinds.removeAll(joybinds);
        window.inputManager.mousebinds.removeAll(mousebinds);
    }
}
