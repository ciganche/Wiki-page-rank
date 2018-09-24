package utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class SaveTest {
   
   
   private static PrintWriter writer = null;
	
	
    public SaveTest() {
    	
		try {
			writer = new PrintWriter("query.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
   
	
	

	
	
	public void upisi(String s) throws IOException
	{
		System.out.println(s);
		
		writer.write(s);
	}
	
   
   
   
}
