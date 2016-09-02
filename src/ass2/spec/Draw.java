package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class Draw {
	private static Avatar myAvatar = new Avatar();
	private static int treeSlices = 32;
	private static double treeRadius = 0.125;
	private static double treeHeight = 0.75;
	private static double leafRadius = 0.4;
	private static double fieldBorder = 200;
	private static double bot = -3;
	private static double top = 47;
	private static double wave = 0;
	
	/**
	 * Draw the terrain
	 * @param gl
	 * @param myTerrain
	 * @param terrainTex
	 */
	public static void drawTerrain2(GL2 gl, Terrain myTerrain, Texture terrainTex) {
		gl.glPushMatrix();
        MathUtil math = new MathUtil();
		Dimension size = myTerrain.size();
		double x = size.getWidth();
		double z = size.getHeight();

    	gl.glFrontFace(GL2.GL_CW);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, terrainTex.getTextureId()); 
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, terrainTex.getColor(),0);
		for(double zA = 0; zA < z - 1; zA++) {
			for(double xA = 0; xA < x - 1; xA++) {
				//
				//(p1)+-----+(p2)
				//    | t1 /| 
				//    |  /  |
				//    |/ t2 |
				//(p4)+-----+(p3) 
				//
				double[] p1 = {xA, myTerrain.getGridAltitude((int)xA,(int)zA), zA};
				double[] p2 = {xA+1, myTerrain.getGridAltitude((int)xA+1,(int)zA), zA};
				double[] p3 = {xA+1, myTerrain.getGridAltitude((int)xA+1,(int)zA+1), (zA+1)};
				double[] p4 = {xA, myTerrain.getGridAltitude((int)xA,(int)zA+1), (zA+1)};
				
				//Drawing t1
				gl.glBegin(GL2.GL_TRIANGLES);
		        {
		        	gl.glNormal3dv(math.getNormal(p2,p1,p4),0);
		        	gl.glTexCoord2d(0.0, 0.0);gl.glVertex3dv(p1,0);
		        	gl.glTexCoord2d(1.0, 0.0);gl.glVertex3dv(p2,0);
		        	gl.glTexCoord2d(0.0, 1.0);gl.glVertex3dv(p4,0);
		        }
		        gl.glEnd();
		        //Drawing t2
		        gl.glBegin(GL2.GL_TRIANGLES);
		        {
		        	gl.glNormal3dv(math.getNormal(p3,p2,p4),0);
		        	gl.glTexCoord2d(1.0, 1.0);gl.glVertex3dv(p3,0);
		        	gl.glTexCoord2d(0.0, 1.0);gl.glVertex3dv(p4,0);
		        	gl.glTexCoord2d(1.0, 0.0);gl.glVertex3dv(p2,0);
		        }
		        gl.glEnd();
			}
		}
    	gl.glFrontFace(GL2.GL_CCW);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glPopMatrix();
	}
		
		/**
		 * Draw Tree
		 */
		public static void drawTrees(GL2 gl, Terrain myTerrain, Texture logTex, Texture leafTex) {			
			GLU glu = new GLU();
			GLUquadric quadric = glu.gluNewQuadric();
            glu.gluQuadricTexture(quadric, true);
            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
			List<Tree> trees = myTerrain.trees();
			
			for(Tree tree : trees) {
				double[] position = tree.getPosition();
				double x = position[0];
				double y = position[1];
				double z = position[2];
				//Drawing the log
				gl.glPushMatrix();
				gl.glBindTexture(GL2.GL_TEXTURE_2D, logTex.getTextureId()); 
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, logTex.getColor(),0);
				gl.glTranslated(x, y, z);
				gl.glRotated(-90,1,0,0);
				glu.gluCylinder(quadric, treeRadius, treeRadius, treeHeight, treeSlices, treeSlices);
		        gl.glPopMatrix();
		        //Drawing the leaf
		        gl.glPushMatrix();
		        gl.glBindTexture(GL2.GL_TEXTURE_2D, leafTex.getTextureId()); 
		        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, leafTex.getColor(),0);
				gl.glTranslated(x, y+treeHeight, z);
	            glu.gluSphere(quadric, leafRadius, 40, 40);
				gl.glPopMatrix();
			}
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			glu.gluDeleteQuadric(quadric);
		}
		
		/**
		 * Drawing the road
		 * @param gl
		 * @param myTerrain
		 * @param roadTex
		 */
		public static void drawRoad(GL2 gl, Terrain myTerrain, Texture roadTex) {
			gl.glPushMatrix();
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, roadTex.getColor(),0);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, roadTex.getTextureId()); 
			gl.glFrontFace(GL2.GL_CW);
			List<Road> roads = myTerrain.roads();
			for(Road road : roads) {
				List<Double> pointsA = new ArrayList<Double>();
				List<Double> pointsB = new ArrayList<Double>();
				double width = road.width() / 2;
				double distance = 0.05; //distance per segment
				double epsilon = 0.01;
				//Pushing all the normal point to 2 lists based on tangent
				for(double t=0; t<road.size(); t+=distance){ 
		    		double curX = road.point(t)[0];
		    		double curY	= road.point(t)[1];
		    		double nextX, nextY;
		    		if(Math.abs(t - road.size() + distance) > epsilon) {
			    		nextX = road.point(t + distance)[0];
			    		nextY = road.point(t + distance)[1];
		    		} else {
		    			nextX = road.controlPoint(road.size()*3)[0];
		    			nextY = road.controlPoint(road.size()*3)[1];
		    		}
		    		double aX, aY, bX, bY;
		    		if(Math.abs(nextY - curY) > epsilon) {
		    			double angle = Math.atan(-(nextX - curX) / (nextY - curY));
			    		if(nextY - curY > 0)angle = angle + Math.PI;
		    			aX = curX + width*Math.cos(angle);
		        		aY = curY + width*Math.sin(angle);
		        		bX = curX - width*Math.cos(angle);
		        		bY = curY - width*Math.sin(angle);
		    		} else {
		    			aX = curX;
		    			bX = curX;
		    			if(nextX > curX) {
			    			aY = curY + width;
			    			bY = curY - width;
		    			} else {
		    				aY = curY - width;
			    			bY = curY + width;
		    			}
		    		}
					pointsA.add(aX);
					pointsA.add(aY);
					pointsB.add(bX);
					pointsB.add(bY);
					//Add the last point, normal is calculated from previous point instead of next point
					if(Math.abs(t - road.size() + distance) < epsilon) {
						pointsA.add(aX - curX + nextX);
						pointsA.add(aY - curY + nextY);
						pointsB.add(bX - curX + nextX);
						pointsB.add(bY - curY + nextY);
		    		}
		        }
				//Start drawing the road
				gl.glBegin(GL2.GL_TRIANGLE_STRIP);{
					double xA, zA, xB, zB;
					double height = myTerrain.getGridAltitude((int)Math.floor(road.controlPoint(0)[0]), (int)Math.floor(road.controlPoint(0)[1]));	
			    	for(int index = 0; index < pointsA.size(); index+=2) {
			    		xA = pointsA.get(index);
			    		zA = pointsA.get(index+1);
			    		xB = pointsB.get(index);
			    		zB = pointsB.get(index+1);
			    		gl.glTexCoord2d(1.0, index / pointsA.size());gl.glVertex3d(xA,height+0.01,zA);
			    		gl.glTexCoord2d(0, index / pointsA.size());gl.glVertex3d(xB,height+0.01,zB);
			    	}
				}gl.glEnd();
			}
			gl.glFrontFace(GL2.GL_CCW);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			gl.glPopMatrix();
		}
		
		/**
		 * Drawing muh avatar
		 * @param gl
		 * @param avaTex
		 * @param x
		 * @param y
		 * @param z
		 * @param angle
		 */
		public static void drawAvatar(GL2 gl, Texture avaTex, double x, double y, double z, double angle, boolean idle) { 
            gl.glPushMatrix();
			
            gl.glDisable(GL2.GL_CULL_FACE);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE,avaTex.getColor(),0);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, avaTex.getTextureId());
            gl.glTranslated(x, y, z);
            gl.glRotated(-angle, 0, 1, 0);
            myAvatar.genAvatar(gl, idle); //generate avatar method in other class
            gl.glEnable(GL2.GL_CULL_FACE);
            gl.glPopMatrix();
		}
		
		/**
		 * Drawing the field(sea)
		 * @param gl
		 * @param myTerrain
		 * @param fieldTex
		 */
		public static void drawField(GL2 gl, Terrain myTerrain, Texture fieldTex) {
			gl.glPushMatrix();
			Dimension size = myTerrain.size();
			double x = size.getWidth();
			double z = size.getHeight();
			MathUtil math = new MathUtil();
			gl.glDisable(GL2.GL_DEPTH_TEST);
			// prefiltering
			gl.glEnable(GL2.GL_POLYGON_SMOOTH);
			gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT,GL2.GL_NICEST);
			// also requires alpha blending
			gl.glEnable(GL2.GL_BLEND); 
			gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
			
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, fieldTex.getColor(),0);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, fieldTex.getTextureId());
			double[][] points = 
				{{x+fieldBorder, bot,z+fieldBorder},
				{x+fieldBorder, bot,-fieldBorder},
				{-fieldBorder, bot,-fieldBorder},
				{-fieldBorder, bot,z+fieldBorder}};
			gl.glBegin(GL2.GL_POLYGON);
	        {
	        	gl.glNormal3dv(math.getNormal(points[0],points[1],points[2]),0);
	        	gl.glTexCoord2d(0.0, 10.0+wave);gl.glVertex3dv(points[0],0);
	        	gl.glTexCoord2d(0.0, 0.0+wave);gl.glVertex3dv(points[1],0);
	        	gl.glTexCoord2d(10.0, 0.0+wave);gl.glVertex3dv(points[2],0);
	        	gl.glTexCoord2d(10.0, 10.0+wave);gl.glVertex3dv(points[3],0);
	        }
	        gl.glEnd();
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			gl.glDisable(GL2.GL_POLYGON_SMOOTH);
			gl.glDisable(GL2.GL_BLEND);
			gl.glEnable (GL2.GL_DEPTH_TEST);
			gl.glPopMatrix();
			//For wave animation
			if(wave > 10) wave = 0;
			wave += 0.001;
		}
		
		/**
		 * Drawing skybox background
		 * @param gl
		 * @param myTerrain
		 * @param skyTex
		 */
		public static void drawBackground(GL2 gl, Terrain myTerrain, Texture skyTex) {
			gl.glPushMatrix();
			Dimension size = myTerrain.size();
			double x = size.getWidth();
			double z = size.getHeight();
			gl.glDisable(GL2.GL_DEPTH_TEST);
			// prefiltering
			gl.glEnable(GL2.GL_POLYGON_SMOOTH);
			gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT,GL2.GL_NICEST);
			// also requires alpha blending
			gl.glEnable(GL2.GL_BLEND); 
			gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE,skyTex.getColor(),0);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, skyTex.getTextureId());
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE ); 
			bot -= 1;
			double[][][] points = 
				{
				{{x+fieldBorder,bot,z+fieldBorder},
				{x+fieldBorder,bot,-fieldBorder},
				{x+fieldBorder,top,-fieldBorder},
				{x+fieldBorder,top,z+fieldBorder}},
				
				{{x+fieldBorder,bot,-fieldBorder},
				{-fieldBorder,bot,-fieldBorder},
				{-fieldBorder,top,-fieldBorder},
				{x+fieldBorder,top,-fieldBorder}},
				
				{{-fieldBorder,bot,-fieldBorder},
				{-fieldBorder,bot,z+fieldBorder},
				{-fieldBorder,top,z+fieldBorder},
				{-fieldBorder,top,-fieldBorder}},
				
				{{-fieldBorder,bot,z+fieldBorder},
				{x+fieldBorder,bot,z+fieldBorder},
				{x+fieldBorder,top,z+fieldBorder},
				{-fieldBorder,top,z+fieldBorder}}};
			gl.glDisable(GL2.GL_LIGHTING);
			gl.glFrontFace(GL2.GL_CW);
			for(int i = 0; i < 4; i++) {
				double a = 0;
				double b = 0;
				if(i%2 == 1) a = 1;
				else b = 1;
				gl.glBegin(GL2.GL_QUADS);
		        {
		        	gl.glTexCoord2d(a, 0.0);gl.glVertex3dv(points[i][0],0);
		        	gl.glTexCoord2d(b, 0.0);gl.glVertex3dv(points[i][1],0);
		        	gl.glTexCoord2d(b, 1.0);gl.glVertex3dv(points[i][2],0);
		        	gl.glTexCoord2d(a, 1.0);gl.glVertex3dv(points[i][3],0);
		        }
		        gl.glEnd();
			}
			bot+=1;
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glFrontFace(GL2.GL_CCW);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			gl.glDisable(GL2.GL_POLYGON_SMOOTH);
			gl.glDisable(GL2.GL_BLEND);
			gl.glEnable (GL2.GL_DEPTH_TEST);
			gl.glPopMatrix();
		}
}
