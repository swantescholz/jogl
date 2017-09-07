package de.sscholz.gfx.model;

import de.sscholz.math.*;
import de.sscholz.util.Array2;
import de.sscholz.util.expression.Expression;
import de.sscholz.util.expression.Parser;
import de.sscholz.util.expression.SetVariable;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    public void renderControlNodes(RenderableObject object) {
        Matrix oldTransformation = object.getTransformation().clone();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Vector3 p = getNode(x, y).position;
                object.setTransformation(oldTransformation);
                object.transform(Matrix.translation(p));
                object.render();
            }
        }
        object.setTransformation(oldTransformation);
    }

    public class Node {
        public Vector3 position = new Vector3();
        public Vector3 normal = new Vector3();
    }

    Array2<Node> data;
    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        data = new Array2<Node>(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data.set(x, y, new Node());
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void arrangePlane(Vector3 upperLeft, Vector3 upperRight, Vector3 lowerLeft) {
        Vector3 lowerRight = lowerLeft.clone().add(upperRight).sub(upperLeft);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector3 p = data.get(x, y).position;
                Vector2 fxy = getFactors(x, y);
                p.interpolateBilinear(upperLeft, upperRight, lowerLeft, lowerRight,
                        fxy.x, fxy.y);
            }
        }
    }

    public void randomizeAxis(Vector3 axis, double minAmount, double maxAmount) {
        Vector3 translation = new Vector3();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector3 p = getNode(x, y).position;
                p.add(translation.mul(axis, MathUtil.randomDouble(minAmount, maxAmount)));
            }
        }
    }

    private Vector2 getFactors(int x, int y) {
        return new Vector2(((double) x) / (width - 1), ((double) y) / (height - 1));
    }

    public List<Triangle> createTriangles(boolean smoothNormals) {
        computeSmoothNormals();
        List<Triangle> triangles = new ArrayList<Triangle>();
        for (int y = 0; y < height - 1; y++) {
            addTrianglesOfRow(triangles, y, smoothNormals);
        }
        return triangles;
    }

    private void computeSmoothNormals() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector3 m = getNode(x, y).position;
                Vector3 normal = getNode(x, y).normal;
                Vector3 up = null, dn = null, rt = null, lt = null;
                if (y > 0)
                    up = new Vector3().sub(getNode(x, y - 1).position, m);
                if (x + 1 < width)
                    rt = new Vector3().sub(getNode(x + 1, y).position, m);
                if (y + 1 < height)
                    dn = new Vector3().sub(getNode(x, y + 1).position, m);
                if (x > 0)
                    lt = new Vector3().sub(getNode(x - 1, y).position, m);
                normal.set(Vector3.zero());
                if (up != null) {
                    if (rt != null) normal.add(new Vector3().cross(rt, up));
                    if (lt != null) normal.add(new Vector3().cross(up, lt));
                }
                if (dn != null) {
                    if (rt != null) normal.add(new Vector3().cross(dn, rt));
                    if (lt != null) normal.add(new Vector3().cross(lt, dn));
                }
                normal.normalize();
            }
        }

    }


    public Node getNode(int x, int y) {
        return data.get(x, y);
    }

    private void addTrianglesOfRow(List<Triangle> triangles, int y, boolean smoothNormals) {
        for (int x = 0; x < width - 1; x++) {
            addTriangle(triangles, y, x, y + 1, x, y, x + 1, smoothNormals);
            addTriangle(triangles, y + 1, x, y + 1, x + 1, y, x + 1, smoothNormals);
        }
    }

    private void addTriangle(List<Triangle> triangles, int ya, int xa,
                             int yb, int xb,
                             int yc, int xc, boolean smoothNormals) {
        Triangle triangle = new Triangle();
        triangle.a.position.set(getNode(xa, ya).position);
        triangle.b.position.set(getNode(xb, yb).position);
        triangle.c.position.set(getNode(xc, yc).position);
        if (smoothNormals) {
            triangle.a.normal.set(getNode(xa, ya).normal);
            triangle.b.normal.set(getNode(xb, yb).normal);
            triangle.c.normal.set(getNode(xc, yc).normal);
        } else {
            triangle.setNormalsByPlane();
        }
        triangles.add(triangle);
    }

    public void applyYExpression(String string) {
        try {
            Expression expr = new Parser().parse(string);

            for (Node node : data) {
                Vector3 p = node.position;
                expr.accept(new SetVariable("x", p.x));
                expr.accept(new SetVariable("z", p.z));
                double y = expr.evaluate();
                p.y = y;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
