package de.sscholz.gfx.texture;

import de.sscholz.math.Dimension;
import de.sscholz.util.Array2;
import de.sscholz.util.DimensionedObject;
import de.sscholz.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class PixelGrid extends Array2<Pixel> implements DimensionedObject {


    public PixelGrid(int w, int h) {
        super(w, h);
    }


    public PixelGrid(Dimension dim) {
        this(dim.width, dim.height);
    }


    public static PixelGrid createFromFile(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return createFromBufferedImage(image);
    }

    private static PixelGrid createFromBufferedImage(BufferedImage image) {
        Raster raster = image.getData();
        PixelGrid grid = new PixelGrid(raster.getWidth(), raster.getHeight());
        int[] colors = new int[4];
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                colors[3] = 255; //opaque is default, when no alpha given
                raster.getPixel(x, y, colors);
                Pixel pixel = new Pixel((byte) colors[0], (byte) colors[1], (byte) colors[2], (byte) colors[3]);
                grid.set(x, grid.getHeight() - y - 1, pixel);
            }
        }
        return grid;
    }


    public Buffer toBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * getWidth() * getHeight());
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Pixel pixel = get(x, y);
                buffer.put(pixel.r);
                buffer.put(pixel.g);
                buffer.put(pixel.b);
                buffer.put(pixel.a);
            }
        }
        buffer.rewind();
        return buffer;
    }

    @Override
    public String toString() {
        String str = "";
        str += getDimension().toString() + StringUtil.NEWLINE;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Pixel pixel = get(x, y);
                str += new Dimension(x, y).toString() + " " + pixel.toString();
                str += StringUtil.NEWLINE;
            }
        }
        return str;
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(getWidth(), getHeight());
    }

}
