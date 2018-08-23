package net.comsoria.engine.loaders;

import net.comsoria.engine.utils.random.Random;
import net.comsoria.engine.utils.Tuple;
import net.comsoria.engine.utils.Utils;
import net.comsoria.engine.view.graph.BufferAttribute;
import org.joml.*;

import java.lang.Math;
import java.util.*;

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

    private static List<Vector3f> genCircle(int c, float r, Vector3f offset) {
        float angle = (float) ((2 * Math.PI) / c);

        List<Vector3f> result = new ArrayList<>();
        for (int i = 0; i < c + 1; i++) {
            result.add(new Vector3f(offset).add(
                    (float) Math.sin(i * angle) * r,
                    0,
                    (float) Math.cos(i * angle) * r
            ));
        }

        return result;
    }

    public static Tuple<List<BufferAttribute>, int[]> genCylinder(int c, int h, float r) {
        return genCylinder(c, h, new CylinderGenerator() {
            @Override
            public float radius(int h) {
                return r;
            }

            @Override
            public Vector3f offset(int h) {
                return new Vector3f(0, h, 0);
            }
        });
    }

    public static Tuple<List<BufferAttribute>, int[]> genCylinder(int c, int h, CylinderGenerator generator) {
        List<Vector3f> points = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();
        List<Vector2f> planePoints = new ArrayList<>();

        points.addAll(genCircle(c, generator.radius(0), generator.offset(0)));
        final int pointsPerCircle = points.size();

        for (int i = 0; i < pointsPerCircle; i++) planePoints.add(new Vector2f(i, 0));

        for (int i = 1; i < h; i++) {
            points.addAll(genCircle(c, generator.radius(i), generator.offset(i)));

            for (int x = 0; x < pointsPerCircle; x++) planePoints.add(new Vector2f(x, i));

            for (int x = 0; x < c; x++) {
                faces.add(new Vector3i(
                        ((i - 1) * pointsPerCircle) + x,
                        (i * pointsPerCircle) + x,
                        (i * pointsPerCircle) + x + 1
                ));

                faces.add(new Vector3i(
                        ((i - 1) * pointsPerCircle) + x,
                        (i * pointsPerCircle) + x + 1,
                        ((i - 1) * pointsPerCircle) + x + 1
                ));
            }
        }

        int[] indices = new int[faces.size() * 3];
        for (int i = 0; i < faces.size(); i++) {
            Vector3i face = faces.get(i);
            indices[(i * 3)] = face.x;
            indices[(i * 3) + 1] = face.y;
            indices[(i * 3) + 2] = face.z;
        }

        return new Tuple<>(new ArrayList<>(Arrays.asList(
                BufferAttribute.create3f(points), BufferAttribute.create2f(planePoints)
        )), indices);
    }

    public static Tuple<List<BufferAttribute>, int[]> genSphere(int p, float radius) {
        return genCylinder(p, p + 1, new CylinderGenerator() {
            @Override
            public float radius(int h) {
                return (float) Math.sin((Math.PI / p) * (float) h) * radius;
            }

            @Override
            public Vector3f offset(int h) {
                return new Vector3f(0, ((float) Math.cos((Math.PI / p) * (float) h) * radius), 0);
            }
        });
    }

    public static BufferAttribute generateDisplacement(BufferAttribute points, float scale, int d) {
        return generateDisplacement(points, (x, y, z) -> scale, d);
    }

    public static BufferAttribute generateSphereDisplacement(BufferAttribute points, float scale, int d, float radius) {
        return generateDisplacement(points, (x, y, z) -> (Utils.distance(x, z) / radius) * scale, d);
    }

    public static BufferAttribute generateDisplacement(BufferAttribute points, DisplacementGenerator generator, int d) {
        int parts = points.parts();

        float[] data = new float[parts * d];

        for (int i = 0; i < parts; i++) {
            float x = Utils.round(points.get(i * 3), 2);
            float y = Utils.round(points.get((i * 3) + 1), 2);
            float z = Utils.round(points.get((i * 3) + 2), 2);

            for (int a = 0; a < d; a++) {
                data[(i * d) + a] = Random.random.noise(x, y, z, 23.65f * a) * generator.scale(x, y, z);
            }
        }

        return new BufferAttribute(data, d);
    }

    public interface DisplacementGenerator {
        float scale(float x, float y, float z);
    }

    public interface CylinderGenerator {
        float radius(int h);
        Vector3f offset(int h);
    }
}
