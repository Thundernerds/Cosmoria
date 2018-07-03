package net.comsoria.engine.view.Batch;

public abstract class RenderData {
    public final static RenderData defaultRenderData = new RenderData() {
        @Override public boolean shouldBindGeometry() {
            return true;
        }
        @Override public boolean shouldBindShaderProgram() {
            return true;
        }
    };

    public abstract boolean shouldBindGeometry();
    public abstract boolean shouldBindShaderProgram();
}
