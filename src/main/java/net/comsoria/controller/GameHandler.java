package net.comsoria.controller;

import net.comsoria.engine.Scene;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.loaders.xhtml.XHTMLLoader;
import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.DocumentHandler;
import net.comsoria.engine.loaders.xhtml.ui.node.Canvas;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.FadeFog;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.color.Color3;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.KeyListener;
import net.comsoria.engine.view.input.MouseInput;
import net.comsoria.game.Player;
import net.comsoria.game.SkyDome;
import net.comsoria.game.terrain.ChunkLoader;
import net.comsoria.game.terrain.World;
import net.comsoria.game.terrain.generation.OctaveGenerator;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameHandler extends DocumentHandler {
    private World world;
    private ChunkLoader chunkLoader;
    private Player player;

    private boolean paused = true;
    private float time;

    private Scene scene;

    @Override public Document init(Window window) throws Exception {
        Document document = XHTMLLoader.loadDocument(FileLoader.loadResourceAsStringFromPath("$uis/game.xhtml"), window);
        document.updateAllStyleSets(window);

        scene = ((Canvas) document.getElementByID("main")).scene;

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
            if (paused) window.showCursor();
            else window.hideCursor();
        }, false));

        Color3 background = new Color3(23, 32, 42).getOneToZero();
        window.setClearColor(background);
        scene.fog = new FadeFog(0.0008f, skyDomeR - 1500);

        scene.light.directionalLight = new DirectionalLight(new Color3(250, 215, 160).getOneToZero(), new Vector3f(), 0.55f);

        scene.add(chunkLoader.batchRenderer);

        return document;
    }

    @Override
    public DocumentHandler update(Window window, Document document, float interval) {
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
//            hud.rotateCompass(scene.camera.rotation.y);

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
}
