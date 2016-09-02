package ass2.spec;

import javax.media.opengl.GL2;


public class Lighting {
	
	/**
	 * Setting the ambient and diffuse of light 0
	 * @param gl
	 */
	public static void setLight0(GL2 gl, boolean nightMode) {
		if(nightMode) {
	        float[] lowAmb = {0.1f,0.1f,0.1f,1.0f};
	        float[] lowDiff = {0.25f,0.25f,0.25f,1.0f};
	        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lowAmb,0);
	        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lowDiff,0);
		} else {
			float[] highAmb = {0.4f,0.4f,0.4f,1.0f};
	        float[] highDiff = {1.0f,1.0f,1.0f,1.0f};
	        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, highAmb,0);
	        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, highDiff,0);
		}
	}
	
	/**
	 * Setting the ambient ,diffuse, light angle, and exponent of light 1
	 * @param gl
	 */
	public static void setLight1(GL2 gl) {
		float[] amb = {0.0f,0.0f,0.0f,1.0f};
        float[] diff = {1.0f,1.0f,1.0f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, amb,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diff,0);
        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 60); 
    	gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 2);
	}
	
	/**
	 * Setting the position of light 0
	 * @param gl
	 */
	public static void setLight0Pos(GL2 gl, Terrain myTerrain) {
		gl.glPushMatrix();
		//Sun direction is changed according to the terrain dimension
		float[] sunlight = myTerrain.getSunlight();
		//Directional light which position is based on the sunlight variable
		//float[] light = {(sunlight[0] * (width - 1))/2, sunlight[1] * height, -(sunlight[2] * (width - 1))/2, 0.0f};
		float[] light = {sunlight[0], sunlight[1], sunlight[2], 0.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light, 0);
        gl.glPopMatrix();
	}
	
	/**
	 * Setting the position and facing angle of light 1(the torch)
	 * @param gl
	 */
	public static void setLight1Pos(GL2 gl, double x, double y, double z, double angle) {
		gl.glPushMatrix();
		gl.glTranslated(x, y, z);
		gl.glRotated(-angle,0,1,0);
		gl.glTranslated(0, 0.5, 0.5);
		//GLUT glut = new GLUT();
		//glut.glutSolidSphere(0.05, 8, 8);
		float[] light = {0.0f, 0.5f, 0.5f, 1.0f};
        float[] dir = {0.0f, -1.0f, 0.5f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light, 0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, dir,0); 
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint.
        gl.glPopMatrix();
	}
}
