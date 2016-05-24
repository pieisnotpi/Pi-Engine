package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.Mousebind;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
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
    public World world;
    public Text fps, pps;
    public Window window;
    public String name = "Scene";
    public Color clearColor = new Color(0.5f, 0.5f, 0.5f);
    public List<Camera> cameras = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>(20);
    public List<Renderable> renderables = new ArrayList<>(100);
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public Vector2f lastCursorPos = new Vector2f();

    private GameUpdate physicsUpdate, gameUpdate;
    protected int physicsPollsPerSecond = 60, updatesPerSecond = 60;

    protected Scene() {}

    public boolean shouldUpdate = true, shouldUpdatePhysics = true;

    public void onLeftClick() { gameObjects.forEach(GameObject::onLeftClick); }
    public void onRightClick() { gameObjects.forEach(GameObject::onRightClick); }
    public void onLeftRelease() { gameObjects.forEach(GameObject::onLeftRelease); }
    public void onRightRelease() { gameObjects.forEach(GameObject::onRightRelease); }
    public void onMiddleClick() { gameObjects.forEach(GameObject::onMiddleClick); }
    public void onMiddleRelease() { gameObjects.forEach(GameObject::onMiddleRelease); }
    public void onKeyPressed(int key, int mods) { gameObjects.forEach(gameObject -> gameObject.onKeyPressed(key, mods)); }
    public void onKeyReleased(int key, int mods) { gameObjects.forEach(gameObject -> gameObject.onKeyReleased(key, mods)); }
    public void onScroll(float xAmount, float yAmount) { gameObjects.forEach(gameObject -> gameObject.onScroll(xAmount, yAmount)); }
    public void onMouseMovement(Vector2f cursorPos) { gameObjects.forEach(gameObject -> gameObject.onMouseMovement(cursorPos)); lastCursorPos.set(cursorPos); }

    public void onWindowResize(Vector2i res)
    {
        gameObjects.forEach(gameObject -> gameObject.onWindowResize(res));
    }

    public void init()
    {
        fps = new Text("", 12, 0, 0.85f, 0.8f, PiEngine.ORTHO_ID, this);
        pps = new Text("", 12, 0, -0.95f, 0.8f, PiEngine.ORTHO_ID, this);
        world = new World(new Vec2(0, -9.81f));

        fps.alignmentID = Text.LEFT;
        pps.alignmentID = Text.LEFT;

        physicsUpdate = new GameUpdate(physicsPollsPerSecond, this::updatePhysics, () ->
        {
            String time = "" + (float) physicsUpdate.totalTimeTaken/physicsUpdate.updates;
            if(time.length() >= 5) time = time.substring(0, 5);
            pps.setText(physicsUpdate.updates + "pps/" + time + "mspp");
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
        unregisterInputs();
        this.window = window;
        registerInputs();

        if(window != null) onWindowResize(window.res);
    }

    public void addRenderable(Renderable renderable)
    {
        if(!renderables.contains(renderable)) renderables.add(renderable);
    }

    public void removeRenderable(Renderable renderable)
    {
        renderables.remove(renderable);
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
