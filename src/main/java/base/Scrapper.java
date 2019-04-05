package base;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.SaveTest;


public class Scrapper {
	
	public static String naziv;
	public static String opis;
	public static long id;
	public static String url;
	public static int niz[];

	public static String[] daj_html(String source) {
		
		String retval[] = {null,null,null};
		
		try {
			Document doc = Jsoup.connect(source).timeout(5*60*1000).get();
			Elements cont_temp = doc.select("div.mw-content-ltr");
			Elements head_temp = doc.select("h1.firstHeading");
			
			for(Element  pom:head_temp )
			{
				naziv = pom.text();				
			}
			retval[0] = naziv;
					

			
			for(Element  paragraf:cont_temp )
			{
				
				if(paragraf.getElementsByTag("p").text().length() == 0)
				{
					Elements pom = paragraf.getElementsByTag("div");
					
					for(Element nesto:pom)
					{
						
					if(nesto.getElementsByTag("p").text().length()==0)
						return retval;
							
					retval[1] = nesto.getElementsByTag("p").get(0).text();
					
					
					retval[2] = nesto.getElementsByTag("p").html();
					}
				
				}
					
				
				else
				{
				opis = paragraf.getElementsByTag("p").get(0).text();
				retval[1] = opis;
				retval[2] =  paragraf.getElementsByTag("p").html();
				}
				
			}
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return retval;
		
	}
	
	

}
