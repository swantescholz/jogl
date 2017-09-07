package de.sscholz.input;

import com.sun.opengl.util.BufferUtil;

import javax.media.opengl.GL;
import java.awt.*;
import java.awt.event.*;
import java.nio.IntBuffer;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
    private float view_transx = 0.0f, view_transy = 0.0f, view_transz = -10.0f;
    private int prevMouseX, prevMouseY;


    public void applyMouseTranslation(GL gl) {
        IntBuffer buffer = BufferUtil.newIntBuffer(1);
        gl.glGetIntegerv(GL.GL_MATRIX_MODE, buffer);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glTranslatef(view_transx, view_transy, view_transz);
        gl.glMatrixMode(buffer.get(0));
    }

    public void applyMouseRotation(GL gl) {
        IntBuffer buffer = BufferUtil.newIntBuffer(1);
        gl.glGetIntegerv(GL.GL_MATRIX_MODE, buffer);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glRotatef(view_rotx, 1f, 0f, 0f);
        gl.glRotatef(view_roty, 0f, 1f, 0f);
        gl.glRotatef(view_rotz, 0f, 0f, 1f);
        gl.glMatrixMode(buffer.get(0));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Dimension size = e.getComponent().getSize();

        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
            float thetaY = 360f * ((float) (x - prevMouseX) / (float) size.width);
            float thetaX = 360f * ((float) (prevMouseY - y) / (float) size.height);
            prevMouseX = x;
            prevMouseY = y;
            view_rotx -= thetaX;
            view_roty += thetaY;
        }
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
            float thetaX = 2f * ((float) (x - prevMouseX) / (float) size.width);
            float thetaY = 2f * ((float) (prevMouseY - y) / (float) size.height);
            prevMouseX = x;
            prevMouseY = y;
            view_transx += thetaX;
            view_transy += thetaY;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        view_transz += e.getWheelRotation() * 0.5f;
    }
}
