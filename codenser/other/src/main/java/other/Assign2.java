
// Your name: Braden Meyerhoefer

import java.util.Scanner;

public class Assign2 {

    // A debug variable - set it to true when debugging so
    // you can print out stuff to screen during execution.
    public static boolean debug = true;
    
    // This method takes in a landscape (and its size M), and
    // a parameter called "fillDensity" and creates a landscape of
    // trees, by randomly generating trees in various cells.
    // IMPORTANT: DO NOT MODIFY THIS CODE.
    
    public static void createRandomForest (int[][] landscapeData, 
					   int M, 
					   double fillDensity,
					   double type1Frac)
    {
	// Check fillDensity validity:
	if ( (fillDensity <= 0) || (fillDensity > 1) ) {
	    System.out.println ("Assign1.createRandomForest(): fillDensity out of range: "
				+ fillDensity);
	}
	
	// Initially, clear the landscape:
	for (int i=0; i<M; i++) {
	    for (int j=0; j<M; j++)
		landscapeData[i][j] = 0;
	}
	
	// Now fill in trees randomly:
	for (int i=0; i<M; i++) {
	    for (int j=0; j<M; j++)
		if (UniformRandom.uniform () < fillDensity) {
		    if (UniformRandom.uniform() < type1Frac)
			landscapeData[i][j] = 1;  // Type-1 tree.
		    else
			landscapeData[i][j] = 2;  // Type-2 tree.
		}
	}
	
    }
    

    // Process a given landscape.
    // DO NOT CHANGE ANY CODE IN THIS METHOD. YOU CAN
    // ADD CODE TO IT IF YOU WISH.
    
    public static void processLandscapeData (int[][] landscapeData, 
					     int M, 
					     int numTreesToIgnite,
					     int[] igniteX, 
					     int[] igniteY)
    {
	Landscape L = new Landscape (landscapeData, M, numTreesToIgnite, igniteX, igniteY);
    }
    
    
    
    public static void main (String[] argv) 
    {

	//////////////////////////////////////////////////////////
	// INSERT YOUR TEST CODE HERE:
	// Example of creating test code:

	Scanner scnr = new Scanner(System.in);
	System.out.println("Input size of grid: ");
	int M = scnr.nextInt();
	int[][] landscapeData = new int [M][];
	for (int i=0; i<M; i++) {
	    landscapeData[i] = new int[M];
	    for (int j=0; j<M; j++) 
		landscapeData[i][j] = 0;
	}
	// Create a random landscape with fillDensity=0.7, type1Frac=0.4:
	createRandomForest (landscapeData, M, 0.7, 0.4);
	// Burn pattern:
	int numTreesToIgnite = 3;
	int[] igniteX = new int [3];
	int[] igniteY = new int [3];
	igniteX[0]=0;   igniteY[0]=1;
	igniteX[1]=1;   igniteY[1]=3;
	igniteX[2]=4;   igniteY[2]=2;
	// Simulate:
	processLandscapeData (landscapeData, M, numTreesToIgnite, igniteX, igniteY);

	// Feel free to add as many methods as you need.
	//////////////////////////////////////////////////////////
    }
  
}
