package ass2.spec;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class Camera implements GLEventListener, KeyListener {
	private Terrain myTerrain;
	private double maxX;
	private double maxZ;
	private double angle;
	private double x;
	private double z;
	private double duration = 0;
	private double border = 0.15; 
	private double movement = 0.5;
	private double velocity = 0.02;
	private double originalVelocity = 0.02;
	private double maxVelocity = 0.05;
	private double moveLeft;
	private double maxMoveLeft = 1;
	private int direction; //0 is idle,1 is move forward, -1 is move backward
	private double changeAngle = 20;
	private double angleVelocity = 5;
	private double angleLeft;
	private double maxAngleLeft = 10;
	private int angleDirection; //0 is idle,1 is turn right, -1 turn left
	private boolean nightMode = false;
	
	public Camera(Terrain terrain) {
		myTerrain = terrain;
		Dimension size = terrain.size();
		maxX = size.getWidth() - 1;
		maxZ = size.getHeight() - 1;
		angle = 0;
		x = border;
		z = border;
		moveLeft = 0;
		direction = 0;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public double getX() {
		return x;
	}
	
	public double getZ() {
		return z;
	}
	
	public double getY() {
		return myTerrain.altitude(x,z);
	}
	
	public double getVelocity() {
		return velocity;
	}
	
	public double getMoveLeft() {
		return moveLeft;
	}
	
	/**
	 * check if avatar has to move
	 * @return
	 */
	public boolean getIdle() {
		if(direction == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setMoveLeft(double moveLeft) {
		this.moveLeft = moveLeft;
	}
	
	public void changeAngle() {
		if(angleDirection != 0) {
			if(angleLeft >= angleVelocity) {
				angleLeft -= angleVelocity;
				angle = angle + angleVelocity * angleDirection;
				angle = angle % 360;
			} else if(angleLeft < changeAngle && angleLeft > 0) {
				angle = angle + angleLeft * angleDirection;
				angle = angle % 360;
				angleLeft = 0;
			} else {
				angleDirection = 0;
			}
		}
	}
	
	public boolean idleAngle() {
		if(angleDirection == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void moved() {
		if(direction != 0) {
			if(duration > 9) velocity = maxVelocity;
			if(moveLeft >= velocity) {
				moveLeft -= velocity;
				x = x - Math.sin(Math.toRadians(angle)) * velocity * direction;
				z = z + Math.cos(Math.toRadians(angle)) * velocity * direction;
				moveLimit();
			} else if(moveLeft < velocity && moveLeft > 0) {
				x = x - Math.sin(Math.toRadians(angle)) * moveLeft * direction;
				z = z + Math.cos(Math.toRadians(angle)) * moveLeft * direction;
				moveLimit();
				moveLeft = 0;
			} else {
				direction = 0;
			}
			duration += 0.1;
		} else {
			duration = 0;
			velocity = originalVelocity;
		}
	}
	
	public boolean getNight() {
		return nightMode;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		  
		 case KeyEvent.VK_RIGHT:
			 if(moveLeft == 0) {
				 if(angleDirection != 1) {
					angleLeft = changeAngle; 
				 	angleDirection = 1;
				 } else {
					if(angleLeft+changeAngle < maxAngleLeft) {
						angleLeft += changeAngle;
					} else {
						angleLeft = maxAngleLeft;
					}
				 }
			 }
			break;
		 case KeyEvent.VK_LEFT:
			 if(moveLeft == 0) {
				 if(angleDirection != -1) {
					angleLeft = changeAngle; 
				 	angleDirection = -1;
				 } else {
					if(angleLeft+changeAngle < maxAngleLeft) {
						angleLeft += changeAngle;
					} else {
						angleLeft = maxAngleLeft;
					}
				 }
			 }
			break;
				  
		 case KeyEvent.VK_UP:
			 if(direction != 1) {
				moveLeft = movement; 
			 	direction = 1;
			 } else {
				if(moveLeft+movement < maxMoveLeft) {
					moveLeft += movement;
				} else {
					moveLeft = maxMoveLeft;
				}
			 }
			 break;
		 case KeyEvent.VK_DOWN:
			 if(direction != -1) {
					moveLeft = movement; 
				 	direction = -1;
			 } else { 
				 if(moveLeft+movement < maxMoveLeft) {
					moveLeft += movement;
				} else {
					moveLeft = maxMoveLeft;
				}
			 } 
			 break;
		 case KeyEvent.VK_N:
			 if(!nightMode) nightMode = true;
			 else nightMode = false;
			 break;
		 default:
			 break;
		 }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	public void moveLimit() {
		if(x < border) {
			x = border;
		} else if(x > maxX - border) {
			x = maxX - border;
		}
		if(z < border) {
			z = border;
		} else if(z > maxZ - border) {
			z = maxZ - border;
		}
	}

}
