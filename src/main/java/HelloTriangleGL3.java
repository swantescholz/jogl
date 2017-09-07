/**
 * Copyright 2012-2013 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */


import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

import java.nio.FloatBuffer;

/**
 * OpenGL 3 "Hello Triangle". Draws a triangle using OpenGL 3 Core(!).
 * READ AND UNDERSTAND THE WHOLE DOCUMENT BEFORE YOU START PROGRAMMING. This is especially true if
 * you already know fixed function OpenGL. Compared to "OpenGL 3" the "Core"-Profile does not
 * include deprecated functions, i.e. the majority of things you now about the fixed function
 * pipeline is gone (e.g. glBegin() and glEnd())! The Programmable pipeline in OpenGL 3 Core is
 * both, fast and flexible yet it takes some extra lines of code to setup.
 *
 * Based on
 * - "JOGL2 OpenGL ES 2 demo" by Xerxes Rånby
 *    http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD
 * - "Learning Modern 3D Graphics Programming" by Jason L. McKesson
 *    http://www.arcsynthesis.org/gltut/
 * - "Tutorials for modern OpenGL (3.3+)"
 *    http://www.opengl-tutorial.org/
 * Thanks a lot. If you want more explanations and other examples the last two sources are just
 * perfect for learning and using OpenGL 3.3.
 *
 * This is a minimal example. In the next week you will get a more convenient template, that
 * encapsulates some of the basic functionalities.
 *
 * @author Stephan Arens
 */
public class HelloTriangleGL3 implements GLEventListener {
    //GLEventListener is our interface to OpenGL. It is only 7 lines of code, have a look at it.

    private String vertexShaderString =

		/* Introducing the GLSL Vertex shader
		 *
		 * The main loop inside the vertex shader gets executed ONE TIME FOR EACH VERTEX.
		 *
		 *      vertex -> *       uniform data -> mat4 projection = ( 1, 0, 0, 0,
		 *      (0,1,0)  / \                                          0, 1, 0, 0,
		 *              / . \  <- origin (0,0,0)                      0, 0, 1, 0,
		 *             /     \                                        0, 0, 0, 1 );
		 *  vertex -> *-------* <- vertex
		 *  (-1,-1,0)             (1,-1,0) <- attribute data can be used
		 *                        (0, 0,1)    for color, position, normals etc.
		 *
		 * The vertex shader receives input data in form of "uniform" data that is common to all
		 * vertices and "attribute" data that is individual to each vertex. One vertex can have
		 * several "attribute" data sources enabled.
		 *
		 * The vertex shader produces output used by the fragment shader. gl_Position is expected
		 * to get set to the final vertex position. You can also send additional user defined
		 * "varying" data to the fragment shader.
		 *
		 * Model Translate, Scale and Rotate are typically done here by matrix-multiplying a matrix
		 * against each vertex position.
		 *
		 * The whole vertex shader program are a String containing GLSL Code sent to the GPU driver
		 * for compilation.
		 */

            "#version 330 \n"+

                    "uniform mat4 uProjection; \n" + 			   	// Incoming data used by the vertex shader:
                    "layout(location = 0) in vec4 aPosition; \n" +	// uniforms and attributes.
                    "layout(location = 1) in vec4 aColor; \n" +   	// "layout location" for attributes must be
                    // exactly as specified in
                    // glVertexAttribPointer(0, ...)

                    "smooth out vec4 vColor; \n" + 					// Outgoing varying data sent to the
                    // fragment shader

                    //out vec4 gl_Position;							// gl_Position is an output that is already
                    // implicitly defined by the system. A
                    // vertex shader must at least provide this
                    // (clip-space) position, for the clipper
                    // and rasterizer to work.

                    "void main(void) \n" + 							// Here comes the actual vertex program.
                    "{ \n" +
                    "  vColor = aColor; \n" +
                    "  gl_Position = uProjection * aPosition; \n" +
                    "} \n";


    private String fragmentShaderString =

		/* Introducing the GLSL Fragment shader
		 *
		 * The main loop of the fragment shader gets executed for each visible pixel fragment on the
		 * render buffer.
		 *
		 *       vertex-> *
		 *       (0,1,0) /f\
		 *              /ffF\ <- This fragment F gl_FragCoord get interpolated
		 *             /fffff\                   based on the three
		 *   vertex-> *fffffff* <-vertex         vertex gl_Position.
		 *  (-1,-1,0)           (1,-1,0)
		 *
		 *
		 * All incoming "varying" and gl_FragCoord data to the fragment shader gets interpolated
		 * based on the vertex positions and outgoing data. Additionally a viewport transform is
		 * done. This is just scaling from clipspace [-1, +1] to screenspace e.g. [0, 1024].
		 * Again: gl_FragCoord is exactly the same as the gl_Position output from the vertex shader,
		 * but interpolated for fragments inbetween and scaled to fit pixel positions.
		 *
		 * The fragment shader produces and stores the final color data output into gl_FragColor.
		 *
		 * Is up to you to set the final colors and calculate lighting here based on supplied
		 * position, color, normal data....
		 *
		 * The whole fragment shader program is a String containing GLSL Code sent to the GPU driver
		 * for compilation.
		 */

            "#version 330 \n" +

                    "smooth in vec4 vColor; \n" + 	// incoming varying data to the fragment shader
                    // sent from the vertex shader. Must have the same
                    // names as the "outs" of the vertex shader

                    //in vec3 gl_FragCoord;				 	// gl_FragCoord is an input that is already
                    // implicitly defined by the system.

                    "out vec4 fFragmentColor; \n" +  		// A fragment shader must provide an "out vec4"
                    // which will be automatically used to write into
                    // the render target.

                    "void main (void) \n" +					// Here comes the actual fragment program.
                    "{ \n" +
                    "  fFragmentColor = vColor; \n" +
                    "} ";

    private double t0 = System.currentTimeMillis();
    private static int windowWidth = 640;
    private static int windowHeight = 480;

    private int shaderProgram;
    private int vertShader;
    private int fragShader;
    private int uProjection_location;

    int[] vboHandles;
    private int vboVertices, vboColors;


    public static void main(String[] args){

        /* This demo is based on the GL3 Core Profile that uses common hardware acceleration
         * functionality of desktop OpenGL 3 devices.
         * JogAmp JOGL will probe all the installed libGL.so, libEGL.so and libGLESv2.so libraries
         * on the system to find which one provides hardware acceleration for your GPU device.
         * It is common to find more than one version of these libraries installed on a system.
         * Good news!: JOGL does all this probing for you. All you have to do is to ask for
         * the GLProfile you want to use.
         */
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        // We may at this point tweak the caps and request a translucent drawable
        caps.setBackgroundOpaque(false);
        GLWindow glWindow = GLWindow.create(caps);

        /* You may combine the NEWT GLWindow inside existing Swing and AWT applications by
         * encapsulating the glWindow inside a com.jogamp.newt.awt.NewtCanvasAWT canvas.
         *
         *  NewtCanvasAWT newtCanvas = new NewtCanvasAWT(glWindow);
         *  JFrame frame = new JFrame("RAW GL3 Demo inside a JFrame!");
         *  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         *  frame.setSize(width, height);
         *  frame.add(newtCanvas);
         *  // add some swing code if you like.
         *  // javax.swing.JButton b = new javax.swing.JButton();
         *  // b.setText("Hi");
         *  // frame.add(b);
         *  frame.setVisible(true);
         */

        // In this demo we prefer to setup and view the GLWindow directly. This allows the demo to
        // run on -Djava.awt.headless=true systems
        glWindow.setTitle("Hello Triangle");
        glWindow.setSize(windowWidth, windowHeight);
        glWindow.setUndecorated(false);
        glWindow.setPointerVisible(true);
        glWindow.setVisible(true);

        // Finally we connect the GLEventListener application code to the NEWT GLWindow. GLWindow
        // will call the GLEventListener init, reshape, display and dispose functions when needed.
        glWindow.addGLEventListener(new HelloTriangleGL3() /* Our GLEventListener */);
        Animator animator = new Animator();
        animator.add(glWindow);
        animator.start();
    }


    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        // Make sure GL3 Core is supported
        if(gl.isGL3core()){
            System.out.println("GL3 core detected");
        } else {
            System.out.println("ERROR: GL3 core not detected");
            System.exit(1);
        }


        // Create GPU shader handles
        // OpenGL returns an index id to be stored for future reference.
        vertShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        fragShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);


        // COMPILE the vertexShader String into a program.
        String[] vlines = new String[] { vertexShaderString };
        int[] vlengths = new int[] { vlines[0].length() };
        gl.glShaderSource(vertShader, vlines.length, vlines, vlengths, 0);
        gl.glCompileShader(vertShader);

        // Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(vertShader, GL3.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != 0) {
            System.out.println("Horray! vertex shader compiled");
        } else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(vertShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(vertShader, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error compiling the vertex shader: " + new String(log));
            System.exit(1);
        }


        // COMPILE the fragmentShader String into a program.
        String[] flines = new String[] { fragmentShaderString };
        int[] flengths = new int[] { flines[0].length() };
        gl.glShaderSource(fragShader, flines.length, flines, flengths, 0);
        gl.glCompileShader(fragShader);

        // Check compile status.
        gl.glGetShaderiv(fragShader, GL3.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != 0) {
            System.out.println("Horray! fragment shader compiled");
        } else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(fragShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(fragShader, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error compiling the fragment shader: " + new String(log));
            System.exit(1);
        }

        // OpenGL returns an index id to be stored for future reference.
        shaderProgram = gl.glCreateProgram();
        // Each shaderProgram must have one vertex shader and one fragment shader.
        gl.glAttachShader(shaderProgram, vertShader);
        gl.glAttachShader(shaderProgram, fragShader);


        // LINK the program
        gl.glLinkProgram(shaderProgram);

        // Check link status.
        int[] linked = new int[1];
        gl.glGetProgramiv(shaderProgram, GL3.GL_LINK_STATUS, linked, 0);
        if (linked[0] != 0) {
            System.out.println("Horray! shader program linked succesfully");
        } else {
            int[] logLength = new int[1];
            gl.glGetProgramiv(shaderProgram, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(shaderProgram, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error linking the shader program: " + new String(log));
            System.exit(1);
        }


        // Get an id number to the uProjection matrix so that we can update it.
        uProjection_location = gl.glGetUniformLocation(shaderProgram, "uProjection");

        /* GL3 core and later mandates that a "Vertex Buffer Object" must
         * be created and bound before calls such as gl.glDrawArrays is used.
         *
         * Generate two VBO pointers / handles
         * VBO is a data buffer stored inside the graphics card memory.
         */

        vboHandles = new int[2];
        gl.glGenBuffers(2, vboHandles, 0);
        vboColors = vboHandles[0];
        vboVertices = vboHandles[1];
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        System.out.println("Window resized to width=" + width + " height=" + height);
        windowWidth = width;
        windowHeight = height;

        // Get gl
        GL3 gl = drawable.getGL().getGL3();

        // Optional: Set viewport (complete window is default)
        gl.glViewport(0, 0, windowWidth, windowHeight);
    }


    public void display(GLAutoDrawable drawable) {

        float delta = (float)( 0.0001 * (System.currentTimeMillis() - t0));

        // Get gl
        GL3 gl = drawable.getGL().getGL3();

        // Clear screen
        gl.glClearColor(1, 0, 1, 0.5f); // Purple (translucency hardly visible. If you want to see
        // it, set glWindow.setUndecorated(true) in the main method)
        gl.glClear(GL3.GL_STENCIL_BUFFER_BIT |
                GL3.GL_COLOR_BUFFER_BIT   |
                GL3.GL_DEPTH_BUFFER_BIT   );

        // Use the shaderProgram that got linked during the init part.
        gl.glUseProgram(shaderProgram);

        // Set a projection matrix
        float[] projection = { 1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1}; // identity, keep this example simple...

        // Send the projection matrix to the vertex shader by using the uniform location id
        // obtained during the init part.
        gl.glUniformMatrix4fv(uProjection_location, 1, false, projection, 0);



        /*
         *  Render a triangle:
         *  The OpenGL 3 code below basically matches this OpenGL code in Fixed Function Pipeline.
         *
         *    gl.glBegin(GL_TRIANGLES);                      // Drawing Using Triangles
         *    gl.glVertex3f( delta, 1.0f, 0.0f);              // Top
         *    gl.glVertex3f(-1.0f,-1.0f, 0.0f);              // Bottom Left
         *    gl.glVertex3f( 1.0f,-1.0f, 0.0f);              // Bottom Right
         *    gl.glEnd();                            		 // Finished Drawing The Triangle
         */

        float[] vertices = { delta,  1.0f, 0.0f, 1.0f,  //Top
                -1.0f, -1.0f, 0.0f, 1.0f,  //Bottom Left
                1.0f, -1.0f, 0.0f, 1.0f}; //Bottom Right


        // Observe that the vertex data passed to glVertexAttribPointer must stay valid through the
        // OpenGL rendering lifecycle. Therefore it is mandatory to allocate a NIO Direct buffer
        // that stays pinned in memory and thus can not get moved by the java garbage collector.
        // Also we need to keep a reference to the NIO Direct buffer around up until we call
        // glDisableVertexAttribArray, then it will be safe to garbage collect the memory.
        // In JOGL, functions that have untyped parameters (GLvoid*) with deferred use (not used
        // immediately) must use direct buffers (glBufferData).
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);


        // Select the VBO, GPU memory data, to use for vertices
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboVertices);

        // Transfer data to VBO, this performs the copy of data from CPU -> GPU memory
        int numBytes = vertices.length * 4; //Float == 4 Byte
        gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, fbVertices, GL.GL_STATIC_DRAW);
        fbVertices = null; // It is OK to release CPU vertices memory after transfer to GPU

        // Associate Vertex attribute 0 with the last bound VBO
        gl.glVertexAttribPointer(0 /* the vertex attribute */,
                4, /* xyzw == four values used for each vertex */
                GL3.GL_FLOAT, false /* normalized? */, 0 /* stride */,
                0 /* The bound VBO data offset */);

        gl.glEnableVertexAttribArray(0); // This 0 is the same 0 as the first parameter of
        // glVertexAttribPointer(0, ...) in the line above and as
        // in the vertex shader "layout(location = 0) ...".
        // You can also use glGetAttribLocation() to make these
        // locations less "voodoo" (After LINKING the program!)

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0); // THIS 0 means to unbind the current buffer from
        // the current context. It has nothing to do with
        // the 0 above, it is like to the keyword "null"!
        // You can unbind the VBO after it has been
        // associated using glVertexAttribPointer.


        float[] colors = {1.0f, 0.0f, 0.0f, 1.0f,  //Top color (red, no transparency)
                0.0f, 1.0f, 0.0f, 1.0f,  //Bottom Left color (green, no transparency)
                0.0f, 0.0f, 1.0f, 1.0f}; //Bottom Right color (blue, no transparency)


        FloatBuffer fbColors = Buffers.newDirectFloatBuffer(colors);

        // Select the VBO, GPU memory data, to use for colors
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboColors);
        numBytes = colors.length * 4; //Float == 4 Byte
        gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, fbColors, GL.GL_STATIC_DRAW);
        fbColors = null; // It is OK to release CPU color memory after transfer to GPU

        // Associate Vertex attribute 1 with the last bound VBO
        gl.glVertexAttribPointer(1 /* the vertex attribute */,
                4 /* four values used for each color */,
                GL3.GL_FLOAT, false /* normalized? */, 0 /* stride */,
                0 /* The bound VBO data offset */);

        gl.glEnableVertexAttribArray(1); // This 1 is the same 1 as the first parameter of
        // glVertexAttribPointer(1, ...) in the line above and as
        // in the vertex shader "layout(location = 1) ..."
        // You can also use glGetAttribLocation() to make these
        // locations less "voodoo" (After LINKING the program!)

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0); // You can unbind "null" the VBO after it has been
        // associated using glVertexAttribPointer


        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, 3); //Draw 3 vertices as triangle, beginning with 0


        gl.glDisableVertexAttribArray(0); // Allow release of vertex position memory
        gl.glDisableVertexAttribArray(1); // Allow release of vertex color memory

        gl.glUseProgram(0); // not necessary, but is convenient to undo everything after drawing
    }


    public void dispose(GLAutoDrawable drawable){
        System.out.println("cleanup, remember to release shaders");
        GL3 gl = drawable.getGL().getGL3();

        gl.glDeleteBuffers(2, vboHandles, 0); // Release VBO, color and vertices, buffer GPU memory.

        gl.glUseProgram(0);
        gl.glDetachShader(shaderProgram, vertShader);
        gl.glDeleteShader(vertShader);
        gl.glDetachShader(shaderProgram, fragShader);
        gl.glDeleteShader(fragShader);
        gl.glDeleteProgram(shaderProgram);
        System.exit(0);
    }

}
