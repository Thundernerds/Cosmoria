package net.comsoria.controller;

import net.comsoria.Utils;
import net.comsoria.engine.IGameLogic;
import net.comsoria.engine.Scene;
import net.comsoria.engine.view.*;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Light.PointLight;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.KeyListener;
import net.comsoria.engine.view.input.MouseInput;
import net.comsoria.game.Player;
import net.comsoria.game.SkyDome;
import net.comsoria.game.terrain.Chunk;
import net.comsoria.game.terrain.ChunkLoader;
import net.comsoria.game.terrain.World;
import net.comsoria.game.terrain.generation.PerlinGenerator;
import org.joml.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements IGameLogic {
    private Renderer renderer = new Renderer();
    private GameHud hud = new GameHud();
    private Scene scene = new Scene(hud);
    private SkyDome skyDome;

    private World world;
    private ChunkLoader chunkLoader;
    private Player player;

    private boolean paused = true;

    private float time = 0;

    public void init(Window window, KeyInput keyInput) throws Exception {
        hud.init();
        renderer.init(window);

        world = new World();
        skyDome = new SkyDome(Utils.loadResourceAsString("$skydome_vertex"), Utils.loadResourceAsString("$skydome_fragment"), scene.camera.far - 100);
        scene.add(skyDome.getGameObject());

        Vector3f background = new Vector3f(23, 32, 42);
        background = background.div(255);

        window.setClearColor(background);
        scene.fog = new Fog(0.001f, scene.camera.far - 1500);

        chunkLoader = new ChunkLoader(new PerlinGenerator(0.05), 65, 4000, 4, 200); // 0.075
        player = new Player(new Vector3f(0, 0, 0));

        keyInput.addListener(new KeyListener(new int[]{GLFW_KEY_ESCAPE}, (charCode, action) -> {
            if (action != GLFW_RELEASE) return;
            paused = !paused;
            if (paused) window.showCursor();
            else window.hideCursor();
        }, false));

        scene.light.ambientLight = new Vector3f(1f, 1f, 1f);
        scene.light.directionalLight = new DirectionalLight(new Vector3f(250, 215, 160).div(255), new Vector3f(), 0.55f);

        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), new Vector3f(0, 0, 1.5f), 0.5f);
        pointLight.attenuation = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
//        scene.light.pointLightList.add(pointLight);

        hud.updateSize(window);

        scene.add(chunkLoader.batchRenderer);
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

        time += 0.005;

        scene.light.directionalLight.direction = new Vector3f((float) Math.sin(time), (float) Math.cos(time), 0);
        scene.light.ambientLight = new Vector3f((((float) Math.sin(time) + 1) / 2) * 1.3f + 1f);

//        Vector3f day = new Vector3f(33, 150, 243).div(255);
//        Vector3f night = new Vector3f(23, 32, 42).div(255);
//        Vector3f day = new Vector3f();
//        Vector3f night = new Vector3f(1);
//        float dist = ((float) Math.sin(time) + 1) / 2;
//        System.out.println(dist);
//        day.x += (day.x - night.x) * dist;
//        System.out.println(day.x);
//        day.y += (day.y - night.y) * dist;
//        day.z += (day.z - night.z) * dist;
        skyDome.setColor(new Vector3f((float) Math.sin(time)), new Vector3f());

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
