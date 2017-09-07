package de.sscholz.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Keyboard implements KeyListener {
    private Map<Integer, KeyState> keymap = new HashMap<Integer, KeyState>();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keymap.put(e.getKeyCode(), KeyState.PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keymap.put(e.getKeyCode(), KeyState.RELEASED);
    }

    public boolean wasPressed(int keycode) {
        if (!keymap.containsKey(keycode)) {
            return false;
        }
        KeyState state = keymap.get(keycode);
        if (state == KeyState.PRESSED) {
            keymap.put(keycode, KeyState.DOWN);
            return true;
        }
        return false;
    }

    public boolean isDown(int keycode) {
        if (!keymap.containsKey(keycode)) {
            return false;
        }
        KeyState state = keymap.get(keycode);
        return state == KeyState.DOWN || state == KeyState.PRESSED;
    }

    public enum KeyState {
        UP,
        PRESSED,
        DOWN,
        RELEASED
    }
}
