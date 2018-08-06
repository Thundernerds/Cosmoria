package net.comsoria.engine;

import net.comsoria.engine.loaders.xhtml.ui.Document;
import net.comsoria.engine.loaders.xhtml.ui.DocumentHandler;
import net.comsoria.engine.utils.Logger;
import net.comsoria.engine.utils.Timer;
import net.comsoria.engine.view.FrameBuffer;
import net.comsoria.engine.view.FrameBufferRenderer;
import net.comsoria.engine.view.Window;
import net.comsoria.engine.view.input.KeyInput;
import net.comsoria.engine.view.input.MouseInput;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.glViewport;

public class GameEngine implements Runnable {
    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private Window window;
    private Thread loopThread;
    private boolean verbose;

    private DocumentHandler documentHandler;
    private Document document;

    public GameEngine(String title, int width, int height, DocumentHandler initHandler, boolean verbose) throws IOException {
        this.verbose = verbose;

        if (this.verbose) Logger.log("Creating window...");
        window = new Window(title, width, height);
        this.documentHandler = initHandler;
    }

    public void start() {
        if (this.verbose) Logger.log("Starting loop...");
        if (System.getProperty("os.name").contains("Mac")) {
            this.run();
        } else {
            loopThread = new Thread(this, "LOOP_THREAD");
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

        if (this.verbose) Logger.log("Doing first render...");
        update(0);
        render();
        window.show();
        if (this.verbose) Logger.log("Done first render.");

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            long startTime = Timer.getTime();

            update((int) (startTime - last));
            last = startTime;
            render();
            sync(startTime);

            Timer.update();
        }
        if (this.verbose) Logger.log("Stopping.");
    }

    private void render() throws Exception {
        document.render(window);
        window.update();
    }

    private void update(int interval) throws Exception {
        DocumentHandler newDoc = documentHandler.update(window, document, interval);
        documentHandler.mouseInput.input();
        if (newDoc != null) {
            documentHandler = newDoc;
            this.loadDocument();
        }
    }

    private void loadDocument() throws Exception {
        documentHandler.mouseInput.init(window);
        documentHandler.keyInput.init(window);
        this.document = documentHandler.init(window);
        this.window.setClearColor(this.document.background);
    }

    private void sync(long startTime) throws InterruptedException {
        int length = (int) (Timer.getTime() - startTime);
        int target = 1000 / TARGET_FPS;
        int loops = target - length;
        for (int i = 0; i < loops && i >= 0; i++) {
            Thread.sleep(1);
        }
    }

    private void init() throws Exception {
        if (this.verbose) Logger.log("Initialising...");
        window.init();
        this.loadDocument();
    }

    private void cleanup() {
        document.cleanup();
        documentHandler.cleanup();
    }
}
