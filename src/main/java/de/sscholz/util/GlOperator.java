package de.sscholz.util;

import javax.media.opengl.GL;
import java.util.ArrayList;
import java.util.List;


public class GlOperator {

    protected GL gl;
    private static List<GlOperator> operators = new ArrayList<GlOperator>();

    public GlOperator() {
        gl = GlUtil.getGl();
        operators.add(this);
    }

    static void updateAll() {
        GL gl = GlUtil.getGl();
        for (GlOperator operator : operators) {
            operator.gl = gl;
        }
    }


}
