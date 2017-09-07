package de.sscholz;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import de.sscholz.input.Keyboard;
import de.sscholz.input.Mouse;
import de.sscholz.util.GlUtil;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class JoglApplication extends JFrame implements GLEventListener {

    protected Keyboard keyboard = new Keyboard();
    protected Mouse mouse = new Mouse();

    protected FPSAnimator animator;
    protected GLCanvas canvas;

    private boolean fullscreen = false;
    private boolean listenersInitialized = false;


    public JoglApplication() {
        canvas = new GLCanvas();
        canvas.addGLEventListener(this);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    killWindow();
                }
            }
        });

        animator = new FPSAnimator(canvas, 60);
        setTitle("My JOGL Application");
        setSize(800, 600);
        add(canvas);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                killWindow();
            }
        });
    }


    private void killWindow() {
        System.out.println("Exit...");
        setVisible(false);
        this.dispose();
        System.exit(0);
    }

    protected abstract void setUp();

    protected abstract void draw();

    public void setVisible(boolean b) {
        boolean visible = isVisible();
        super.setVisible(b);
        if (visible != b) {
            if (b) {
                getAnimator().start();
                canvas.requestFocus();
            } else {
                getAnimator().stop();
            }
        }
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        //printGlInfo(gl);

        if (!listenersInitialized) {
            listenersInitialized = true;
            drawable.addMouseListener(mouse);
            drawable.addMouseMotionListener(mouse);
            drawable.addMouseWheelListener(mouse);
            drawable.addKeyListener(keyboard);
        }

        GlUtil.initGl(gl);
        setUp();
    }

    private void printGlInfo(GL gl) {
        System.out.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.out.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.out.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GlUtil.reshapeGlView(gl, x, y, width, height);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        GlUtil.initFrame(gl);
        mouse.applyMouseTranslation(gl);
        mouse.applyMouseRotation(gl);
        draw();
    }

    public Animator getAnimator() {
        return animator;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        if (fullscreen == this.fullscreen)
            return;

        boolean visible = isVisible();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        if (fullscreen) {
            this.fullscreen = true;
            setVisible(false);
            dispose();
            setUndecorated(true);
            setResizable(false);
            gd.setFullScreenWindow(this);
        } else {
            this.fullscreen = false;
            gd.setFullScreenWindow(null);
            setVisible(false);
            dispose();
            setUndecorated(false);
            setResizable(true);
            setSize(600, 400);
        }

        setVisible(visible);
    }

}