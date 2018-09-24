package methods;

import java.util.Arrays;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class Jakobi {

	 public static float[] racunaj(float[][] A, float[] b, float[] x0, int itMax, float errMax) {
		 int n = b.length;
		 float[] x = new float[n];
		 float err;
		 float sum;
		 
		 System.out.println("Jakobi starts.");
		 for (int it = 0; it < itMax; it++ ){
			 err = 0;
			 for (int i = 0; i < n; i++) {
				 sum = 0;
				 for (int j = 0; j < i-1; j++) 
					 sum = sum + A[i][j]*x0[j];
				 for (int j = i+1; j < n; j++)
					 sum = sum + A[i][j]*x0[j];
				 
				 x[i] = (b[i] - sum)/A[i][i];
				 
				 err = err + ((x[i]-x0[i])*(x[i]-x0[i]));
				 
			}
			System.out.println("Iteration["+it+"] --- err: " + err);
			for (int i = 0; i < n; i++)
				x0[i] = x[i];
			
			if (err < errMax*errMax)
				return x;
		}
		 
		 return x; 
	 }
	
}