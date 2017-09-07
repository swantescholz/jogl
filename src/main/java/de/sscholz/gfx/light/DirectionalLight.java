package de.sscholz.gfx.light;

import de.sscholz.math.Vector3;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;

public class DirectionalLight extends Light {

    public Vector3 direction = new Vector3();

    public DirectionalLight() {

    }

    @Override
    public void shine() {
        assignColors();
        float[] lightPosition = createFloatQuadrupel(direction.clone().negate(), 0.0);
        GlUtil.getGl().glLightfv(id, GL.GL_POSITION, lightPosition, 0);
    }

}
