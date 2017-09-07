package de.sscholz.gfx.model;

import de.sscholz.math.Matrix;

public class Transformable {

    protected Matrix transformation = Matrix.identity();

    public Matrix getTransformation() {
        return transformation;
    }

    public void setTransformation(Matrix transformation) {
        this.transformation.set(transformation);
    }

    public void transform(Matrix transformation) {
        this.transformation.mul(transformation);
    }

}
