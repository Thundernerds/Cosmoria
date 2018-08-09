package net.comsoria.engine.loaders;

import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.graph.BufferAttribute;
import net.comsoria.engine.view.graph.Geometry;
import net.comsoria.engine.view.graph.mesh.Mesh2D;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
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

    private static List<Vector3f> genCircle(int c, float offsetX) {
        float angle = (float) ((2 * Math.PI) / c);

        List<Vector3f> result = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            float sin = (float) Math.sin(i * angle);
            result.add(new Vector3f(offsetX, sin, sin));
        }

        return result;
    }

    public static Tuple<List<BufferAttribute>, int[]> genCylinder(int c, int h) {
        List<Vector3f> points = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        points.addAll(genCircle(c, 0));
        final int pointsPerCircle = points.size();

        for (int i = 1; i < h; i++) {
            points.addAll(genCircle(c, i));

            for (int x = 0; x < pointsPerCircle; x++) {
                faces.add(new Vector3i(
                        ((i - 1) * pointsPerCircle) + x,
                        (i * pointsPerCircle) + x,
                        (i * pointsPerCircle) + x + 1
                ));

                faces.add(new Vector3i(
                        ((i - 1) * pointsPerCircle) + x,
                        ((i - 1) * pointsPerCircle) + x + 1,
                        (i * pointsPerCircle) + x + 1
                ));
            }
        }

        int[] indices = new int[faces.size() * 3];
        for (int i = 0; i < faces.size(); i++) {
            Vector3i face = faces.get(i);
            indices[i] = face.x;
            indices[i + 1] = face.y;
            indices[i + 2] = face.z;
        }

        float[] pointsArr = new float[points.size() * 3];
        for (int i = 0; i < points.size(); i++) {
            Vector3f vertex = points.get(i);
            pointsArr[i] = vertex.x;
            pointsArr[i + 1] = vertex.y;
            pointsArr[i + 2] = vertex.z;
        }

        return new Tuple<>(Arrays.asList(
                new BufferAttribute(pointsArr, 3)
        ), indices);
    }

    public static BufferAttribute gen2dPlan(int pointsLength) {
        float[] newPoints = new float[pointsLength * 2];

        for (int i = 0; i < pointsLength; i += 3) {
            newPoints[i] = (float) i;
            newPoints[i + 1] = 0f;

            newPoints[i + 2] = (float) i;
            newPoints[i + 3] = 1f;

            newPoints[i + 4] = (float) i + 1;
            newPoints[i + 5] = 1f;
        }

        return new BufferAttribute(newPoints, 2);
    }
}
