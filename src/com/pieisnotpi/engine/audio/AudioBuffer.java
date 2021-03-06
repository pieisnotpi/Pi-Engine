package com.pieisnotpi.engine.audio;

import com.pieisnotpi.engine.output.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;

public class AudioBuffer
{
    public int channels, sampleRate, sampleLength, bufferID;
    private ShortBuffer samples;
    private String name;

    public AudioBuffer(String path)
    {
        path = path.replaceAll("\\\\", "/");
        name = path.substring(path.lastIndexOf('/') + 1);

        samples = getSamples(path);
        bufferID = alGenBuffers();
    }

    public AudioBuffer(ShortBuffer buffer, int sampleRate, int channels)
    {
        this.samples = buffer;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.sampleLength = buffer.limit();
        bufferID = alGenBuffers();
    }

    public AudioBuffer clipSeconds(float start)
    {
        return clipSeconds(start, (float) sampleLength/sampleRate);
    }

    public AudioBuffer clipSeconds(float start, float end)
    {
        return clip((int) (start*sampleRate), (int) (end*sampleRate));
    }

    public AudioBuffer clip(int start)
    {
        return clip(start, sampleLength);
    }

    public AudioBuffer clip(int start, int end)
    {
        sampleLength = end - start;

        ShortBuffer newBuffer = BufferUtils.createShortBuffer(sampleLength);

        samples.position(start);
        while(samples.position() < end) newBuffer.put(samples.get());
        samples.clear();
        samples = newBuffer;

        return this;
    }

    public AudioBuffer bind()
    {
        if(samples != null)
        {
            alBufferData(bufferID, channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, samples, sampleRate);
            int error = alGetError();
            if(error != AL_NO_ERROR) Logger.AUDIO.err(String.format("Error with file '%s': %s", name, alGetString(error)));
        }

        return this;
    }

    private ShortBuffer getSamples(String path)
    {
        ByteBuffer bytes;

        try
        {
            Path p = Paths.get(AudioPlayer.class.getResource(path).toURI());
            SeekableByteChannel channel = Files.newByteChannel(p);

            bytes = ByteBuffer.allocateDirect((int) channel.size() + 1);
            while(channel.read(bytes) != -1) {/**/}

            bytes.flip();

            IntBuffer ec = BufferUtils.createIntBuffer(1);

            long decoder = stb_vorbis_open_memory(bytes, ec, null);
            assert decoder != 0;

            STBVorbisInfo info = STBVorbisInfo.malloc();

            stb_vorbis_get_info(decoder, info);

            channels = info.channels();
            sampleRate = info.sample_rate();
            sampleLength = stb_vorbis_stream_length_in_samples(decoder);

            ShortBuffer pcm = BufferUtils.createShortBuffer(sampleLength);

            pcm.limit(sampleLength = stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm)*channels);

            stb_vorbis_close(decoder);

            info.free();

            return pcm;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
