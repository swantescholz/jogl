package de.sscholz.util;

import de.sscholz.gfx.light.Light;
import de.sscholz.math.Color;
import de.sscholz.math.Dimension;
import de.sscholz.math.MathUtil;
import de.sscholz.math.Matrix;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GlUtil {

    private static GL gl;
    private static boolean wireframed = false;
    private static Dimension dimension = new Dimension();
    private static boolean useOrthoProjection = false;

    public static GL getGl() {
        return gl;
    }

    private static void setGl(GL gl) {
        GlUtil.gl = gl;
        GlOperator.updateAll();
    }

    public static int[] toArray(int i) {
        int[] tmp = new int[1];
        tmp[0] = i;
        return tmp;
    }

    public static void toggleWireframeMode() {
        if (wireframed) {
            //gl.glEnable(GL.GL_CULL_FACE);
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
        } else {
            //gl.glDisable(GL.GL_CULL_FACE);
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        }
        wireframed = !wireframed;
    }

    public static void toggleShadeModel() {
        int model = getInteger(GL.GL_SHADE_MODEL);
        int newModel = GL.GL_FLAT;
        if (model == GL.GL_FLAT) {
            newModel = GL.GL_SMOOTH;
        }
        gl.glShadeModel(newModel);
    }

    public static void toggleEnabled(int field) {
        if (gl.glIsEnabled(field)) {
            gl.glDisable(field);
        } else {
            gl.glEnable(field);
        }
    }

    private static int getInteger(int field) {
        IntBuffer buffer = IntBuffer.allocate(1);
        gl.glGetIntegerv(field, buffer);
        return buffer.get();
    }

    public static void setClearColor(Color bgColor) {
        gl.glClearColor((float) bgColor.r, (float) bgColor.g, (float) bgColor.b, (float) bgColor.a);
    }

    public static void initGl(GL gl) {
        setGl(gl);
        setClearColor(Color.sky());
        gl.glClearDepth(1.0f);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

        //gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);
        gl.glFrontFace(GL.GL_CCW);

        gl.glEnable(GL.GL_BLEND);


        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_RESCALE_NORMAL);

        gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_FALSE);
        Light.setGlobalAmbient(Color.white().clone().mul(0.2));

        gl.glEnable(GL.GL_TEXTURE_2D);
    }


    public static Dimension getDimension() {
        return dimension;
    }

    public static double getAspect() {
        return (double) dimension.width / (double) dimension.height;
    }

    private static void setDimension(int width, int height) {
        dimension.width = width;
        dimension.height = height;
    }

    public static void reshapeGlView(GL gl, int x, int y, int width, int height) {
        setGl(gl);
        System.out.println("reshaping: " + x + ", " + y + ", " + width + ", " + height);
        gl.glViewport(0, 0, width, height);
        setDimension(width, height);
        updateProjectionMatrix();
    }

    private static void updateProjectionMatrix() {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        if (useOrthoProjection) {
            double top = 20.0;
            double right = top * getAspect();
            double far = top;
            //gl.glOrtho(-right, right, -top, top, -far, far);
            Matrix ortho = Matrix.ortho(-right, right, -top, top, -far, far);
            ortho.glLoad();
        } else {
            Matrix projectionMatrix = Matrix.projection(MathUtil.toRadian(80.0), getAspect(), 0.01, 10000.0);
            projectionMatrix.glLoad();
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static void toggleProjectionMode() {
        useOrthoProjection = !useOrthoProjection;
        updateProjectionMatrix();
    }

    public static void initFrame(GL gl) {
        setGl(gl);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static Dimension getViewportDimension() {
        int[] data = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, data, 0);
        return new Dimension(data[2], data[3]);
    }

    public static void saveScreenshot(File file) {
        BufferedImage image = readScreenshot();
        try {
            file.mkdirs();
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage readScreenshot() {
        Dimension dimension = getViewportDimension();
        final int bytesPerPixel = 3;
        System.out.println("screenshot dimension: " + dimension);
        ByteBuffer buffer = ByteBuffer.allocate(dimension.size() * bytesPerPixel);
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
        gl.glReadBuffer(GL.GL_FRONT);
        gl.glReadPixels(0, 0, dimension.width, dimension.height,
                GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buffer);
        BufferedImage image = new BufferedImage(dimension.width,
                dimension.height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < dimension.height; y++) {
            for (int x = 0; x < dimension.width; x++) {
                int index = ((dimension.height - y - 1) * dimension.width + x) * bytesPerPixel;
                byte red = buffer.get(index);
                byte green = buffer.get(index + 1);
                byte blue = buffer.get(index + 2);
                int rgb = Color.rgbIntFromBytes(red, green, blue);
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    public static void pushEnabled() {
        gl.glPushAttrib(GL.GL_ENABLE_BIT);
    }

    public static void popEnabled() {
        gl.glPopAttrib();
    }

}
