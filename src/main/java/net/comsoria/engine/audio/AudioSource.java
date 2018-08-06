package net.comsoria.engine.audio;

import net.comsoria.engine.LibraryImplementation.AudioLibrary;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {
    private final int sourceId;

    public AudioSource(boolean loop, boolean relative) {
        this.sourceId = AudioLibrary.AL.genSource();

        if (loop) {
            AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Looping, true);
        }
        if (relative) {
            AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Relative, true);
        }
    }

    public void setBuffer(int bufferId) {
        stop();
        AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Buffer, bufferId);
    }

    public void setPosition(Vector3f position) {
        AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Position, position.x, position.y, position.z);
//        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public void setSpeed(Vector3f speed) {
        AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Velocity, speed.x, speed.y, speed.z);
//        alSource3f(sourceId, AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    public void setGain(float gain) {
        AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Gain, gain);
//        alSourcef(sourceId, AL_GAIN, gain);
    }

    public void setProperty(int param, float value) {
//        alSourcef(sourceId, param, value);
    }

    public void play() {
//        alSourcePlay(sourceId);
        AudioLibrary.AL.play(sourceId);
    }

    public boolean isPlaying() {
        return AudioLibrary.AL.isPlaying(sourceId);
    }

    public void pause() {
//        alSourcePause(sourceId);
        AudioLibrary.AL.pause(sourceId);
    }

    public void stop() {
//        alSourceStop(sourceId);
        AudioLibrary.AL.stop(sourceId);
    }

    public void cleanup() {
        stop();
//        alDeleteSources(sourceId);
        AudioLibrary.AL.delete(sourceId);
    }
}