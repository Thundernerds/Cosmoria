package net.comsoria.engine.loaders;

import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.mesh.Mesh2D;

import java.util.Arrays;
import java.util.List;

public final class Shape {
    public static Tuple<List<BufferAttribute>, int[]> genRect(float w, float h) {
        w *= 0.5;
        h *= 0.5;

       BufferAttribute positions = new BufferAttribute(new float[]{
               -w, -h, 0,
               -w, h, 0,
               w, h, 0,
               w, -h, 0
       }, 3);

        BufferAttribute texCoords = new BufferAttribute(new float[]{
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        }, 2);

       return new Tuple<>(Arrays.asList(positions, texCoords), new int[] {
               0, 1, 2, 0, 2, 3
       });
    }
}
