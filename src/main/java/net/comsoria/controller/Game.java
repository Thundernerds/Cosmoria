package net.comsoria.controller;

import net.comsoria.engine.utils.Logger;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.loaders.FileLoader;
import net.comsoria.engine.view.Color;
import net.comsoria.engine.IGameLogic;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.*;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Light.PointLight;
import net.comsoria.engine.view.graph.Texture;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.KeyListener;
import net.comsoria.engine.view.input.MouseInput;
import net.comsoria.game.DayNightHandler;
import net.comsoria.game.Player;
import net.comsoria.game.SkyDome;
import net.comsoria.game.terrain.ChunkLoader;
import net.comsoria.game.terrain.World;
import net.comsoria.game.terrain.generation.OctaveGenerator;
import net.comsoria.game.terrain.generation.Perlin2Generator;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements IGameLogic {
    private Renderer renderer = new Renderer();
    private GameHud hud = new GameHud();
    private Scene scene = new Scene(hud);
    private DayNightHandler dayNightHandler;

    private World world;
    private ChunkLoader chunkLoader;
    private Player player;

    private boolean paused = true;

    private float time = 0;

    public void init(Window window, KeyInput keyInput) throws Exception {
        StartupFileHandler.load();

        Logger.log("Loaded files, creating game...");

        hud.init();

        renderer.frameBuffers.add(new FrameBuffer(window.getWidth(), window.getHeight(),
                FileLoader.loadResourceAsStringFromPath("$shaders/post_processing/post_processing.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/post_processing/post_processing.f.glsl")));

        world = new World();

        SkyDome skyDome = new SkyDome(FileLoader.loadResourceAsStringFromPath("$shaders/skydome/skydome.v.glsl"),
                FileLoader.loadResourceAsStringFromPath("$shaders/skydome/skydome.f.glsl"),
                scene.camera.far - 100, new Texture(Utils.utils.getPath("$textures/sun.png")));
        scene.add(skyDome.getGameObject());

        dayNightHandler = new DayNightHandler(skyDome, -0.5f, scene.light, 0.15f, 0.25f);

        Color background = new Color(23, 32, 42).getOneToZero();
        window.setClearColor(background);
        scene.fog = new Fog(0.005f, scene.camera.far - 1500);

        List<OctaveGenerator.Octave> octaves = new ArrayList<>();
        octaves.add(new OctaveGenerator.Octave(0.05f, 1.2f));
        octaves.add(new OctaveGenerator.Octave(0.02f, 1.2f));

        chunkLoader = new ChunkLoader(new OctaveGenerator(octaves, (float) Math.random()), 65, 4000, 4, 200);

        player = new Player(new Vector3f(0, 0, 0));

        keyInput.addListener(new KeyListener(new int[]{GLFW_KEY_ESCAPE}, (charCode, action) -> {
            if (action != GLFW_RELEASE) return;
            paused = !paused;
            if (paused) window.showCursor();
            else window.hideCursor();
        }, false));

        scene.light.directionalLight = new DirectionalLight(new Color(250, 215, 160).getOneToZero(), new Vector3f(), 0.55f);

        PointLight pointLight = new PointLight(new Color(1, 1, 1), new Vector3f(0, 0, 1.5f), 0.5f);
        pointLight.attenuation = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
//        scene.light.pointLightList.add(pointLight);

        hud.updateSize(window);

        scene.add(chunkLoader.batchRenderer);

        Logger.log("Loaded game...");
    }

    public void update(Window window, float interval, MouseInput mouse, KeyInput keys) {
        if (!paused) {
            Vector3i movement = new Vector3i();
            if (keys.isKeyPressed(GLFW_KEY_W)) {
                movement.z -= 1;
            }

            if (keys.isKeyPressed(GLFW_KEY_S)) {
                movement.z += 1;
            }

            if (keys.isKeyPressed(GLFW_KEY_A)) {
                movement.x -= 1;
            }

            if (keys.isKeyPressed(GLFW_KEY_D)) {
                movement.x += 1;
            }

            if (keys.isKeyPressed(GLFW_KEY_SPACE)) {
                movement.y += 1;
            }

            if (keys.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                movement.y -= 1;
            }

            float speed = player.getSpeed(keys.isKeyPressed(GLFW_KEY_LEFT_CONTROL));
            scene.camera.movePosition((movement.x / 15f) * speed, ((movement.y / 15f) * speed), ((movement.z / 15f) * speed));

            Vector2d pos = mouse.getMovementVec();
            scene.camera.rotation.x += (float) pos.y * 0.07f;
            scene.camera.rotation.y += (float) pos.x * 0.07f;
            hud.rotateCompass(scene.camera.rotation.y);

            player.setPosition(scene.camera.position);

            if (keys.isKeyPressed(GLFW_KEY_UP)) {
                time += 0.1;
            }
            if (keys.isKeyPressed(GLFW_KEY_DOWN)) {
                time -= 0.1;
            }
        }

//        time += 0.005;
        dayNightHandler.update(time);

        try {
            chunkLoader.updateAroundPlayer(player.get2DPosition(), world);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void render(Window window) throws Exception {
        renderer.render(window, scene);
    }

    public void cleanup() {
        scene.cleanup();
    }
}
