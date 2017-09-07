package de.sscholz;

import de.sscholz.gfx.Scene;

import static java.lang.System.out;

public class MyApplication extends JoglApplication {

    Scene scene = new Scene(keyboard);

    public static void main(String[] args) {

        mymain();

    }


    private static void mymain() {
        out.println("Starting...");


        MyApplication template = new MyApplication();
        template.setVisible(true);
    }

    public void setUp() {
        scene.init();
        System.out.println(canvas.getWidth() + ", " + canvas.getHeight() + " canvas");
    }

    public void draw() {
        scene.update();
        scene.render();
    }

}
