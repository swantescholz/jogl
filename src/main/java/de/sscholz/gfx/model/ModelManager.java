package de.sscholz.gfx.model;

import de.sscholz.util.FileUtil;
import de.sscholz.util.ResourceManager;

import java.io.File;

public class ModelManager extends ResourceManager<Mesh> {

    public ModelManager(File defaultLocation, String defaultFilenameEnding) {
        super(defaultLocation, defaultFilenameEnding);
    }

    public Model get(String name) {
        return new Model(getResource(name));
    }

    public void loadOff(String filename) {
        Mesh mesh = Mesh.createFromOff(FileUtil.readFile(getFile(filename)));
        addMesh(filename, mesh);
    }

    public void loadObj(String filename) {
        Mesh mesh = Mesh.createFromObj(FileUtil.readFile(getFile(filename)));
        addMesh(filename, mesh);
    }

    private void addMesh(String filename, Mesh mesh) {
        mesh.createVbo();
        addResource(filename, mesh);
    }

}
