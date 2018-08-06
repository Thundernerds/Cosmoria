package net.comsoria.controller;

import net.comsoria.engine.Scene;
import net.comsoria.engine.audio.AudioBuffer;
import net.comsoria.engine.audio.AudioNode;
import net.comsoria.engine.audio.AudioManager;
import net.comsoria.engine.audio.AudioSource;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.xhtml.XHTMLLoader;
import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.DocumentHandler;
import net.comsoria.engine.loaders.xhtml.ui.UINode;
import net.comsoria.engine.loaders.xhtml.ui.node.Canvas;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.FadeFog;
import net.comsoria.engine.view.FrameBuffer;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.color.Color3;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.input.KeyListener;
import net.comsoria.game.Player;
import net.comsoria.game.SkyDome;
import net.comsoria.game.terrain.ChunkLoader;
import net.comsoria.game.terrain.World;

import net.comsoria.game.terrain.generation.OctaveGenerator;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.openal.AL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.AL_FREQUENCY;

public class GameHandler extends DocumentHandler {
    private World world;
    private ChunkLoader chunkLoader;
    private Player player;

    private boolean paused = true;
    private float time;

    private Scene scene;

    private UINode compass;

    private final AudioManager soundManager = new AudioManager();
    private AudioSource sourceBack;

    @Override public Document init(Window window) throws Exception {
        Document document = XHTMLLoader.loadDocument(FileLoader.loadResourceAsStringFromPath("$uis/game.xhtml"), window);
        document.updateAllStyleSets(window);

        document.frameBufferRenderer.frameBuffers.add(new FrameBuffer(
                window.getWidth(), window.getHeight(),
                FileLoader.loadResourceAsStringFromPath("$shaders/post_processing/post_processing.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/post_processing/post_processing.f.glsl"))
        );

        scene = ((Canvas) document.getElementByID("main")).scene;
//        scene = new Scene();
        compass = document.getElementByID("compass");
        compass.getMesh().rotation.z = 180;

        player = new Player(new Vector3f(0, 0, 0));
        world = new World();

        float skyDomeR = scene.camera.far - 100;
        scene.add(SkyDome.genSkyDome(
                FileLoader.loadResourceAsStringFromPath("$shaders/skydome/skydome.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/skydome/skydome.f.glsl"),
                skyDomeR, new Texture(Utils.utils.getPath("$textures/sun.png"))
        ));

        List<OctaveGenerator.Octave> octaves = new ArrayList<>();
        octaves.add(new OctaveGenerator.Octave(0.05f, 1.2f));
        octaves.add(new OctaveGenerator.Octave(0.02f, 1.2f));

        chunkLoader = new ChunkLoader(new OctaveGenerator(octaves, (float) Math.random()), 65, 4000, 4, 200, skyDomeR);

        keyInput.addListener(new KeyListener(new int[]{GLFW_KEY_ESCAPE}, (charCode, action) -> {
            if (action != GLFW_RELEASE) return;
            paused = !paused;
            if (paused) {
                window.showCursor();
                sourceBack.pause();
            }
            else {
                sourceBack.play();
                window.hideCursor();
            }
        }, false));

        Color3 background = new Color3(23, 32, 42).getOneToZero();
        window.setClearColor(background);
        scene.fog = new FadeFog(0.0008f, skyDomeR - 1500);

        scene.light.directionalLight = new DirectionalLight(new Color3(250, 215, 160).getOneToZero(), new Vector3f(), 0.55f);

        scene.add(chunkLoader.batchRenderer);

        soundManager.init();
        soundManager.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);

        AudioBuffer buffBack = new AudioBuffer(Utils.utils.p("$audio/bg2.ogg"));
        soundManager.addSoundBuffer(buffBack);
        sourceBack = new AudioSource(true, true);
        sourceBack.setBuffer(buffBack.getBufferId());
        sourceBack.setGain(0.5f);

        soundManager.addSoundSource("BACKING", sourceBack);

        soundManager.setListener(new AudioNode(new Vector3f(0, 0, 0)));

        return document;
    }

    @Override
    public DocumentHandler update(Window window, Document document, float interval) throws Exception {
        if (window.isResized()) {
            document.updateAllStyleSets(window);
        }

        if (!paused) {
            Vector3i movement = new Vector3i();
            if (keyInput.isKeyPressed(GLFW_KEY_W)) {
                movement.z -= 1;
            }

            if (keyInput.isKeyPressed(GLFW_KEY_S)) {
                movement.z += 1;
            }

            if (keyInput.isKeyPressed(GLFW_KEY_A)) {
                movement.x -= 1;
            }

            if (keyInput.isKeyPressed(GLFW_KEY_D)) {
                movement.x += 1;
            }

            if (keyInput.isKeyPressed(GLFW_KEY_SPACE)) {
                movement.y += 1;
            }

            if (keyInput.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                movement.y -= 1;
            }

            float speed = player.getSpeed(keyInput.isKeyPressed(GLFW_KEY_LEFT_CONTROL));
            scene.camera.movePosition((movement.x / 15f) * speed, ((movement.y / 15f) * speed), ((movement.z / 15f) * speed));

            Vector2d pos = mouseInput.getMovementVec();
            scene.camera.rotation.x += (float) pos.y * 0.07f;
            scene.camera.rotation.y += (float) pos.x * 0.07f;

            soundManager.updateListenerPosition(scene.camera);

            compass.getMesh().rotation.z = scene.camera.rotation.y + 180f;

            player.setPosition(scene.camera.position);

            if (keyInput.isKeyPressed(GLFW_KEY_UP)) {
                time += 0.1f;
            }
            if (keyInput.isKeyPressed(GLFW_KEY_DOWN)) {
                time -= 0.1f;
            }
        }

//        time += 0.005;
        scene.updateLight(time);

        try {
            chunkLoader.updateAroundPlayer(player.get2DPosition(), world);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    @Override public void cleanup() {
        sourceBack.stop();
        soundManager.cleanup();
    }
}
