package com.pieisnotpi.engine.audio;

import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class AudioSource
{
    private AudioFile file;
    private Vector3f pos = new Vector3f();
    private boolean relative = false, looping = false;
    public int sourceID;

    public AudioSource(AudioFile file, AudioPlayer player)
    {
        this.file = file;
        sourceID = alGenSources();
        alSourcei(sourceID, AL_BUFFER, file.bufferID);
    }

    public AudioSource setPosition(float x, float y, float z)
    {
        if(x == pos.x && y == pos.y && z == pos.z) return this;

        pos.set(x, y, z);
        alSource3f(sourceID, AL_POSITION, x, y, z);

        return this;
    }

    public AudioSource setPosition(Vector3f pos)
    {
        if(pos.equals(this.pos)) return this;

        this.pos = pos;
        alSource3f(sourceID, AL_POSITION, pos.x, pos.y, pos.z);

        return this;
    }

    public AudioSource setLooping(boolean looping)
    {
        this.looping = true;
        alSourcei(sourceID, AL_LOOPING, AL_TRUE);
        return this;
    }

    public AudioSource setRelative(boolean relative)
    {
        if(this.relative = relative) alSourcei(sourceID, AL_SOURCE_RELATIVE, AL_TRUE);
        else alSourcei(sourceID, AL_SOURCE_ABSOLUTE, AL_TRUE);
        return this;
    }

    public AudioSource play()
    {
        alSourcePlay(sourceID);
        return this;
    }

    public AudioSource pause()
    {
        alSourcePause(sourceID);
        return this;
    }

    public AudioSource stop()
    {
        alSourceStop(sourceID);
        return this;
    }

    public boolean isPlaying()
    {
        return alGetSourcei(sourceID, AL_PLAYING) == AL_TRUE;
    }

    public boolean isPaused()
    {
        return alGetSourcei(sourceID, AL_PAUSED) == AL_TRUE;
    }

    public boolean isStopped()
    {
        return alGetSourcei(sourceID, AL_STOPPED) == AL_TRUE;
    }
}
