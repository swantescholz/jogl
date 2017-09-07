package de.sscholz.math;

public class Triangle {

    public Vertex a = new Vertex();
    public Vertex b = new Vertex();
    public Vertex c = new Vertex();

    public Triangle() {

    }

    public Triangle(Vector3 a, Vector3 b, Vector3 c) {
        this.a.position.set(a);
        this.b.position.set(b);
        this.c.position.set(c);
        setNormalsByPlane();
    }

    public void setNormals(Vector3 normal) {
        a.normal.set(normal);
        b.normal.set(normal);
        c.normal.set(normal);
    }

    public static Vector3 normal(Vector3 a, Vector3 b, Vector3 c) {
        return new Vector3().cross(b.clone().sub(a), c.clone().sub(a)).normalize();
    }

    public Vector3 getPlaneNormal() {
        return normal(a.position, b.position, c.position);
    }

    public void setNormalsByPlane() {
        setNormals(getPlaneNormal());
    }

    public boolean hasTexCoords() {
        return a.hasTexCoord() && b.hasTexCoord() && c.hasTexCoord();
    }
}
