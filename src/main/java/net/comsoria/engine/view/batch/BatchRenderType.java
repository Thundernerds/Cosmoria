package net.comsoria.engine.view.batch;

import net.comsoria.engine.view.GLSL.ShaderProgram;
import net.comsoria.engine.view.graph.Geometry;

public class BatchRenderType {
    public Geometry geometry = null;
    public ShaderProgram shaderProgram = null;

    public BatchRenderType() {

    }

    public BatchRenderType(Geometry geometry) {
        this.geometry = geometry;
    }

    public BatchRenderType(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public BatchRenderType(Geometry geometry, ShaderProgram shaderProgram) {
        this.geometry = geometry;
        this.shaderProgram = shaderProgram;
    }
}
