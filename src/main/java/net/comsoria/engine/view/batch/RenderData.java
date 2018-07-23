package net.comsoria.engine.view.batch;

public abstract class RenderData {
    public final static RenderData defaultRenderData = new RenderData() {
        @Override public boolean shouldBindOwnGeometry() {
            return true;
        }
        @Override public boolean shouldBindOwnShaderProgram() {
            return true;
        }
    };

    public abstract boolean shouldBindOwnGeometry();
    public abstract boolean shouldBindOwnShaderProgram();
}
