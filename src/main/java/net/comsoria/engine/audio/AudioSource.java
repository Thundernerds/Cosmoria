package net.comsoria.engine.audio;

import net.comsoria.engine.libraryImplementation.AudioLibrary;
import org.joml.Vector3f;

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
    }

    public void setSpeed(Vector3f speed) {
        AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Velocity, speed.x, speed.y, speed.z);
    }

    public void setGain(float gain) {
        AudioLibrary.AL.setSourceProperty(sourceId, AudioLibrary.SourceParam.Gain, gain);
    }

    public void play() {
        AudioLibrary.AL.play(sourceId);
    }

    public boolean isPlaying() {
        return AudioLibrary.AL.isPlaying(sourceId);
    }

    public void pause() {
        AudioLibrary.AL.pause(sourceId);
    }

    public void stop() {
        AudioLibrary.AL.stop(sourceId);
    }

    public void cleanup() {
        stop();
        AudioLibrary.AL.delete(sourceId);
    }
}