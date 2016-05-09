package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.game_objects.GameObject;
import com.pieisnotpi.engine.rendering.Camera;
import com.pieisnotpi.engine.rendering.Color;
import com.pieisnotpi.engine.rendering.Window;
import com.pieisnotpi.engine.rendering.renderable_types.Renderable;
import com.pieisnotpi.engine.rendering.shapes.types.colored.ColorQuad;
import com.pieisnotpi.engine.rendering.ui.text.Text;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public abstract class Scene
{
    public World world;
    public Text fps, pps, pausedText;
    public Window window;
    public Color clearColor = new Color(0, 0, 0);
    public List<Camera> cameras = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>();
    public List<Renderable> renderables;
    public Vector2f lastCursorPos = new Vector2f();
    public ColorQuad tint;

    protected Scene() {}

    public boolean shouldUpdate = true, shouldUpdatePhysics = true, paused = false;

    private Vector2f motionless = new Vector2f(0, 0);

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
        float ratio = (float) res.x/res.y;

        tint.points[0].set(-ratio, -ratio, 0.7f);
        tint.points[1].set(ratio, -ratio, 0.7f);
        tint.points[2].set(-ratio, ratio, 0.7f);
        tint.points[3].set(ratio, ratio, 0.7f);

        gameObjects.forEach(gameObject -> gameObject.onWindowResize(res));
    }

    protected void allocateBuffer(int capacity)
    {
        renderables = new Vector<>(capacity);
    }

    public void init()
    {
        if(renderables == null) allocateBuffer(100);
        fps = new Text("", 12, 0, 0.85f, 0.8f, PiEngine.ORTHO_ID, this);
        pps = new Text("", 12, 0, -0.95f, 0.8f, PiEngine.ORTHO_ID, this);
        pausedText = new Text("PAUSED", 16, 0, 0, 0.8f, new Color(1, 0, 0), new Color(0, 0, 0, 1), PiEngine.ORTHO_ID, this);
        world = new World(new Vec2(0, -9.81f));

        pausedText.setX(pausedText.getX() - pausedText.getWidth()/2);
        pausedText.setY(pausedText.getY() - pausedText.getHeight()/2);
        pausedText.disable();

        tint = new ColorQuad(0, 0, 0, 0, 0, 0, new Color(0, 0, 0, 0.4f), PiEngine.ORTHO_ID, this);
        tint.transparent = true;
        tint.unregister();

        fps.alignmentID = Text.LEFT;
        pps.alignmentID = Text.LEFT;
    }

    public void update()
    {
        if(!shouldUpdate) return;

        gameObjects.forEach(GameObject::update);
    }

    public void updatePhysics()
    {
        if(!shouldUpdatePhysics) return;

        world.step(1/60f, 20, 10);
        gameObjects.forEach(GameObject::physicsUpdate);
    }

    public void setWindow(Window window)
    {
        cameras.forEach(Camera::unregisterInputs);

        this.window = window;

        cameras.forEach(Camera::registerInputs);

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

    public void togglePause()
    {
        paused = !paused;

        for(Camera camera : cameras)
            for(int i = 0; i < camera.joybinds.size(); i++)
                if(i != camera.pauseSlot && i != camera.fullscreenSlot)
                    camera.joybinds.get(i).enabled = !paused;

        shouldUpdate = !paused;
        shouldUpdatePhysics = !paused;

        if(paused) pausedText.enable();
        else pausedText.disable();

        tint.toggle();
    }
}
