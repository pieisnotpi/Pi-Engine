package com.pieisnotpi.engine.audio;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;

public class AudioListener
{
    private Vector3f pos = new Vector3f(), rot = new Vector3f();
    private AudioPlayer player = PiEngine.instance.player;
    private List<AudioSource> sources = new ArrayList<>();
    private Scene scene;

    public AudioListener(Scene scene)
    {
        this.scene = scene;

        /*AudioFile file = new AudioFile("/assets/test2.ogg")*//*.clipSeconds(0.5f)*//*.bind();
        sources.add(new AudioSource(file, player).setPosition(0, 0, 0).setLooping(true).setRelative(false).play());*/
    }

    public void setPosition(float x, float y, float z)
    {
        if(pos.x == x && pos.y == y && pos.z == z) return;

        pos.set(x, y, z);
        alListener3f(AL_POSITION, x, y, z);
    }

    public void setPosition(Vector3f pos)
    {
        if(pos.equals(this.pos)) return;

        this.pos.set(pos);
        alListener3f(AL_POSITION, pos.x, pos.y, pos.z);
    }

    public void setRotation(float xr, float yr, float zr)
    {
        if(pos.x == xr && pos.y == yr && pos.z == zr) return;

        rot.set(xr, yr, zr);
        alListener3f(AL_ORIENTATION, xr, yr, zr);
    }

    public void setRotation(Vector3f rot)
    {
        this.rot = rot;
        alListener3f(AL_ORIENTATION, rot.x, rot.y, rot.z);
    }
}
