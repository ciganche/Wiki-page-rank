package base;




import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import utils.SaveOpen;

public class PageRank  implements Serializable{
	
	private static float H[][];
	private static float A[][];
	private static float I[][];
	private static float p;
	private static float pocetniUslovi[];
	private static float b[];
	private static float x[];
	final private static float d = (float) 0.85;
	private List<SortedPageRank> pageRank = new ArrayList<SortedPageRank>();
	
	public static int n; //ideja je bila n = cursor.count(); ali outOfMemoryExeption
	
	public PageRank(DBCollection coll, String loc, int no) {
		n = no;
		makeHMatrix(coll);
		 
		 saveHMatrix(loc + "H");
		// H = openHMatrix("D:\\mongodb\\H.ha");
		
		checkCollSum(H);
		 
		
		makeOtherMatrixs();
	    System.out.println("Is matrix a diagonally dominant: " + isDiagonallyDominant(A));
		
		
		 
		x = methods.Jakobi.racunaj(A, b, pocetniUslovi, 1000, (float)0.000001);
		
		
		for (int i = 0; i < x.length; i++) {
		    pageRank.add(new SortedPageRank(i, (int)(100000000*x[i])));
		}
		
		Collections.sort(pageRank);
		Collections.reverse(pageRank);
				
		saveSortedPageRank(pageRank, loc + "pageRank");
		
	
		
	}
	
	
	
	public static float[][] openHMatrix(String loc) {
		System.out.println("Opening H matrix from following location: " + loc);
		SaveOpen s = new SaveOpen();
		return s.openMatrix(loc); 
	}
	
	public static void saveHMatrix(String loc) {
		System.out.println("Saving H matrix to following location: " + loc + ".ha");
		SaveOpen s = new SaveOpen();
		s.saveMatrix(H, loc);
	}
	
	public static List<SortedPageRank> openSortedPageRank(String loc) {
		System.out.println("Opening PageRank object from following location: " + loc);
		SaveOpen s = new SaveOpen();
		return s.openSortedPageRank(loc); 
	}
	
	public static void saveSortedPageRank(List<SortedPageRank> o, String loc) {
		System.out.println("Saving PageRank object from following location: " + loc + ".pr");
		SaveOpen s = new SaveOpen();
		s.saveSortedPageRank(o, loc);
	}
	
	
	public void makeHMatrix(DBCollection coll) {
		DBCursor cursor;
		
		
		System.out.println("N[CursorCount] = " + n);
		
		
		//float matrix[][] = new float[n][n];
		H = new float[n][n];
		
		
		BasicDBObject keys = new BasicDBObject();
		
		keys.put("vodiNa", true);
		keys.put("_id", false);
		final DBCursor usersCursor = coll.find(new BasicDBObject(), keys);
		
		
		
		
		long start = System.currentTimeMillis();
		for (int i =0; i<n; i++) {
			
			ArrayList<Integer> vodiNaList = (ArrayList<Integer>) usersCursor.next().get("vodiNa");
			ArrayList<Integer> list = new ArrayList<>();
			
			
			
			for (int j = 0; j < n; j++) {
				
				if (vodiNaList.contains(j)) {
					
					H[i][j] = 1;
					list.add(j);
				}
				
			}
			if (i % 1000 ==0 )
				System.out.println("C LOOP: i: "+ i);
		}
		System.out.println("C done.");
		
		int sum;
		for (int i = 0; i < n; i++) {
			sum = 0;
			for (int j = 0; j < n; j++) {
				sum += H[j][i];
				//System.out.println("H Loop 1 : i: "+ i + " \\ j: " + j);
			}
			for (int j = 0; j < n; j++) {
				if (H[j][i] == 1)
					H[j][i] = (float)1.0/sum;
				//System.out.println("H Loop 2 : i: "+ i + " \\ j: " + j);
			}
			if (i % 1000 ==0 )
				System.out.println("H LOOP: i: "+ i);
		}
		System.out.println("H done.");
		

		long end = System.currentTimeMillis();
		System.out.println("Time consumed for H matrix of " + n + " dimension: " + (end-start) + " ms.");
		//setH(matrix);
		
		
		
		//System.out.println(Arrays.deepToString(matrix));
	}
		
	
	public void makeOtherMatrixs(){
		p = (float)1.00000/ n;
		pocetniUslovi = new float[n];
		b = new float[n];
		I = new float[n][n];
		A = new float[n][n];
		System.out.println("Making A and b matrixs...");
		long start = System.currentTimeMillis();
		for(int i = 0; i< n ;i++) {
			
			pocetniUslovi[i] = 0;
			b[i] = (1 - d)*p;
			for(int j = 0; j< n;j++)
			{
				if(i==j) I[i][j] = 1;
				else I[i][j] = 0;
				
				A[i][j] = I[i][j] - d*H[i][j];
				//System.out.println("A["+ i + "][" + j + "] = " + A[i][j]);
			}
	
		}
		long end = System.currentTimeMillis();
		System.out.println("DONE with makeOtherMatrixs();");
		System.out.println("Time consumed makeOtherMatrixs: " + (end-start) + " ms.");
	}
	
		
	 public static void print2D(float H[][]){
	       
	        for (float[] row : H)
	            System.out.println(Arrays.toString(row));
	 }
	 
	 public void setH(float mat[][]) {
		 this.H = mat;
	 }
	 
	 public float[][] getH(){
		 
		 return this.H;
	 }
	
	 public void initI(){
		
			
	 }
		
	 
	 
	 public static void checkCollSum(float H[][]) {
		 int flag = 0;
		 System.out.println("Checking column sums..." );
		 for (int i = 0; i < H.length; i++) {
				float sum = 0;
				for (int j = 0; j < H.length; j++) {
					sum += H[j][i];
					
				}
				//System.out.println("Sum [" + i + "] = " + sum );
				if ( sum < 0.99992 || sum > 1.00001) {
					System.out.println("Irregular sum of [" + i + "] column!" );
					flag = 1;
				}
					
		}
		 
		 if (flag == 0)
			 System.out.println("Sum of H matrix columns is just fine!");
		 else 
			 System.err.println("Sum of H matrix columns is corrupted! H matrix not well built. Again prick.");
	 }
	 
	 
	 public static void printPageRankObject(List<SortedPageRank> o) {
		 for (SortedPageRank element : o) {
			    System.out.println("Value: " + element.value + " --- RBR: " + element.rbr);
			}
		 
	 }
	 
	 public static void printAll(DBCollection coll) {
			DBCursor cursor =  coll.find();
			try {
				while(cursor.hasNext()) 
			       System.out.println(cursor.next());
			    
			} finally {
			   
			}
			
	}
	 
	 public List<SortedPageRank> getPageRank(){
		 
		 return pageRank;
	 }
	 
	 public boolean isDiagonallyDominant(float[][] array) {
		    int otherTotal = 0;

		    // Loop through every row in the array
		    for(int row = 0; row < array.length; row++) {
		        otherTotal = 0;

		        // Loop through every element in the row
		        for(int column = 0; column < array[row].length; column++) {

		            // If this element is NOT on the diagonal
		            if(column != row) {

		                // Add it to the running total
		                otherTotal += Math.abs(array[row][column]);
		            }
		        }

		        // If this diagonal element is LESS than the sum of the other ones...
		        if(Math.abs(array[row][row]) < otherTotal) {

		            // then the array isn't diagonally dominant and we can return.
		            return false;
		        }
		    }
		    return true;
		}

}
