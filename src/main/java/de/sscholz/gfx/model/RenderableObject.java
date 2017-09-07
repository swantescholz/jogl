package de.sscholz.gfx.model;

import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;

public abstract class RenderableObject extends Transformable implements Renderable {

    public void render() {
        GL gl = GlUtil.getGl();
        gl.glPushMatrix();
        transformation.glMult();
        draw();
        gl.glPopMatrix();
    }

    protected abstract void draw();

}
