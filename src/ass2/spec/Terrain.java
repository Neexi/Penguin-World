package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    /**
    public double altitude(double x, double z) {
        double yn = 0;
        double x1,x2,x3,z1,z2,z3;
        //Using bilinear interpolation
        //1st condition, pn is at the left or equal to the diagonal line
        // (p1)+----------+(p2)
        //     |         /
        //     |        /
        //     |       /
        //     |      /
        //(pn1)|(pn) /(pn2)
        //     |    /
        //     |   /
        //     |  /
        //     | /
        //     |/
        //  (p3)
        //2nd condition, pn is at the right or equal to the diagonal line
        //             (p3) 
        //               /|
        //              / |
        //             /  |
        //            /   |
        //           /    |
        //     (pn2)/(pn) |(pn1)
        //         /      |
        //        /       |
        //       /        |
        //      /         |
        // (p2)+----------+(p1)
        if(x - Math.floor(x) > 0 || z - Math.floor(z) > 0) { //both x and z are not integer, use bilinear interpolation
	        if(Math.floor(x) - x >= z - Math.floor(z)) { //at the left of or equal to diagonal
	        	x1 = Math.floor(x);
	        	z1 = Math.floor(z);
	        	if(x - Math.floor(x) > 0 || x == mySize.getWidth() - 1) {
	        		x2 = Math.ceil(x);
	        	} else {
	        		x2 = x + 1;
	        	}
	        	z2 = Math.floor(z);
	        	x3 = Math.floor(x);
	        	if(z - Math.floor(z) > 0 || z == mySize.getHeight() - 1) {
	        		z3 = Math.ceil(z);
	        	} else {
	        		z3 = z + 1;
	        	}
	        } else { //at the right of right of diagonal
	        	if(x - Math.floor(x) > 0 || x == mySize.getWidth() - 1) {
	        		x1 = Math.ceil(x);
	        	} else {
	        		x1 = x + 1;
	        	}
	        	if(z - Math.floor(z) > 0 || z == mySize.getHeight() - 1) {
	        		z1 = Math.ceil(z);
	        	} else {
	        		z1 = z + 1;
	        	}
	        	x2 = Math.floor(x);
	        	if(z - Math.floor(z) > 0 || z == mySize.getHeight() - 1) {
	        		z2 = Math.ceil(z);
	        	} else {
	        		z2 = z + 1;
	        	}
	        	if(x - Math.floor(x) > 0 || x == mySize.getWidth() - 1) {
	        		x3 = Math.ceil(x);
	        	} else {
	        		x3 = x + 1;
	        	}
	        	z3 = Math.floor(z);
	        }
	        //1st step
	        //Find the the yn1 of pn1, calculated from p1 and p3(z axis comparison)
	        System.out.println("x1,z1 is "+x1+","+z1+" x2,z2 is "+x2+","+z2+"x3,z3 is "+x3+","+z3);
	        double yn1,yn2;
	        if(z3 - z1 != 0) {
	        	yn1 = (z - z1)/(z3 - z1) * getGridAltitude((int)x3,(int)z3) + (z3 - z)/(z3 - z1) *  getGridAltitude((int)x1,(int)z1);
	        } else {
	        	yn1 = (getGridAltitude((int)x3,(int)z3) + getGridAltitude((int)x1,(int)z1)) / 2;
	        }
	        if(z3 - z2 != 0) {
	        	yn2 = (z - z2)/(z3 - z2) * getGridAltitude((int)x3,(int)z3) + (z3 - z)/(z3 - z2) *  getGridAltitude((int)x2,(int)z2);
	        } else {
	        	yn2 = (getGridAltitude((int)x3,(int)z3) + getGridAltitude((int)x2,(int)z2)) / 2;
	        }
	        if(x2 - x1 != 0) {
	        	yn = (x - x1)/(x2 - x1) * yn2 + (x2 - x)/(x2 - x1) * yn1;
	        } else {
	        	yn = (yn2 + yn1) / 2;
	        }
        } else { //if both x and z are integer, just use the getGridAltitude
        	yn = getGridAltitude((int)x,(int)z);
        }
        return yn;
    }
    **/
    
    public double altitude(double x, double z) {
        double yn = 0;
        double x1,x2,x3,x4,z1,z2,z3,z4;
        //Using bilinear interpolation
        // (p1)+----------+(p2)
        //     |         /|
        //     |        / |
        //     |       /  |
        //     |      /   |
        //(pn1)|(pn) /pn2)|
        //     |    /     |
        //     |   /      |
        //     |  /       |
        //     | /        |
        //     |/         |
        // (p4)+----------+(p3)
        if(x - Math.floor(x) > 0 || z - Math.floor(z) > 0) { //at the left of or equal to diagonal
        	x1 = Math.floor(x);
        	z1 = Math.floor(z);
        	if(x - Math.floor(x) > 0 || x == mySize.getWidth() - 1) {
        		x2 = Math.ceil(x);
        	} else {
        		x2 = x + 1;
        	}
        	z2 = Math.floor(z);
        	x4 = Math.floor(x);
        	if(z - Math.floor(z) > 0 || z == mySize.getHeight() - 1) {
        		z4 = Math.ceil(z);
        	} else {
        		z4 = z + 1;
        	}
        	if(x - Math.floor(x) > 0 || x == mySize.getWidth() - 1) {
        		x3 = Math.ceil(x);
        	} else {
        		x3 = x + 1;
        	}
        	if(z - Math.floor(z) > 0 || z == mySize.getHeight() - 1) {
        		z3 = Math.ceil(z);
        	} else {
        		z3 = z + 1;
        	}
	        //1st step
	        //Find the the yn1 of pn1, calculated from p1 and p3(z axis comparison)
	        //System.out.println("x1,z1 is "+x1+","+z1+" x2,z2 is "+x2+","+z2+"x3,z3 is "+x3+","+z3);
	        double yn1,yn2;
	        if(z4 - z1 != 0) {
	        	yn1 = (z - z1)/(z4 - z1) * getGridAltitude((int)x4,(int)z4) + (z4 - z)/(z4 - z1) *  getGridAltitude((int)x1,(int)z1);
	        } else {
	        	yn1 = (getGridAltitude((int)x4,(int)z4) + getGridAltitude((int)x1,(int)z1)) / 2;
	        }
	        if(z3 - z2 != 0) {
	        	yn2 = (z - z2)/(z3 - z2) * getGridAltitude((int)x3,(int)z3) + (z3 - z)/(z3 - z2) *  getGridAltitude((int)x2,(int)z2);
	        } else {
	        	yn2 = (getGridAltitude((int)x3,(int)z3) + getGridAltitude((int)x2,(int)z2)) / 2;
	        }
	        if(x2 - x1 != 0) {
	        	yn = (x - x1)/(x2 - x1) * yn2 + (x2 - x)/(x2 - x1) * yn1;
	        } else {
	        	yn = (yn2 + yn1) / 2;
	        }
        } else { //if both x and z are integer, just use the getGridAltitude
        	yn = getGridAltitude((int)x,(int)z);
        }
        return yn;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


}
