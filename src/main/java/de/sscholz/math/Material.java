package de.sscholz.math;

import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;

public class Material extends VectorSpace<Material> {

    public static Material black() {
        return new Material(Color.black());
    }

    public static Material white() {
        return new Material(Color.white());
    }

    public static Material red() {
        return new Material(Color.red());
    }

    public static Material green() {
        return new Material(Color.green());
    }

    public static Material blue() {
        return new Material(Color.blue());
    }

    public static Material yellow() {
        return new Material(Color.yellow());
    }

    public static Material purple() {
        return new Material(Color.purple());
    }

    public static Material cyan() {
        return new Material(Color.cyan());
    }

    public static Material orange() {
        return new Material(Color.orange());
    }

    public static Material pink() {
        return new Material(Color.pink());
    }

    public static Material sky() {
        return new Material(Color.sky());
    }

    public static Material mellow() {
        return new Material(Color.mellow());
    }

    public static Material forest() {
        return new Material(Color.forest());
    }

    public static Material silver() {
        return new Material(Color.silver(), 96.0);
    }

    public static Material gold() {
        return new Material(Color.gold(), 96.0);
    }

    public static Material brass() {
        return new Material(new Color(0.33, 0.22, 0.03), new Color(0.78, 0.57, 0.11), new Color(0.99, .91, 0.81), 5.0);
    }

    public Color ambient = Color.white();
    public Color diffuse = Color.white();
    public Color specular = Color.white();
    public double shininess = 64.0;

    public Material() {
    }

    public Material(Color color) {
        this(color, color, color);
    }

    public Material(Color color, double shininess) {
        this(color, color, color, shininess);
    }

    public Material(Color ambient, Color diffuse, Color specular) {
        this(ambient, diffuse, specular, 64.0);
    }

    public Material(Color ambient, Color diffuse, Color specular, double shininess) {
        set(ambient, diffuse, specular, shininess);
    }

    public Material(Material that) {
        set(that);
    }

    public Material set(Material b) {
        return set(b.ambient, b.diffuse, b.specular, b.shininess);
    }

    @Override
    public Material clone() {
        return new Material(this);
    }

    @Override
    public Material negate() {
        ambient.negate();
        diffuse.negate();
        specular.negate();
        shininess = -shininess;
        return this;
    }

    public Material set(Color ambient, Color diffuse, Color specular, double shininess) {
        this.ambient.set(ambient);
        this.diffuse.set(diffuse);
        this.specular.set(specular);
        this.shininess = shininess;
        return this;
    }

    public Material add(Material b) {
        ambient.add(b.ambient);
        diffuse.add(b.diffuse);
        specular.add(b.specular);
        shininess += b.shininess;
        return this;
    }

    public Material add(Material a, Material b) {
        ambient.add(a.ambient, b.ambient);
        diffuse.add(a.diffuse, b.diffuse);
        specular.add(a.specular, b.specular);
        shininess = a.shininess + b.shininess;
        return this;
    }

    public Material sub(Material b) {
        ambient.sub(b.ambient);
        diffuse.sub(b.diffuse);
        specular.sub(b.specular);
        shininess -= b.shininess;
        return this;
    }

    public Material sub(Material a, Material b) {
        ambient.sub(a.ambient, b.ambient);
        diffuse.sub(a.diffuse, b.diffuse);
        specular.sub(a.specular, b.specular);
        shininess = a.shininess - b.shininess;
        return this;
    }

    public Material mul(double s) {
        ambient.mul(s);
        diffuse.mul(s);
        specular.mul(s);
        shininess *= s;
        return this;
    }

    @Override
    public boolean almostEqual(Material b) {
        return ambient.almostEqual(b.ambient) && diffuse.almostEqual(b.diffuse) &&
                specular.almostEqual(b.specular) && MathUtil.almostEqual(shininess, b.shininess);
    }

    public void gl() {
        GL gl = GlUtil.getGl();
        final int face = GL.GL_FRONT;
        gl.glMaterialfv(face, GL.GL_AMBIENT, ambient.floatData(), 0);
        gl.glMaterialfv(face, GL.GL_DIFFUSE, diffuse.floatData(), 0);
        gl.glMaterialfv(face, GL.GL_SPECULAR, specular.floatData(), 0);
        gl.glMaterialf(face, GL.GL_SHININESS, (float) shininess);
    }

    public String toString() {
        return "[" + ambient + ", " + diffuse + ", " + specular + ", " + shininess + "]";
    }

}
