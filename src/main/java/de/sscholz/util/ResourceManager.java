package de.sscholz.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceManager<R> {

    protected Map<String, R> resources = new HashMap<String, R>();
    protected final File defaultLocation;
    protected final String defaultFilenameEnding;

    protected ResourceManager(File defaultLocation, String defaultFilenameEnding) {
        this.defaultLocation = defaultLocation;
        this.defaultFilenameEnding = defaultFilenameEnding;
    }

    public File getFile(String name) {
        return new File(defaultLocation, name + defaultFilenameEnding);
    }

    public String getPath(String name) {
        return getFile(name).getPath();
    }

    protected void addResource(String name, R resource) {
        if (hasResource(name)) {
            throw new RuntimeException("Resource '" + name + "' (" + getPath(name) + ") already added!");
        }
        resources.put(getPath(name), resource);
    }

    protected boolean hasResource(String name) {
        String idPath = getPath(name);
        return resources.containsKey(idPath);
    }

    protected R getResource(String name) {
        if (!hasResource(name)) {
            throw new RuntimeException("Resource '" + name + "' (" + getPath(name) + ") not found!");
        }
        return resources.get(getPath(name));
    }

}
