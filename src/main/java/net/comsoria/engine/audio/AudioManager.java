package net.comsoria.engine.audio;

import net.comsoria.engine.LibraryImplementation.AudioLibrary;
import net.comsoria.engine.view.Camera;
import net.comsoria.engine.view.GLSL.matrices.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.system.MemoryUtil.NULL;

public class AudioManager {
    private long device;
    private long context;
    private AudioNode listener;
    private final List<AudioBuffer> soundBufferList;
    private final Map<String, AudioSource> soundSourceMap;
    private final Matrix4f cameraMatrix;

    public AudioManager() {
        soundBufferList = new ArrayList<>();
        soundSourceMap = new HashMap<>();
        cameraMatrix = new Matrix4f();
    }

    public void init() throws Exception {
        this.device = AudioLibrary.AL.openDevice(null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
//        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        int deviceCaps = AudioLibrary.AL.createCapabilites(device);
        this.context = AudioLibrary.AL.createContext(device, null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        AudioLibrary.AL.makeContextCurrent(context);
//        alcMakeContextCurrent(context);
//        AL.createCapabilities(deviceCaps);
        AudioLibrary.AL.createCapabilites(deviceCaps);
    }

    public void addSoundSource(String name, AudioSource soundSource) {
        this.soundSourceMap.put(name, soundSource);
    }

    public AudioSource getSoundSource(String name) {
        return this.soundSourceMap.get(name);
    }

    public void playSoundSource(String name) {
        AudioSource soundSource = this.soundSourceMap.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(String name) {
        this.soundSourceMap.remove(name);
    }

    public void addSoundBuffer(AudioBuffer soundBuffer) {
        this.soundBufferList.add(soundBuffer);
    }

    public AudioNode getListener() {
        return this.listener;
    }

    public void setListener(AudioNode listener) {
        this.listener = listener;
    }

    public void updateListenerPosition(Camera camera) {
        // Update camera matrix with camera data
        Transformation.updateGenericViewMatrix(camera.position, camera.rotation, cameraMatrix);

        listener.setPosition(camera.position);
        Vector3f at = new Vector3f();
        cameraMatrix.positiveZ(at).negate();
        Vector3f up = new Vector3f();
        cameraMatrix.positiveY(up);
        listener.setOrientation(at, up);
    }

    public void setAttenuationModel(AudioLibrary.Model model) {
        AudioLibrary.AL.setDistanceModel(model);
    }

    public void cleanup() {
        for (AudioSource soundSource : soundSourceMap.values()) {
            soundSource.cleanup();
        }
        soundSourceMap.clear();
        for (AudioBuffer soundBuffer : soundBufferList) {
            soundBuffer.cleanup();
        }
        soundBufferList.clear();
        if (context != NULL) {
            AudioLibrary.AL.destroyContext(context);
        }
        if (device != NULL) {
            AudioLibrary.AL.closeDevice(device);
        }
    }
}