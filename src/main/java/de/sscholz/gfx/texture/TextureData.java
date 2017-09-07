package de.sscholz.gfx.texture;

import de.sscholz.math.Dimension;
import de.sscholz.util.DimensionedObject;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;
import java.util.ArrayList;
import java.util.List;

public class TextureData implements DimensionedObject {

    private final PixelGrid grid;
    private int id;
    private List<PixelGrid> mipmaps = new ArrayList<PixelGrid>();

    public TextureData(PixelGrid grid) {
        this.grid = grid;

        createId();
        createMipmaps();
        loadIntoGl();
    }

    public void dispose() {
        GL gl = GlUtil.getGl();
        gl.glDeleteTextures(1, GlUtil.toArray(id), 0);
    }

    public void bind() {
        GL gl = GlUtil.getGl();
        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
    }

    public String toString() {
        String str = "";
        str += grid.toString();
        return str;
    }

    public static void unbind() {
        GL gl = GlUtil.getGl();
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    private void createId() {
        GL gl = GlUtil.getGl();
        int[] arr = new int[1];
        gl.glGenTextures(1, arr, 0);
        id = arr[0];
    }

    private void createMipmaps() {
        Dimension dim = getDimension();
        dim.width /= 2;
        dim.height /= 2;
        PixelGrid curMip = null;
        PixelGrid lastMip = grid;
        while (dim.width > 0 && dim.height > 0) {
            curMip = new PixelGrid(dim);
            for (int y = 0; y < dim.height; y++) {
                for (int x = 0; x < dim.width; x++) {
                    Pixel pixel = lastMip.get(x * 2, y * 2);
                    curMip.set(x, y, pixel.clone());
                }
            }
            mipmaps.add(curMip);
            dim.width /= 2;
            dim.height /= 2;
            lastMip = curMip;
        }
    }

    private void loadIntoGl() {
        bind();
        GL gl = GlUtil.getGl();
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, grid.getWidth(), grid.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, grid.toBuffer());
        for (int i = 0; i < mipmaps.size(); i++) {
            PixelGrid mipmap = mipmaps.get(i);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, i + 1, GL.GL_RGBA, mipmap.getWidth(), mipmap.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, mipmap.toBuffer());
        }
        unbind();
    }

    @Override
    public Dimension getDimension() {
        return grid.getDimension();
    }

}
