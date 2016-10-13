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
    private AudioPlayer player = PiEngine.gameInstance.player;
    private List<AudioSource> sources = new ArrayList<>();
    private Scene scene;

    public AudioListener(Scene scene)
    {
        this.scene = scene;

        /*int length = 1024, jump = (Short.MAX_VALUE - Short.MIN_VALUE)/length;
        ShortBuffer samples = BufferUtils.createShortBuffer(length);
        for(int i = 0; i < length; i++) samples.put((short) (Short.MIN_VALUE + i*jump));
        samples.flip();
        AudioBuffer buffer = new AudioBuffer(samples, 256, 1).bind();
        sources.add(new AudioSource(buffer, player).setRelative(true).setLooping(true).play());*/

        /*AudioBuffer file = new AudioBuffer("/assets/sounds/test2.ogg").bind();
        sources.add(new AudioSource(file, player).setPosition(0, 0, 0).setLooping(true).setRelative(true).play());*/
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
