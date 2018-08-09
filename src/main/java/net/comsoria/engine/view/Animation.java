package net.comsoria.engine.view;

public class Animation {
    private TickHandler tickHandler;

    public Animation(TickHandler handler) {
        this.tickHandler = handler;
    }

    public void tick(float index) {
        tickHandler.update(index);
    }

    public interface TickHandler {
        void update(float index);
    }
}
