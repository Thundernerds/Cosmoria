package net.comsoria.engine.libraryImplementation;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface AudioLibrary {
    AudioLibrary AL = new OpenAL();

    void bufferData(int name, Format format, ShortBuffer data, int frequency);
    void deleteBuffers(int buffer);
    int genBuffer();
    long openDevice(ByteBuffer data);
    int createCapabilites(long device);
    long createContext(long device, IntBuffer data);
    void makeContextCurrent(long context);
    void createCapabilites(int deviceCaps);
    void destroyContext(long context);
    void closeDevice(long device);
    void setDistanceModel(Model model);
    void listener(Parameter param, float x, float y, float z);
    void listener(Parameter param, float[] values);
    int genSource();
    void setSourceProperty(int id, SourceParam param, boolean value);
    void setSourceProperty(int id, SourceParam param, int value);
    void setSourceProperty(int id, SourceParam param, float value);
    void setSourceProperty(int id, SourceParam param, float x, float y, float z);
    void play(int source);
    void pause(int source);
    void stop(int source);
    void delete(int source);
    boolean isPlaying(int source);

    enum Format {
        MONO_16,
        STEREO_16
    }

    enum Parameter {
        Position,
        Velocity,
        Orientation
    }

    enum SourceParam {
        Looping,
        Relative,
        Buffer,
        Position,
        Velocity,
        Gain
    }

    enum Model {
        Exponent
    }
}
