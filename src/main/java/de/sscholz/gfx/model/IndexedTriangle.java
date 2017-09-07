package de.sscholz.gfx.model;

import de.sscholz.math.Triangle;
import de.sscholz.math.Vector3;

import java.util.ArrayList;

public class IndexedTriangle {
    public IndexedVertex a = new IndexedVertex();
    public IndexedVertex b = new IndexedVertex();
    public IndexedVertex c = new IndexedVertex();

    public Triangle createTriangle(ArrayList<Vector3> positions, ArrayList<Vector3> normals) {
        Triangle triangle = new Triangle();
        triangle.a.position.set(positions.get(a.positionIndex));
        triangle.b.position.set(positions.get(b.positionIndex));
        triangle.c.position.set(positions.get(c.positionIndex));
        triangle.a.normal.set(normals.get(a.normalIndex));
        triangle.b.normal.set(normals.get(b.normalIndex));
        triangle.c.normal.set(normals.get(c.normalIndex));
        return triangle;
    }

    public Vector3 computeNormal(ArrayList<Vector3> positions) {
        Vector3 va = positions.get(a.positionIndex);
        Vector3 vb = positions.get(b.positionIndex);
        Vector3 vc = positions.get(c.positionIndex);
        return Triangle.normal(va, vb, vc);
    }

    public void setEveryNormalIndex(int normalIndex) {
        a.normalIndex = normalIndex;
        b.normalIndex = normalIndex;
        c.normalIndex = normalIndex;
    }

    public void setPositionIndices(int index1, int index2, int index3) {
        a.positionIndex = index1;
        b.positionIndex = index2;
        c.positionIndex = index3;
    }

    public void setNormalIndices(int normal1, int normal2, int normal3) {
        a.normalIndex = normal1;
        b.normalIndex = normal2;
        c.normalIndex = normal3;
    }
}
