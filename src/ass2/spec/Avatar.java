package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class Avatar {
	private GLU glu = new GLU();
	private GLUquadric quadric = glu.gluNewQuadric();
	//variable for avatar animation
	private boolean idle = true;
	private double movingDuration = 0;
	private double accelerateTiming = 7;
	private double direction = 1;
	private double bodyRotate = 0;
	private double bodyRotateSpeed = 1;
	private double bodyRotateSpeedMax = 2;
	private double bodyRotateSpeedMin = 1;
	private double maxRotate = 15;
	private double leftLegRotate = 0;
	private double rightLegRotate = 0;
	private double legRotateSpeed = 1;
	private double legRotateSpeedMax = 2;
	private double legRotateSpeedMin = 1;
	private double legDirection = 1;
	private double legMaxRotate = 15;
	private double wingRotate = 0;
	private double wingRotateSpeed = 6;
	private double wingDirection = 1;
	private double wingMaxRotate = 90;
	
	/**
	 * Generating idle/moving avatar
	 * @param gl
	 * @param idle
	 */
	public void genAvatar(GL2 gl, boolean idle) {
        glu.gluQuadricTexture(quadric, true);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        this.idle = idle;
        if(movingDuration > accelerateTiming) {
        	bodyRotateSpeed = bodyRotateSpeedMax;
        	legRotateSpeed = legRotateSpeedMax;
        } else {
        	bodyRotateSpeed = bodyRotateSpeedMin;
        	legRotateSpeed = legRotateSpeedMin;
        }
        drawBody(gl);
		drawLeg(gl);
		drawWing(gl);
        glu.gluDeleteQuadric(quadric);
	}
	
	public void drawBody(GL2 gl) {
		gl.glPushMatrix();

		if(idle) {
			if(bodyRotate != 0) {
				if(bodyRotate > 0) {
					bodyRotate -= bodyRotateSpeed;
					direction = -1;
				} else {
					bodyRotate += bodyRotateSpeed;
					direction = 1;
				}
			}
			movingDuration = 0;
		} else {
			if(bodyRotate > maxRotate) direction = -1;
			else if(bodyRotate < -maxRotate) direction = 1;
			bodyRotate += direction * bodyRotateSpeed;
			movingDuration += 0.1;
		}
		gl.glRotated(bodyRotate,0,0,1);
        gl.glTranslated(0, 0.1, 0);
		gl.glScaled(1,1.5,1);
		glu.gluSphere(quadric, 0.09, 40, 40);
		gl.glPopMatrix();
	}
	
	public void drawLeg(GL2 gl) {
		gl.glPushMatrix();
		
			if(idle) {
				if(leftLegRotate > 0) {
					leftLegRotate -= legRotateSpeed;
				}
			} else if(bodyRotate > 0){
				if(leftLegRotate > legMaxRotate) legDirection = -1;
				else if(leftLegRotate < 0) legDirection = 1;
				leftLegRotate += legDirection * legRotateSpeed;
			}
			gl.glPushMatrix();
			gl.glRotated(leftLegRotate, 0, 0, 1);
			gl.glTranslated(0.05, 0.02, -0.05);
			gl.glScaled(1.25, 1, 2);
			glu.gluSphere(quadric, 0.02, 40, 40);
			gl.glPopMatrix();
		
			if(idle) {
				if(rightLegRotate > 0) {
					rightLegRotate -= legRotateSpeed;
				}
			} else if(bodyRotate < 0){
				if(rightLegRotate > legMaxRotate) legDirection = -1;
				else if(rightLegRotate < 0) legDirection = 1;
				rightLegRotate += legDirection * legRotateSpeed;
			}
			gl.glPushMatrix();
			gl.glRotated(-rightLegRotate, 0, 0, 1);
			gl.glTranslated(-0.05, 0.02, -0.05);
			gl.glScaled(1.4, 1, 2);
			glu.gluSphere(quadric, 0.02, 40, 40);
			gl.glPopMatrix();
			
		gl.glPopMatrix();
	}
	
	public void drawWing(GL2 gl) {
		gl.glPushMatrix();
		
			if(idle) {
				if(wingRotate > 0) {
					wingRotate -= wingRotateSpeed;
				}
			} else if(movingDuration > accelerateTiming){
				if(wingRotate < wingMaxRotate) {
					wingRotate += wingDirection * wingRotateSpeed;
				}
			}
			gl.glPushMatrix();
			gl.glRotated(bodyRotate,0,0,1);
			gl.glTranslated(0.095, 0.17, -0.03);
			gl.glRotated(wingRotate,1,0,0);
			gl.glTranslated(0, -0.04, 0);
			gl.glScaled(1, 3, 1);
			glu.gluSphere(quadric, 0.025, 40, 40);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glRotated(bodyRotate,0,0,1);
			gl.glTranslated(-0.095, 0.17, -0.03);
			gl.glRotated(wingRotate,1,0,0);
			gl.glTranslated(0, -0.04, 0);
			gl.glScaled(1, 3, 1);
			glu.gluSphere(quadric, 0.025, 40, 40);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
}
