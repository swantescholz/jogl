package de.sscholz.gfx.texture;

import de.sscholz.math.Matrix;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;

public class Texture {

    public static void enable() {
        GlUtil.getGl().glEnable(GL.GL_TEXTURE_2D);
    }

    public static void disable() {
        GlUtil.getGl().glDisable(GL.GL_TEXTURE_2D);
    }

    TextureData data;
    Matrix transformation = Matrix.identity();


    public Texture(TextureData data) {
        this.data = data;
    }

    public void dispose() {
        data.dispose();
    }

    public void bind() {
        data.bind();
    }

    public static void unbind() {
        TextureData.unbind();
    }

}
