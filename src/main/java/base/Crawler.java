package base;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Crawler {
	
	public static Queue<Element> kju = new LinkedList<>();
	public static Set<String> marked = new HashSet<>();
	public static String regex = "/wiki/(.+?)\"";
	public static int id;
	public static int temp_id;
	public static MongoConnect mongo;
	
	
	public static List<Element> unique = new ArrayList<Element>();
	
	public static void algoritam(String root, int n) 
	{
	
	Element start = new Element(root); //inicijalizacija roota
	start.setRbr(id); //pripadnost ostaje na -1 jer od root-a se krece
	id++; 
	unique.add(start);
	
	kju.add(start);
	while(!kju.isEmpty())
	{
		
		Element curr = kju.poll();
		String crawledURL = curr.getURL(); 				
		
		System.out.println("Kroluje se stranica: " + curr.getRbr()+ " izronjena iz " + Integer.toString(curr.getPripadnost()));

		
		
		String html = null;
		String naziv = null;
		String opis = null;
		
		
		String retval[];
		retval = base.Scrapper.daj_html(crawledURL);
		
		
		
		curr.setNaziv(retval[0]);
		curr.setOpis(retval[1]);
		
		

		html = retval[2];
		
		if(html==null)
		{
			curr.saveElement();
			continue;
		}
		
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		
		while(matcher.find())  // trazenje linkova iz elementa curr, dodavanje istih na kraj 
		{
			
			String w = matcher.group();
			String adresa = "https://sr.wikipedia.org" + w.substring(0, w.length()-1); //secenje " karaktera zbog nacina definisanja regex-a

			if(adresa.startsWith("https://sr.wikipedia.org/w/index.php?title") || adresa.startsWith("https://sr.wikipedia.org/wiki/en:"))
			{
				System.out.println("Naleteli smo na praznu stranu wikipedije.");
				continue;
			}
			marked.add(adresa);
			
			

			
			
			boolean klasican_inc_id = true;
			for(int i = 0;i<unique.size();i++)
			{
				if(adresa.equals(unique.get(i).getURL()))
				{
					temp_id = unique.get(i).getRbr();
					klasican_inc_id = false;
				}
			}
			
			Element novi = new Element(adresa);
			
			
			
			novi.setPripadnost(curr.getRbr());
			if(klasican_inc_id)
			{
				novi.setRbr(id);
				id++;				
				unique.add(novi);
				kju.add(novi);
			}
			else
			{
				novi.setRbr(temp_id);
				if(curr.exists_in_list(temp_id)) continue; //preskakanje duplikata unutar jedne stranice
							
			}
			
			
	
			
			curr.append(novi.getRbr());
						
		}
		
	
		
		curr.saveElement();
		
		
			
		
		if(curr.getRbr()>n)
		{
			System.out.println("Done. Crawled " + n + " pages.");
			return;
		}
	}

	
}
	
	
}
