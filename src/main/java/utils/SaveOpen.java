package utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import base.SortedPageRank;

public class SaveOpen implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4710142960153185452L;

	public SaveOpen() {
		
		
	}
	
	public void saveMatrix(float[][] H, String filename) {
		
		try {
			

			FileOutputStream fos = new FileOutputStream(filename + ".ha");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(H);
			oos.close();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		System.out.println("Matrix saved at: " + filename + ".ha");
	}
	
	public float[][] openMatrix(String loc) {
		float[][] H = null;
		try {
	
			FileInputStream fis = new FileInputStream(loc);
			ObjectInputStream ois = new ObjectInputStream(fis);
			H = (float[][]) ois.readObject();
			ois.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		return H;

	}
	
	public void saveSortedPageRank(List<SortedPageRank> o, String filename) {
		
		try {
			

			FileOutputStream fos = new FileOutputStream(filename + ".pr");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.close();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		System.out.println("PageRank object saved as: " + filename + ".pr");
	}
	
	public List<SortedPageRank> openSortedPageRank(String loc) {
		List<SortedPageRank> o = null;
		try {
	
			FileInputStream fis = new FileInputStream(loc);
			ObjectInputStream ois = new ObjectInputStream(fis);
			o = (List<SortedPageRank>) ois.readObject();
			ois.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    System.out.println("Opened.");
		return o;

	}
	

}
