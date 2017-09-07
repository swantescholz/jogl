package de.sscholz.gfx.light;

import de.sscholz.math.Vector3;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;

public class PositionalLight extends Light {

    public Vector3 position = new Vector3();
    public double constantAttenuation = 1.0;
    public double linearAttenuation = 0.0;
    public double quadraticAttenuation = 0.0;

    public PositionalLight() {

    }

    @Override
    public void shine() {
        GL gl = GlUtil.getGl();
        assignColors();

        float[] lightPosition = createFloatQuadrupel(position, 1.0);
        gl.glLightfv(id, GL.GL_POSITION, lightPosition, 0);

        gl.glLightf(id, GL.GL_CONSTANT_ATTENUATION, (float) constantAttenuation);
        gl.glLightf(id, GL.GL_LINEAR_ATTENUATION, (float) linearAttenuation);
        gl.glLightf(id, GL.GL_QUADRATIC_ATTENUATION, (float) quadraticAttenuation);
    }

}
