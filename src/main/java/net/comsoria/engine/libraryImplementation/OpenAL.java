package net.comsoria.engine.libraryImplementation;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE;
import static org.lwjgl.openal.ALC10.*;

public class OpenAL implements AudioLibrary {
    private List<ALCCapabilities> capabilities = new ArrayList<>();

    @Override public void bufferData(int name, Format format, ShortBuffer data, int frequency) {
        alBufferData(
                name,
                format == Format.MONO_16? AL_FORMAT_MONO16:AL_FORMAT_STEREO16,
                data,
                frequency
        );
    }

    @Override public void deleteBuffers(int buffer) {
        alDeleteBuffers(buffer);
    }
    @Override public int genBuffer() {
        return alGenBuffers();
    }

    @Override public long openDevice(ByteBuffer data) {
        return alcOpenDevice(data);
    }
    @Override public int createCapabilites(long device) {
        this.capabilities.add(ALC.createCapabilities(device));
        return this.capabilities.size() - 1;
    }
    @Override public long createContext(long device, IntBuffer data) {
        return alcCreateContext(device, data);
    }
    @Override public void makeContextCurrent(long context) {
        alcMakeContextCurrent(context);
    }
    @Override public void createCapabilites(int deviceCaps) {
        org.lwjgl.openal.AL.createCapabilities(this.capabilities.get(deviceCaps));
    }

    @Override public void destroyContext(long context) {
        alcDestroyContext(context);
    }
    @Override public void closeDevice(long device) {
        alcCloseDevice(device);
    }

    private int getModelInt(Model model) {
        switch (model) {
            case Exponent:
                return AL_EXPONENT_DISTANCE;
        }

        return -1;
    }
    @Override public void setDistanceModel(Model model) {
        alDistanceModel(getModelInt(model));
    }

    private int getParamInt(Parameter parameter) {
        switch (parameter) {
            case Position:
                return AL_POSITION;
            case Velocity:
                return AL_VELOCITY;
            case Orientation:
                return AL_ORIENTATION;
        }

        return -1;
    }
    @Override public void listener(Parameter param, float x, float y, float z) {
        alListener3f(getParamInt(param), x, y, z);
    }
    @Override public void listener(Parameter param, float[] values) {
        alListenerfv(getParamInt(param), values);
    }

    @Override public int genSource() {
        return alGenSources();
    }

    private int getParamInt(SourceParam parameter) {
        switch (parameter) {
            case Looping:
                return AL_LOOPING;
            case Relative:
                return AL_SOURCE_RELATIVE;
            case Buffer:
                return AL_BUFFER;
            case Position:
                return AL_POSITION;
            case Velocity:
                return AL_VELOCITY;
            case Gain:
                return AL_GAIN;
        }

        return -1;
    }
    @Override public void setSourceProperty(int id, SourceParam param, boolean value) {
        this.setSourceProperty(id, param, value? AL_TRUE:AL_FALSE);
    }
    @Override public void setSourceProperty(int id, SourceParam param, int value) {
        alSourcei(id, getParamInt(param), value);
    }
    @Override public void setSourceProperty(int id, SourceParam param, float value) {
        alSourcef(id, getParamInt(param), value);
    }
    @Override public void setSourceProperty(int id, SourceParam param, float x, float y, float z) {
        alSource3f(id, getParamInt(param), x, y, z);
    }

    @Override public void play(int source) {
        alSourcePlay(source);
    }
    @Override public void pause(int source) {
        alSourcePause(source);
    }
    @Override public void stop(int source) {
        alSourceStop(source);
    }

    @Override public void delete(int source) {
        alDeleteSources(source);
    }

    @Override
    public boolean isPlaying(int source) {
        return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
    }
}
