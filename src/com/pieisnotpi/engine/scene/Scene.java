package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.audio.AudioListener;
import com.pieisnotpi.engine.input.joystick.Joybind;
import com.pieisnotpi.engine.input.joystick.Joystick;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.mouse.Mousebind;
import com.pieisnotpi.engine.rendering.Light;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.ui.text.Text;
import com.pieisnotpi.engine.ui.text.font.SystemFont;
import com.pieisnotpi.engine.updates.GameUpdate;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The base class for scenes, the foundation of the engine's user interface.
 * All scenes used by the game will extend this class.
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
    public Map<Integer, List<Renderable>> renderables = new TreeMap<>();
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();

    private GameUpdate gameUpdate;

    public boolean shouldUpdate = true;
    private boolean initialized = false;

    public void onJoystickConnect(Joystick joystick) { gameObjects.forEach(g -> g.onJoystickConnect(joystick)); }
    public void onJoystickDisconnect(Joystick joystick) { gameObjects.forEach(g -> g.onJoystickDisconnect(joystick)); }
    public void onLeftClick() { gameObjects.forEach(GameObject::onLeftClick); }
    public void onLeftHold() { gameObjects.forEach(GameObject::onLeftHold); }
    public void onLeftRelease() { gameObjects.forEach(GameObject::onLeftRelease); }
    public void onRightClick() { gameObjects.forEach(GameObject::onRightClick); }
    public void onRightHold() { gameObjects.forEach(GameObject::onRightHold); }
    public void onRightRelease() { gameObjects.forEach(GameObject::onRightRelease); }
    public void onMiddleClick() { gameObjects.forEach(GameObject::onMiddleClick); }
    public void onMiddleHold() { gameObjects.forEach(GameObject::onMiddleHold); }
    public void onMiddleRelease() { gameObjects.forEach(GameObject::onMiddleRelease); }
    public void onKeyPressed(int key, int mods) { gameObjects.forEach(g -> g.onKeyPressed(key, mods)); }
    public void onKeyReleased(int key, int mods) { gameObjects.forEach(g -> g.onKeyReleased(key, mods)); }
    public void onScroll(float xAmount, float yAmount) { gameObjects.forEach(g -> g.onScroll(xAmount, yAmount)); }
    public void onMouseMovement(Vector2f scaled, Vector2i unscaled) { gameObjects.forEach(g -> g.onMouseMovement(scaled, unscaled)); }

    public void onWindowResize(Vector2i res)
    {
        gameObjects.forEach(gameObject -> gameObject.onWindowResize(res));
    }

    public Scene init() throws Exception
    {
        if(PiEngine.debug)
        {
            fps = new Text(SystemFont.getFont("Arial", 28, SystemFont.PLAIN, false), "", new Vector3f(0, 0, -0.1f), Camera.ORTHO2D_R);
            fps.setAlignment(UiObject.HAlignment.LEFT, UiObject.VAlignment.TOP, 8, -8);
            fps.setOutlineSize(1);
            fps.setOutlineSmoothing(false);
            addGameObject(fps);
        }

        gameUpdate = new GameUpdate(60, this::update).setName(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));

        PiEngine.gameInstance.registerUpdate(gameUpdate);

        initialized = true;

        return this;
    }

    public void update(float timeStep) throws Exception
    {
        if(!shouldUpdate || window == null) return;

        gameObjects.forEach(go -> go.update(timeStep));
    }

    public void drawUpdate(float timeStep) throws Exception
    {
        if(window == null) return;

        gameObjects.forEach(go -> go.drawUpdate(timeStep));
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
        }
    }

    public void addCamera(Camera camera)
    {
        cameras.add(camera);
        addGameObject(camera);
    }

    public void removeCamera(Camera camera)
    {
        cameras.remove(camera);
        removeGameObject(camera);
    }

    public void addGameObject(GameObject object)
    {
        if(!gameObjects.contains(object))
        {
            gameObjects.add(object);
            object.onRegister(this);
        }
    }
    
    public void removeGameObject(GameObject object)
    {
        gameObjects.remove(object);
        object.onUnregister();
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
    
    /**
     * This should not be used without the context of a GameObject.
     * All meshes should be registered through an associated GameObject
     *
     * @param r Renderable to be registered.
     */
    
    public void addRenderable(Renderable r)
    {
        List<Renderable> d = renderables.computeIfAbsent(r.getPass(), k -> new ArrayList<>());
    
        d.add(r);
        d.sort(Renderable.comparator);
    }
    
    /**
     * This should not be used without the context of a GameObject.
     * All meshes should be registered through an associated GameObject
     *
     * @param r Renderable to be unregistered.
     */
    
    public void removeRenderable(Renderable r)
    {
        List<Renderable> d = renderables.get(r.getPass());
        if(d != null) d.remove(r);
    }

    public boolean isInitialized() { return initialized; }
}
