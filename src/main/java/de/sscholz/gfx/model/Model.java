package de.sscholz.gfx.model;


public class Model extends RenderableObject {

    private Mesh mesh;

    public Model(Mesh mesh) {
        this.mesh = mesh;
    }

    protected void draw() {
        mesh.render();
    }

    public Model clone() {
        Model newModel = new Model(mesh);
        newModel.setTransformation(transformation);
        return newModel;
    }

}
