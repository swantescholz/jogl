package de.sscholz.math;


public class Camera {

    public Vector3 position;
    public Vector3 direction;
    public Vector3 upVector;
    private final Vector3 originalUpVector;

    public Camera() {
        this(new Vector3(0, 0, 10), new Vector3(0, 0, -1), new Vector3(0, 1, 0));
    }

    public Camera(Vector3 pos, Vector3 dir, Vector3 up) {
        this.position = pos.clone();
        this.direction = dir.clone();
        this.upVector = up.clone();
        this.originalUpVector = this.upVector.clone();
    }

    public Matrix getMatrix() {
        return Matrix.camera(position, direction, upVector);
    }

    public void pitch(double radian) { //x
        radian *= -1; //for CCW
        Vector3 cross = new Vector3();
        cross.cross(upVector, direction);
        Matrix.rotation(cross, radian).transformNormal(direction);
        Matrix.rotation(cross, radian).transformNormal(upVector);
    }

    public void yaw(double radian) { //y
        radian *= -1; //for CCW
        Matrix.rotation(upVector, radian).transformNormal(direction);
    }

    public void yawAroundOriginalUpVector(double radian) {
        radian *= -1; //for CCW
        Matrix.rotation(originalUpVector, radian).transformNormal(direction);
        Matrix.rotation(originalUpVector, radian).transformNormal(upVector);
    }

    public void roll(double radian) { //z
        radian *= -1; //for CCW
        Matrix.rotation(direction, radian).transformNormal(upVector);
    }

    public void lookAt(Vector3 to) {
        direction.sub(to, position);
    }

    public Vector3 getXAxis() {
        Vector3 xaxis = new Vector3();
        xaxis.cross(upVector, direction);
        xaxis.normalize();
        return xaxis;
    }

    public Vector3 getYAxis() {
        Vector3 yaxis = new Vector3();
        yaxis.cross(getZAxis(), getXAxis());
        yaxis.normalize();
        return yaxis;
    }

    public Vector3 getZAxis() {
        Vector3 dir = direction.clone();
        dir.normalize();
        return dir;
    }

    public void moveLocal(Vector3 v) {
        Vector3 xmove = getXAxis();
        Vector3 ymove = getYAxis();
        Vector3 zmove = getZAxis();
        xmove.mul(v.x);
        ymove.mul(v.y);
        zmove.mul(v.z);
        position.add(xmove);
        position.add(ymove);
        position.add(zmove);
    }

    public void advance(double zmove) {
        moveLocal(new Vector3(0, 0, zmove));
    }

    public void apply() {
        getMatrix().glLoad();
    }
}
