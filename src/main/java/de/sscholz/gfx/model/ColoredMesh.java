package de.sscholz.gfx.model;

import de.sscholz.math.Color;
import de.sscholz.math.Vector3;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;
import java.util.ArrayList;

public class ColoredMesh extends RenderableObject {

    Mesh mesh;
    ArrayList<Color> meshColors = new ArrayList<Color>();


    public ColoredMesh(Mesh baseMesh) {
        mesh = baseMesh;
        for (int i = 0; i < mesh.positions.size(); i++) {
            Color color = Color.random();
            meshColors.add(color);
        }
    }

    @Override
    protected void draw() {
        GL gl = GlUtil.getGl();
        GlUtil.pushEnabled();
        //gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glBegin(GL.GL_TRIANGLES);

        for (IndexedTriangle tri : mesh.indexedTriangles) {
            drawIndexedTriangle(tri);
        }
        gl.glEnd();

        GlUtil.popEnabled();
    }

    private void drawIndexedTriangle(IndexedTriangle tri) {
        tri.computeNormal(mesh.positions).glNormal();
        drawIndexedVertex(tri.a);
        drawIndexedVertex(tri.b);
        drawIndexedVertex(tri.c);
    }

    private void drawIndexedVertex(IndexedVertex v) {
        //meshColors.get(v.positionIndex).gl();
        Vector3 p = mesh.positions.get(v.positionIndex);

        p.toXy().mul(.2).glTex();
        p.gl();
    }
}
