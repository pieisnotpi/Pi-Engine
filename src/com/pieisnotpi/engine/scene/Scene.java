package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.audio.AudioListener;
import com.pieisnotpi.engine.input.joystick.Joybind;
import com.pieisnotpi.engine.input.joystick.Joystick;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.mouse.Mousebind;
import com.pieisnotpi.engine.rendering.Light;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.types.tex_shader.TexQuad;
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
    public List<Mesh> unsortedMeshes = new ArrayList<>(100), sortedMeshes = new ArrayList<>(10);
    public List<Keybind> keybinds = new ArrayList<>();
    public List<Joybind> joybinds = new ArrayList<>();
    public List<Mousebind> mousebinds = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();
    public Mesh<TexQuad> viewMesh;

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

    public Scene init()
    {
        if(PiEngine.debug)
        {
            fps = new Text(SystemFont.getFont("Arial", 28, SystemFont.PLAIN, false), "", new Vector3f(0, 0, -0.1f), Camera.ORTHO2D_R);
            //fps.transform.setScale(0.01f, 0.01f, 0.01f);
            //fps.setAlignment(UiObject.HAlignment.LEFT, UiObject.VAlignment.TOP, 32, -32);
            fps.setAlignment(UiObject.HAlignment.LEFT, UiObject.VAlignment.TOP, 8, -8);
            addGameObject(fps);
        }

        //listener = new AudioListener(this);

        gameUpdate = new GameUpdate(60, this::update).setName(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));

        PiEngine.gameInstance.registerUpdate(gameUpdate);

        initialized = true;

        return this;
    }

    public void update(float timeStep)
    {
        if(!shouldUpdate || window == null) return;

        gameObjects.forEach(go -> go.update(timeStep));
    }

    public void drawUpdate(float timeStep)
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
            unsortedMeshes.forEach(m -> m.material.shader.addUnsortedMesh(m));
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
     * @param mesh Mesh to be registered.
     */
    
    public void addMesh(Mesh mesh)
    {
        Material m = mesh.material;
        if(!mesh.shouldSort())
        {
            unsortedMeshes.add(mesh);
            if(window != null) m.shader.addUnsortedMesh(mesh);
        }
        else sortedMeshes.add(mesh);
    }
    
    /**
     * This should not be used without the context of a GameObject.
     * All meshes should be registered through an associated GameObject
     *
     * @param mesh Mesh to be unregistered.
     */
    
    public void removeMesh(Mesh mesh)
    {
        if(!mesh.shouldSort())
        {
            unsortedMeshes.remove(mesh);
            if(window != null) mesh.material.shader.removeUnsortedMesh(mesh);
        }
        else sortedMeshes.remove(mesh);
    }

    public boolean isInitialized() { return initialized; }
}
