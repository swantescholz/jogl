package de.sscholz.gfx.model;

import de.sscholz.math.MathUtil;
import de.sscholz.math.Matrix;
import de.sscholz.math.Vector3;

public class Clock extends RenderableObject {

    private static final double clockSize = 10.0;

    Model bgcylinder, mhand, hhand;
    ColoredMesh initialA, initialB;
    double hangle, mangle;

    public Clock(Mesh a, Mesh b, Model cylinder) {
        initialA = new ColoredMesh(a);
        initialB = new ColoredMesh(b);
        bgcylinder = cylinder.clone();
        mhand = cylinder.clone();
        hhand = cylinder.clone();
    }

    private void initTransformations() {
        bgcylinder.setTransformation(Matrix.scaling(new Vector3(1, .1, 1)));
        bgcylinder.transform(Matrix.rotation(Vector3.x(), MathUtil.toRadian(-90)));
        bgcylinder.transform(Matrix.scaling(new Vector3(10, 10, 1)));
        bgcylinder.transform(Matrix.translation(Vector3.nz().mul(2)));

        Matrix mscaling = Matrix.scaling(new Vector3(.1, .1, .1).mul(2));
        double handoffset = clockSize * .06;
        Matrix hhandscaling = Matrix.scaling(new Vector3(.3, clockSize / 2 - handoffset, .5));
        Matrix mhandscaling = Matrix.scaling(new Vector3(.3, clockSize / 2 * .6 - handoffset, .5));
        Vector3 zeroPoint = Vector3.y().mul(clockSize);
        initialA.setTransformation(mscaling);
        initialB.setTransformation(mscaling);
        initialA.transform(Matrix.translation(zeroPoint.clone().mul(.6)));
        initialB.transform(Matrix.translation(zeroPoint));
        mhand.setTransformation(Matrix.translation(new Vector3(0, 1, 0)));
        hhand.setTransformation(Matrix.translation(new Vector3(0, 1, 0)));
        mhand.transform(hhandscaling);
        hhand.transform(mhandscaling);
    }

    @Override
    protected void draw() {
        initTransformations();
        Matrix hrot = Matrix.rotation(Vector3.z(), hangle);
        Matrix mrot = Matrix.rotation(Vector3.z(), mangle);

        bgcylinder.render();
        hhand.transform(hrot);
        mhand.transform(mrot);
        initialA.transform(hrot);
        initialB.transform(mrot);
        hhand.render();
        mhand.render();
        initialA.render();
        initialB.render();
    }

    public void setTime(double minutes) {
        double hours = minutes / 60.0;
        minutes -= (int) (hours) * 60.0;
        hangle = -MathUtil.toRadian(360 * hours / 12.0);
        mangle = -MathUtil.toRadian(360 * minutes / 60.0);
    }

}
