package de.sscholz.gfx;

import de.sscholz.math.Triangle;
import de.sscholz.math.Vertex;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Vbo {


    private int totalNumVerts;
    private int id = 0;

    private int stride;
    private int vertexPointer;
    private int normalPointer;
    private int texCoordPointer = 0;
    private boolean hasTexCoords = true;

    public void create(List<Triangle> triangles) {
        GL gl = GlUtil.getGl();
        hasTexCoords = true;

        List<Vertex> vertices = new ArrayList<Vertex>();
        for (Triangle triangle : triangles) {
            if (!triangle.hasTexCoords()) {
                hasTexCoords = false;
            }
            vertices.add(triangle.a);
            vertices.add(triangle.b);
            vertices.add(triangle.c);
        }

        totalNumVerts = vertices.size();

        IntBuffer buf = IntBuffer.allocate(1);
        gl.glGenBuffers(1, buf);
        id = buf.get();

        int doublesPerVert = 6;
        if (hasTexCoords)
            doublesPerVert += 2;

        DoubleBuffer data = DoubleBuffer.allocate(totalNumVerts * doublesPerVert);
        for (Vertex vertex : vertices) {
            if (hasTexCoords)
                data.put(vertex.texCoord.data());
            data.put(vertex.normal.data());
            data.put(vertex.position.data());
        }
        data.rewind();

        int bytesPerDouble = Double.SIZE / Byte.SIZE;

        int numBytes = data.capacity() * bytesPerDouble;
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, data, GL.GL_STATIC_DRAW);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

        if (!hasTexCoords) {
            texCoordPointer = -2;
        }
        normalPointer = 2 + texCoordPointer;
        vertexPointer = 3 + normalPointer;
        stride = 3 + vertexPointer;
        texCoordPointer *= bytesPerDouble;
        normalPointer *= bytesPerDouble;
        vertexPointer *= bytesPerDouble;
        stride *= bytesPerDouble;
    }

    public void render() {
        GL gl = GlUtil.getGl();
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
        gl.glEnableClientState(GL.GL_VERTEX_ARRAY);

        gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
        if (hasTexCoords) {
            gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
            gl.glTexCoordPointer(2, GL.GL_DOUBLE, stride, texCoordPointer);
        }
        gl.glNormalPointer(GL.GL_DOUBLE, stride, normalPointer);
        gl.glVertexPointer(3, GL.GL_DOUBLE, stride, vertexPointer);

        gl.glDrawArrays(GL.GL_TRIANGLES, 0, totalNumVerts);

        gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
        if (hasTexCoords) {
            gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
        }
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    public void dispose() {
        if (id != 0) {
            GlUtil.getGl().glDeleteBuffers(1, new int[]{id}, 0);
            id = 0;
        }
    }

    public String toString() {
        return "Vertex Buffer Object (VBO): " + id;
    }

}
