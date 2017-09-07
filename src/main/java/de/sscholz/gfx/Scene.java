package de.sscholz.gfx;

import de.sscholz.gfx.light.Light;
import de.sscholz.gfx.light.PositionalLight;
import de.sscholz.gfx.model.*;
import de.sscholz.gfx.texture.*;
import de.sscholz.input.Keyboard;
import de.sscholz.math.*;
import de.sscholz.util.*;

import javax.media.opengl.GL;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class Scene extends GlOperator {

    private Camera camera = new Camera();
    private Keyboard keyboard;
    private ModelManager modelManager = new ModelManager(new File("res/model"), "");

    private double elapsed;
    private boolean alive = true;

    private final double moveSpeed = 4.0;
    private final double rotSpeed = MathUtil.toRadian(220);

    Timer timer = new Timer(5.0);
    FrameRateComputer fpsComputer = new FrameRateComputer();
    Model cube, cylinder, sphere;
    PositionalLight light;
    Slider slider = new Slider(0.5);
    Nurbs nurbs = new Nurbs(2, 2);
    Grid grid;
    ColoredMesh initialA, initialB;
    TextureManager textureManager = new TextureManager(new File("res/textures"), ".png");
    Texture texture;
    Vector3 spherePos = new Vector3();
    IntSlider filterSize = new IntSlider(0, 100);

    public Scene(Keyboard keyboard) {
        this.keyboard = keyboard;
        camera.position.set(new Vector3(-2, 3, 7));
        camera.lookAt(Vector3.zero());
    }

    public void init() {
        light = new PositionalLight();
        light.ambient.set(Color.black());
        light.diffuse.set(Color.white());
        light.specular.set(Color.white());
        light.position.set(new Vector3(-2, 4, 4));

        modelManager.loadOff("cube.off");
        modelManager.loadOff("cylinder.off");
        modelManager.loadObj("sphere.obj");
        cube = modelManager.get("cube.off");
        cylinder = modelManager.get("cylinder.off");
        sphere = modelManager.get("sphere.obj");
        reload();
    }

    private void createTexture() {
        PixelGrid grid = null;
        try {
            grid = PixelGrid.createFromFile(textureManager.getFile("redgrid"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        boxcarFilter(grid, filterSize.getValue());

        TextureData data = new TextureData(grid);
        texture = new Texture(data);
    }

    private void boxcarFilter(PixelGrid grid, int size) {
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Color average = Color.zero();
                int counter = 0;
                for (int b = -size; b <= size; b++) {
                    for (int a = -size; a <= size; a++) {
                        Pixel p = grid.get(x + a, y + b);
                        if (p != null) {
                            average.add(p.toColor());
                            counter++;
                        }
                    }
                }
                average.div(counter);
                Pixel target = grid.get(x, y);
                target.set(average.toPixel());
            }
        }
    }

    private void reload() {
        grid = new Grid(25, 25);
        grid.arrangePlane(new Vector3(-10, 4, -10), new Vector3(10, 0, -10),
                new Vector3(-10, 0, 10));
        //grid.randomizeAxis(Vector3.y(), -8, -3);
        readExpression();
        nurbs.create(grid);
        Mesh meshA = Mesh.createFromObj(FileUtil.readFile(modelManager.getFile("sa.obj")));
        Mesh meshB = Mesh.createFromObj(FileUtil.readFile(modelManager.getFile("sb.obj")));
        initialA = new ColoredMesh(meshA);
        initialB = new ColoredMesh(meshB);
        createTexture();
    }

    private void readExpression() {
        grid.applyYExpression(FileUtil.readFile(new File("res/text/expr.txt")));
    }

    private void setupFrame() {
        camera.apply();

        //gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        Texture.disable();

        light.shine();
        drawCoordinateSystem();
        drawLight();
    }

    public void render() {
        setupFrame();

        Material.mellow().gl();
        nurbs.render();
        sphere.render();
        drawMesh();
        textureTest();
    }

    private void drawMesh() {
        Vector3 offset = new Vector3(9, 0, 0);


        GlUtil.pushEnabled();
        Texture.enable();
        texture.bind();

        Material.brass().gl();

        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        initialA.render();
        initialB.setTransformation(Matrix.translation(offset));
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        initialB.render();

        GlUtil.popEnabled();
    }

    private void drawLight() {
        GlUtil.pushEnabled();
        Light.disable();
        Texture.disable();
        Color.gold().gl();
        Model lightBall = modelManager.get("sphere.obj");
        lightBall.setTransformation(Matrix.scaling(.33).mul(Matrix.translation(light.position)));
        lightBall.render();
        GlUtil.popEnabled();
    }

    public void update() {
        fpsComputer.update();
        elapsed = fpsComputer.getElapsed();
        Timer.updateAll(elapsed);
        if (timer.isExpired()) {
            System.out.println("FPS: " + fpsComputer.getAverageFps());
            timer.reset();
        }
        processInput();
    }

    private void processInput() {
        if (keyboard.wasPressed(KeyEvent.VK_ESCAPE))
            alive = false;
        if (keyboard.isDown(KeyEvent.VK_C))
            camera.advance(moveSpeed * elapsed);
        if (keyboard.isDown(KeyEvent.VK_X))
            camera.advance(-moveSpeed * elapsed);
        if (keyboard.isDown(KeyEvent.VK_LEFT))
            camera.yawAroundOriginalUpVector(-rotSpeed * elapsed);
        if (keyboard.isDown(KeyEvent.VK_RIGHT))
            camera.yawAroundOriginalUpVector(rotSpeed * elapsed);

        if (keyboard.isDown(KeyEvent.VK_UP))
            camera.pitch(rotSpeed * elapsed);
        if (keyboard.isDown(KeyEvent.VK_DOWN))
            camera.pitch(-rotSpeed * elapsed);

        moveVector(light.position, 1.0 * moveSpeed, KeyEvent.VK_A, KeyEvent.VK_D,
                KeyEvent.VK_E, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_W);

        updateSlider();
        if (keyboard.wasPressed(KeyEvent.VK_1))
            GlUtil.toggleWireframeMode();
        if (keyboard.wasPressed(KeyEvent.VK_2))
            GlUtil.toggleProjectionMode();
        if (keyboard.wasPressed(KeyEvent.VK_3))
            GlUtil.toggleShadeModel();
        if (keyboard.wasPressed(KeyEvent.VK_4))
            GlUtil.toggleEnabled(GL.GL_LIGHTING);
        if (keyboard.wasPressed(KeyEvent.VK_5))
            GlUtil.toggleEnabled(GL.GL_TEXTURE_2D);
        if (keyboard.wasPressed(KeyEvent.VK_P))
            saveScreenshot();
        if (keyboard.wasPressed(KeyEvent.VK_R))
            reload();
        if (keyboard.wasPressed(KeyEvent.VK_V)) {
            filterSize.progress(-1);
            reload();
        }
        if (keyboard.wasPressed(KeyEvent.VK_B)) {
            filterSize.progress(1);
            reload();
        }

        moveVector(spherePos, 1.0 * moveSpeed, KeyEvent.VK_J, KeyEvent.VK_L,
                KeyEvent.VK_O, KeyEvent.VK_U, KeyEvent.VK_K, KeyEvent.VK_I);
        Matrix m = Matrix.scaling(MathUtil.interpolate(1, 8, slider.getValue())).mul(Matrix.translation(spherePos));
        sphere.setTransformation(m);
    }

    private void saveScreenshot() {
        File file = new File("screenshots", StringUtil.currentTimestamp() + ".jpg");
        GlUtil.saveScreenshot(file);
    }


    private void drawCoordinateSystem() {
        GlUtil.pushEnabled();
        Light.disable();
        Texture.disable();
        gl.glBegin(GL.GL_LINES);
        double axisLength = 20.0;
        Color.red().gl();
        gl.glVertex3d(-axisLength, 0, 0);
        gl.glVertex3d(axisLength, 0, 0);
        Color.green().gl();
        gl.glVertex3d(0, -axisLength, 0);
        gl.glVertex3d(0, axisLength, 0);
        Color.blue().gl();
        gl.glVertex3d(0, 0, -axisLength);
        gl.glVertex3d(0, 0, axisLength);
        gl.glEnd();
        GlUtil.popEnabled();
    }

    public boolean isDead() {
        return !alive;
    }

    private void updateSlider() {
        if (keyboard.isDown(KeyEvent.VK_F))
            slider.progress(-elapsed);
        if (keyboard.isDown(KeyEvent.VK_G))
            slider.progress(elapsed);
    }

    private void moveVector(Vector3 position, double speed, int left, int right,
                            int up, int down, int back, int forward) {
        if (keyboard.isDown(left))
            position.x -= speed * elapsed;
        if (keyboard.isDown(right))
            position.x += speed * elapsed;
        if (keyboard.isDown(up))
            position.y += speed * elapsed;
        if (keyboard.isDown(down))
            position.y -= speed * elapsed;
        if (keyboard.isDown(back))
            position.z += speed * elapsed;
        if (keyboard.isDown(forward))
            position.z -= speed * elapsed;
    }

    private void textureTest() {

        GlUtil.pushEnabled();
        Light.disable();
        Texture.enable();

        texture.bind();
        gl.glBegin(GL.GL_QUADS);
        Color.white().gl();

        Vector2.zero().glTex();
        Vector3.zero().gl();
        Vector2.x().glTex();
        Vector3.x().gl();
        Vector2.xy().glTex();
        Vector3.xy().gl();
        Vector2.y().glTex();
        Vector3.y().gl();
        gl.glEnd();

        GlUtil.popEnabled();
    }

}
