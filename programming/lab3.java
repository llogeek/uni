package garmatnaya_z3v18;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
public class Garmatnaya_z3v18 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("Enter n: ");
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		in.close();
		if (n <= 2) {
			System.err.println("Invalid n value, requires n > 2. Can't form triangles.");
			System.exit(1);
		}
		Random rnd = new Random((new Date()).getTime());
		int [][] arr = new int[n][n];
		System.out.println("Source values: ");
		for( int i = 0; i < arr.length; i++ ) {
			 for( int j = 0; j < arr.length; j++ ) {
			 arr[i][j] = rnd.nextInt() % ( n + 1 );
			 if (i == j || i == n - j - 1) {
				 System.out.print("[" + arr[i][j] + "]" + "\t"); 
			  }
			 else {
				 System.out.print(arr[i][j] + "\t");
			 }
				 }
			 System.out.println();
	    }
		int[] MaxNums = new int[4];
		int max = -n;   
		for (int i = 0; i < arr.length/2; i++)        
			// upper triangle
	            for (int j = i + 1; j < arr.length - i - 1; j++)
	            	if (arr[i][j] > max) max = arr[i][j];
		MaxNums[0] = max;
		max = -n;
		   // left triangle
		   for (int i = 0; i < arr.length/2; i++)        
	            for (int j = i + 1 ; j < arr.length - i - 1; j++)
	            	if (arr[j][i] > max) max = arr[j][i];
		   MaxNums[1] = max;
			max = -n;
			// right triangle
		   for (int i = arr.length/2; i < arr.length; i++)        
	            for (int j = arr.length - i; j < i; j++)
	            	if (arr[j][i] > max) max = arr[j][i];
		   MaxNums[2] = max;
			max = -n;
			// lower triangle
		   for (int i = arr.length/2; i < arr.length; i++)        
	            for (int j = arr.length - i; j < i; j++)
	            	if (arr[i][j] > max) max = arr[i][j];
		   MaxNums[3] = max;
	/*  System.out.println("Max element of the upper triangle: " + MaxNums[0]);
	  System.out.println("Max element of the left triangle: " + MaxNums[1]);
	  System.out.println("Max element of the right triangle: " + MaxNums[2]);
	  System.out.println("Max element of the lower triangle: " + MaxNums[3]); */
	   System.out.println("Massive of max elements in order [upper, left, right, lower]: " + Arrays.toString(MaxNums));
	}

}
