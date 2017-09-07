package de.sscholz.math;

public class Vertex {

    public Vector3 position = new Vector3();
    public Vector3 normal = new Vector3(0.0, 1.0, 0.0);
    public Vector3 texCoord;

    public Vertex(Vector3 position, Vector3 normal) {
        this.position.set(position);
        this.normal.set(normal);
    }

    public Vertex(Vector3 position, Vector3 normal, Vector3 texCoord) {
        this(position, normal);
        this.texCoord = texCoord.clone();
    }

    public Vertex() {
    }

    public boolean hasTexCoord() {
        return texCoord != null;
    }
}
