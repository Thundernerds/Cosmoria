package net.comsoria.controller;

import net.comsoria.Utils;
import net.comsoria.engine.IGameLogic;
import net.comsoria.engine.Scene;
import net.comsoria.engine.Tuple;
import net.comsoria.engine.loaders.OBJLoader;
import net.comsoria.engine.view.*;
import net.comsoria.engine.view.Light.DirectionalLight;
import net.comsoria.engine.view.Light.PointLight;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.Material;
import net.comsoria.engine.view.graph.Mesh;
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
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements IGameLogic {
    private Renderer renderer = new Renderer();
    private Camera camera = new Camera();
    private Hud hud = new Hud();
    private Scene scene = new Scene(hud);
    private SkyDome skyDome;

    private World world = new World();
    private ChunkLoader chunkLoader;
    private Player player;

    private boolean paused = true;

    private float time = 0;

    public void init(Window window, KeyInput keyInput) throws Exception {
        hud.init();
        renderer.init(window);
        skyDome = new SkyDome(Utils.loadResourceAsString("$skydome_vertex"), Utils.loadResourceAsString("$skydome_fragment"), 1000);
        scene.children.add(skyDome.getGameObject());

        Vector3f background = new Vector3f(23, 32, 42);
        background = background.div(255);

        window.setClearColor(background);
        scene.fog = new Fog(0.001f, camera.far - 1000, background);

        chunkLoader = new ChunkLoader(new PerlinGenerator(0.055), 65, 5000, 2, 200); // 0.075
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
            camera.movePosition((movement.x / 15f) * speed, ((movement.y / 15f) * speed), ((movement.z / 15f) * speed));

            Vector2d pos = mouse.getMovementVec();
            camera.rotation.x += (float) pos.y * 0.07f;
            camera.rotation.y += (float) pos.x * 0.07f;
            hud.rotateCompass(camera.rotation.y);

            player.setPosition(camera.position);

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

        try {
            chunkLoader.updateAroundPlayer(player.get2DPosition(), world);
            for (Chunk chunk : world.getBuffer()) {
                scene.children.add(chunk.getGameObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void render(Window window) throws Exception {
        renderer.render(window, camera, scene);
    }

    public void cleanup() {
        scene.cleanup();
    }
}
