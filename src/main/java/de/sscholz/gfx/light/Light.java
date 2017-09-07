package de.sscholz.gfx.light;

import de.sscholz.math.Color;
import de.sscholz.math.Vector3;
import de.sscholz.util.GlUtil;
import de.sscholz.util.IndexPool;

import javax.media.opengl.GL;

public abstract class Light {

    public static void enable() {
        GlUtil.getGl().glEnable(GL.GL_LIGHTING);
    }

    public static void disable() {
        GlUtil.getGl().glDisable(GL.GL_LIGHTING);
    }

    static final IndexPool indexPool = new IndexPool();

    static {
        indexPool.add(GL.GL_LIGHT0);
        indexPool.add(GL.GL_LIGHT1);
        indexPool.add(GL.GL_LIGHT2);
        indexPool.add(GL.GL_LIGHT3);
    }

    public Color ambient = Color.white();
    public Color diffuse = Color.white();
    public Color specular = Color.white();
    protected int id = -1;
    private boolean active = false;

    protected Light() {
        setActive(true);
    }

    protected void assignColors() {
        GL gl = GlUtil.getGl();
        gl.glLightfv(id, GL.GL_AMBIENT, ambient.floatData(), 0);
        gl.glLightfv(id, GL.GL_DIFFUSE, diffuse.floatData(), 0);
        gl.glLightfv(id, GL.GL_SPECULAR, specular.floatData(), 0);
    }

    public abstract void shine();


    public static void setGlobalAmbient(Color globalAmbient) {
        GL gl = GlUtil.getGl();
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, globalAmbient.floatData(), 0);
    }

    public void setActive(boolean active) {
        GL gl = GlUtil.getGl();
        if (this.active == active)
            return;

        if (active) {
            id = indexPool.aquire();
            gl.glEnable(id);
        } else {
            gl.glDisable(id);
            indexPool.release(id);
        }
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    protected float[] createFloatQuadrupel(Vector3 tripel, double fourth) {
        return new float[]{(float) tripel.x, (float) tripel.y, (float) tripel.z, (float) fourth};
    }
}
