package net.comsoria.engine.audio;

import net.comsoria.engine.LibraryImplementation.AudioLibrary;
import org.joml.Vector3f;

public class AudioNode {
    public AudioNode() {
        this(new Vector3f(0, 0, 0));
    }

    public AudioNode(Vector3f position) {
        AudioLibrary.AL.listener(AudioLibrary.Parameter.Position, position.x, position.y, position.z);
        AudioLibrary.AL.listener(AudioLibrary.Parameter.Velocity, 0, 0, 0);
    }

    public void setSpeed(Vector3f speed) {
        AudioLibrary.AL.listener(AudioLibrary.Parameter.Velocity, speed.x, speed.y, speed.z);
    }

    public void setPosition(Vector3f position) {
        AudioLibrary.AL.listener(AudioLibrary.Parameter.Position, position.x, position.y, position.z);
    }

    public void setOrientation(Vector3f at, Vector3f up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        AudioLibrary.AL.listener(AudioLibrary.Parameter.Orientation, data);
    }
}