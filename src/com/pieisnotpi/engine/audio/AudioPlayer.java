package com.pieisnotpi.engine.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.ALC_MONO_SOURCES;
import static org.lwjgl.openal.ALC11.ALC_STEREO_SOURCES;

public class AudioPlayer
{
    public int frequency, refreshRate, monoSources, stereoSources;
    public boolean sync;
    private long deviceID, context;
    private ALCCapabilities caps;
    private String defaultDevice;


    private static final long NULL = 0;

    public AudioPlayer()
    {
        deviceID = alcOpenDevice((ByteBuffer) null);
        assert deviceID != NULL;

        caps = ALC.createCapabilities(deviceID);
        assert caps.OpenALC10 && caps.OpenALC11 && caps.ALC_EXT_EFX;

        context = alcCreateContext(deviceID, (IntBuffer) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(caps);

        frequency = alcGetInteger(deviceID, ALC_FREQUENCY);
        refreshRate = alcGetInteger(deviceID, ALC_REFRESH);
        monoSources = alcGetInteger(deviceID, ALC_MONO_SOURCES);
        stereoSources = alcGetInteger(deviceID, ALC_STEREO_SOURCES);
        sync = alcGetInteger(deviceID, ALC_SYNC) == ALC_TRUE;
    }

    public void destroy()
    {
        alcDestroyContext(context);
        alcCloseDevice(deviceID);
    }

    protected void finalize() throws Throwable
    {
        alcDestroyContext(context);
        alcCloseDevice(deviceID);
        super.finalize();
    }
}
