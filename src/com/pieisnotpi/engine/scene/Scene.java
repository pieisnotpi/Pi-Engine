package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.audio.AudioListener;
import com.pieisnotpi.engine.input.Joybind;
import com.pieisnotpi.engine.input.Joystick;
import com.pieisnotpi.engine.input.Keybind;
import com.pieisnotpi.engine.input.Mousebind;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Mesh;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import com.pieisnotpi.engine.updates.GameUpdate;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for scenes, the foundation of the engine's user interface.
 * All scenes used by the game should extend this class.
 */

public abstract class Scene
{
    public AudioListener listener;
    public Text fps;
    public Window window;
    public String name = "Scene";
    public Color clearColor = new Color(0.5f, 0.5f, 0.5f);
    public List<Camera> cameras = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>(20);
    public List<Mesh> unsortedMeshes = new ArrayList<>(500);
    //public List<Renderable> unsortedBuffer = new ArrayList<>(100);
    //public List<Renderable> sortedBuffer = new ArrayList<>(100);
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public Vector2f lastCursorPos = new Vector2f();

    private GameUpdate gameUpdate;
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
        fps = new Text("", new Vector3f(0, 0, -0.1f), PiEngine.C_ORTHO2D_ID, this);
        fps.getMesh().scale(1.1f, 1.1f, 1);
        listener = new AudioListener(this);

        fps.setAlignment(GameObject.HAlignment.LEFT, GameObject.VAlignment.TOP, 10, -40);

        gameUpdate = new GameUpdate(60, this::update);

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

    public void setWindow(Window window)
    {
        gameObjects.forEach(g -> g.onWindowChanged(this.window, window));

        unregisterInputs();
        this.window = window;
        registerInputs();

        if(window != null)
        {
            onWindowResize(window.getWindowRes());
            unsortedMeshes.forEach(m ->
            {
                m.material.shader = window.shaders.get(m.material.shaderID);
                m.material.shader.addUnsortedMesh(m);
            });
            //sortedBuffer.forEach(r -> r.shader = window.shaders.get(r.getShaderID()));
        }
        else
        {
            unsortedMeshes.forEach(m -> m.material.shader = null);
            //sortedBuffer.forEach(m -> m.material.shader = null);
        }
    }

    public void addMesh(Mesh mesh)
    {
        Material m = mesh.material;
        if(window != null) m.shader = window.shaders.get(m.shaderID);
        else m.shader = null;

        //if(renderable.shouldBeSorted)
        //{
        //    if(!sortedBuffer.contains(renderable)) sortedBuffer.add(renderable);
        //}
        //else if(!unsortedBuffer.contains(renderable))
        {
            unsortedMeshes.add(mesh);
            if(m.shader != null) m.shader.addUnsortedMesh(mesh);
        }
    }

    public void removeMesh(Mesh mesh)
    {
        //if(renderable.shouldBeSorted) sortedBuffer.remove(renderable);
        //else
        {
            unsortedMeshes.remove(mesh);
            if(mesh.material.shader != null) mesh.material.shader.removeUnsortedMesh(mesh);
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
