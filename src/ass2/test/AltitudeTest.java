package ass2.test;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import ass2.spec.LevelIO;
import ass2.spec.Terrain;

public class AltitudeTest {
	private static Terrain terrain;
	private static double x;
	private static double z;
	private static final double EPSILON = 0.001;
	
	public static void main(String[] args) throws FileNotFoundException {
        terrain = LevelIO.load(new File(args[0]));
        Dimension size = terrain.size();
        x = size.getWidth();
        z = size.getHeight();
        integerPointTest();
        flatTerrainTest();
    }
	
	public static void integerPointTest() {
		boolean result = true;
		loop:
		for(double zA = 0; zA < z - 1; zA++) {
			for(double xA = 0; xA < x - 1; xA++) {
				if(Math.abs(terrain.getGridAltitude(0,0) - terrain.altitude(0.0,0.0)) > EPSILON) {
					result = false;
					break loop;
				}
			}
		}
		System.out.println("Integer point test is "+result);
	}
	
	public static void flatTerrainTest() throws FileNotFoundException {
		boolean result = true;
		Terrain terrain0 = LevelIO.load(new File("json/test0.json"));
		Dimension size = terrain0.size();
		double x0 = size.getWidth() - 1;
        double z0 = size.getHeight() - 1;
		double randomX, randomZ;
		loop:
		for(int index = 0; index < 100 ; index++) {
			randomX = Math.random()*x0;
			randomZ = Math.random()*z0;
			if(terrain0.altitude(randomX,randomZ) != 0) {
				result = false;
				break loop;
			}
		}
		System.out.println("Flat terrain test is "+result);
		
	}
}
