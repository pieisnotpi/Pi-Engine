package com.pieisnotpi.engine.scene;

import com.pieisnotpi.engine.input.joystick.Joystick;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.window.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GameObject<m extends Renderable>
{
    public GameObject() {}
    
    public GameObject(Mesh<m> mesh)
    {
        this.mesh = mesh;
    }
    
    protected Mesh<m> mesh;
    protected GameObject parent;
    protected List<GameObject> children = new ArrayList<>();
    protected Transform transform = new Transform();
    protected Vector3f size = new Vector3f();
    protected Scene scene;

    public void update(float timeStep) {}
    public void drawUpdate(float timeStep) {}
    public void setScene(Scene scene) { this.scene = scene; }

    public void onJoystickConnect(Joystick joystick) {}
    public void onJoystickDisconnect(Joystick joystick) {}
    public void onLeftClick() {}
    public void onLeftHold() {}
    public void onLeftRelease() {}
    public void onRightClick() {}
    public void onRightHold() {}
    public void onRightRelease() {}
    public void onMiddleClick() {}
    public void onMiddleHold() {}
    public void onMiddleRelease() {}
    public void onKeyPressed(int key, int mods) {}
    public void onKeyReleased(int key, int mods) {}
    public void onWindowResize(Vector2i res) {}
    public void onScroll(float xAmount, float yAmount) {}
    public void onMouseEntered() {}
    public void onMouseExited() {}
    public void onMouseMovement(Vector2f scaled, Vector2i unscaled) {}
    public void onWindowChanged(Window oldWindow, Window newWindow) {}

    public boolean isRegistered() { return scene != null; }
    public boolean isPointInsideObject(Vector2f point) { return false; }

    public float getWidth() { return size.x*transform.scale.x; }
    public float getHeight() { return size.y*transform.scale.y; }
    public float getDepth() { return size.z*transform.scale.z; }
    public Mesh<m> getMesh() { return mesh; }
    public Transform getTransform() { return transform; }
    public Vector3f getSize() { return size; }
    public Scene getScene() { return scene; }
    
    public void setParent(GameObject parent)
    {
        if(this.parent != null) this.parent.removeChild(this);
        if(parent != null) parent.addChild(this);
    }
    
    public void addChild(GameObject child)
    {
        child.parent = this;
        children.add(child);
        transform.addChild(child.transform);
        if(scene != null) scene.addGameObject(child);
    }
    
    public void removeChild(GameObject child)
    {
        if(!children.contains(child)) return;
        
        child.parent = null;
        children.remove(child);
        transform.removeChild(child.transform);
        if(scene != null) scene.removeGameObject(child);
    }
    
    public void setMesh(Mesh<m> mesh)
    {
        if(scene != null)
        {
            if(this.mesh != null) scene.removeMesh(mesh);
            if(mesh != null) scene.addMesh(mesh);
        }
        
        this.mesh = mesh;
    }
    
    public Mesh<m> createMesh(Material material, MeshConfig config)
    {
        setMesh(new Mesh<>(material, transform, config));
        return mesh;
    }
    
    public Mesh<m> createMesh(Mesh original)
    {
        return this.mesh = new Mesh<>(original, transform);
    }
    
    public void onRegister(Scene scene)
    {
        this.scene = scene;
        
        if(this.mesh != null) scene.addMesh(this.mesh);
        else if(!getClass().isAnnotationPresent(IgnoreMeshWarning.class))
            Logger.SYSTEM.debugErr(String.format("Object added without associated mesh%n\t%s%n", toString()));
        
        children.forEach(scene::addGameObject);
    }
    
    public void onUnregister()
    {
        if(this.scene != null && this.mesh != null) this.scene.removeMesh(mesh);
        if(this.scene != null) children.forEach(scene::removeGameObject);
    
        this.scene = null;
        
    }
    
    public void destroy()
    {
        if(parent != null) parent.removeChild(this);
        else scene.removeGameObject(this);
        while(children.size() > 0)
        {
            GameObject child = children.remove(0);
            child.destroy();
        }
    }
}
