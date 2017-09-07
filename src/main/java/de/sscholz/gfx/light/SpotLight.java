package de.sscholz.gfx.light;

import de.sscholz.math.Vector3;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;

public class SpotLight extends PositionalLight {

    public Vector3 direction = Vector3.nz().clone();
    public double exponent = 0.0;
    public double cutoff = 45.0;

    public SpotLight() {

    }

    @Override
    public void shine() {
        GL gl = GlUtil.getGl();
        super.shine();
        gl.glLightf(id, GL.GL_SPOT_EXPONENT, (float) exponent);
        gl.glLightf(id, GL.GL_SPOT_CUTOFF, (float) cutoff);
        gl.glLightfv(id, GL.GL_SPOT_DIRECTION, direction.floatData(), 0);
    }

}
