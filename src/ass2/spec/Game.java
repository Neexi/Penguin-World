package ass2.spec;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Camera myCamera;
	private Terrain myTerrain;
	
	//Texture
	private int numTexture = 8;
	private String texFolder = "pict";
	private String[] texFilename = {"snow.jpg","log.png","snow.jpg","ice.jpg","sea.jpg","sky2.jpg","aurora.jpg","frost.png"};
	private String[] texExtension = {"jpg","png","jpg","jpg","jpg","jpg","jpg","png"};
	private float[][] texColor = {
			{201/255f, 249/255f, 255/255f, 1.0f},
			{0.325f, 0.208f, 0.039f, 1.0f},
			{1.0f,250/255f,250/255f,1.0f},
			{165/255f, 242/255f, 243/255f, 1.0f},
			{163/255f,242/255f,255/255f,1.0f},
			{1.0f,1.0f,1.0f,1.0f},
			{1.0f,1.0f,1.0f,1.0f},
			{245/255f,245/255f,255/255f,1.0f}
	};
	private boolean[] mip = {true, true, true, true,true,true,true, true};
	private Texture[] texture = new Texture[numTexture];

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
   
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel(caps);
          panel.addGLEventListener(this);
 
       // add a GL Event listener to handle rendering
          panel.addGLEventListener(myCamera);
          // NEW: add a key listener to respond to keypresses
          panel.addKeyListener(myCamera);
       
          
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          JTextArea text = readme();
          setLayout(new BorderLayout());
          add(panel, BorderLayout.CENTER);
          add(text, BorderLayout.SOUTH); //Adding command list box
          setSize(1800, 900);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        myCamera = new Camera(terrain);
        game.run();
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();

    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	

    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	
    	//You can use glu look at or normal transformations
    	//to position the camera.
    	GLU glu = new GLU();
    	//Handling camera movement
    	myCamera.changeAngle();
    	myCamera.moved();
    	double x = myCamera.getX();
    	double z = myCamera.getZ();
    	double y = myCamera.getY();
    	double angle = myCamera.getAngle();
    	double xAngle = Math.sin(Math.toRadians(angle));
    	double zAngle = -Math.cos(Math.toRadians(angle));
    	
    	boolean idle = myCamera.getIdle(); //Check if avatar is moving
    	//Set the camera perspective using current point calculation
    	glu.gluLookAt((xAngle/2)*2+x, 1.1+y, (zAngle/2)*2+z, x-(xAngle/2)*2, y, z-(zAngle/2)*2, 0, 1, 0);
    	//glu.gluLookAt((xAngle/2)*2+x, 1.1+y, (zAngle/2)*2+z, x+(xAngle/2)*1, y, z+(zAngle/2)*1, 0, 1, 0);
    	
    	//Setting up light
    	//Set light0 and light1 ambience etc
        Lighting.setLight0(gl, myCamera.getNight()); //Difference ambience and diffuse at night
        Lighting.setLight1(gl);
    	if(!myCamera.getNight()) { //Disable LIGHT1 at day
    		gl.glDisable(GL2.GL_LIGHT1);
    		gl.glClearColor(160/255f, 160/255f, 1f, 0f);
    	} else {
    		gl.glEnable(GL2.GL_LIGHT1);
    		gl.glClearColor(0f, 51/255f, 102/255f,0f);
    	}
    	Lighting.setLight0Pos(gl, myTerrain);
    	Lighting.setLight1Pos(gl,x,y,z,angle);
    	
    	
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    	//Drawing all the stuff needed
    	Draw.drawField(gl, myTerrain, texture[4]);
    	if(!myCamera.getNight()) Draw.drawBackground(gl, myTerrain, texture[5]);
    	else Draw.drawBackground(gl, myTerrain, texture[6]);
    	Draw.drawAvatar(gl, texture[7],x,y,z,angle, idle);
    	Draw.drawTrees(gl, myTerrain, texture[1], texture[2]);
    	Draw.drawTerrain2(gl, myTerrain, texture[0]);
    	Draw.drawRoad(gl, myTerrain, texture[3]);
    }

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glEnable(GL2.GL_BLEND); 
    	gl.glClearColor(204/255f, 1f, 1f, 0f); //Background similar to the sky color(for antialiasing masking)
    	gl.glDisable(GL2.GL_BLEND); 
    	
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	
    	gl.glEnable(GL2.GL_LIGHTING);
    	gl.glEnable(GL2.GL_LIGHT0);
    	float globAmb[] = {0.05f, 0.05f, 0.05f, 1.0f};
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint.
    	
    	gl.glEnable(GL2.GL_NORMALIZE);
    	   	
    	gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        
     // Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);
        
    	//Adding texture
        for(int index = 0 ; index < texture.length ; index++) {
        	texture[index] = new Texture(gl,texFolder+"/"+texFilename[index],texExtension[index],texColor[index],mip[index]);
        }
        
        
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();      
		double aspect = 1.0 * width / height; //to prevent scretching
		GLU glu = new GLU();
		glu.gluPerspective(60.0, aspect, 0.01, 400.0);
		
	}
	
	
	/**
	 * Textbox for button direction 
	 * @return
	 */
	public JTextArea readme() {
		JTextArea text = new JTextArea();
		Font font = new Font("Arial", Font.BOLD, 18);
		text.setFont(font);
		text.setDisabledTextColor(Color.BLACK);
		text.append("Command :\n");
		text.append("N -> toggle night mode\n");
		text.append("Up/Down -> Move forward/backward, hold to move faster\n");
		text.append("Left/Right -> Turn left/right");
		text.setEditable(false);   //Prevents editing  
		text.setEnabled(false);   //Stops cutting and pasting
		return text;
	}
}
