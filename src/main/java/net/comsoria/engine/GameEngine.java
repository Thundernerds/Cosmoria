package net.comsoria.engine;

import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.MouseInput;

public class GameEngine implements Runnable {
    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private Window window;
    private IGameLogic gameLogic;
    private Thread loopThread;
    private Timer timer = new Timer();
    private MouseInput mouseInput = new MouseInput();
    private KeyInput keyInput = new KeyInput();

    public GameEngine(String title, int width, int height, IGameLogic gameLogic) {
        window = new Window(title, width, height);
        this.gameLogic = gameLogic;
        loopThread = new Thread(this, "LOOP_THREAD");
    }

    public void start() {
        if (System.getProperty("os.name").contains("Mac")) {
            this.run();
        } else {
            loopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void gameLoop() throws Exception {
        long last = 0;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            long startTime = Timer.getTime();

            update((int) (startTime - last));
            last = startTime;
            render();
            sync(startTime);
        }
    }

    private void render() throws Exception {
        gameLogic.render(window);
        window.update();
    }

    private void update(int interval) {
        gameLogic.update(window, interval, mouseInput, keyInput);
        mouseInput.input();
    }

    private void sync(long startTime) throws InterruptedException {
        int length = (int) (Timer.getTime() - startTime);
        int target = (1000 / TARGET_FPS);
        int loops = target - length;
        for (int i = 0; i < loops && i >= 0; i++) {
            Thread.sleep(1);
        }
    }

    private void init() throws Exception {
        window.init();
        gameLogic.init(window, keyInput);
        keyInput.init(window);
        mouseInput.init(window);
    }

    private void cleanup() {
        gameLogic.cleanup();
    }
}
