package de.sscholz.gfx.texture;

import de.sscholz.util.ResourceManager;

import java.io.File;
import java.io.IOException;

public class TextureManager extends ResourceManager<TextureData> {

    public TextureManager(File defaultLocation, String defaultFilenameEnding) {
        super(defaultLocation, defaultFilenameEnding);
    }

    public Texture get(String name) {
        return new Texture(getResource(name));
    }

    public Texture load(String filename) {
        File file = getFile(filename);
        try {
            PixelGrid grid = PixelGrid.createFromFile(file);
            addResource(filename, new TextureData(grid));
        } catch (IOException e) {
            System.err.println("File: " + file);
            e.printStackTrace();
        }
        return get(filename);
    }

}
