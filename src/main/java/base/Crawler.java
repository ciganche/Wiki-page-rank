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
	/*
	public static void main(String[] args) throws InterruptedException 
	{
			//utils.SaveTest.kreiraj();
			//mongo = new MongoConnect();
			
			
			//algoritam("https://sr.wikipedia.org/wiki/%D0%9D%D1%83%D0%BC%D0%B5%D1%80%D0%B8%D1%87%D0%BA%D0%B0_%D0%BC%D0%B0%D1%82%D0%B5%D0%BC%D0%B0%D1%82%D0%B8%D0%BA%D0%B0"); //ubaciti pocetnu adresu krolovanja
			//base.PageRank.makeHMatrix(mongo.coll);
			//double[][] H = base.PageRank.openHMatrix("D:\\mongodb\\matrix.ha");
			//base.PageRank.checkCollSum(H);
			
			
			PageRank pr = new PageRank(mongo.coll);
	}
	
	*/
	
	public static void algoritam(String root, int n) 
	{
	
	Element start = new Element(root); //inicijalizacija roota
	start.setRbr(id); //pripadnost ostaje na -1 jer od root-a se krece
	id++; 
	unique.add(start);
	
	kju.add(start);
	while(!kju.isEmpty())
	{
		
		/*
		
		boolean upisuj = true;
		
		
		// provera
		boolean spreman = false;
		while(!spreman)
		{
			Element curr = kju.poll();
			String crawledURL = curr.getURL(); 
			for(int i = 0;i<unique.size();i++)
			{
				if(crawledURL.equals(unique.get(i).getURL()))
				{
					curr.setRbr(unique.get(i).getRbr());
					upisuj = false; 	//ne upisuj, uzima se sledeci iz kju-a
					break;
				}
			}
			if(upisuj)
			{
				// dodela novog id-a
				//upis
				//ubacivanje u unique
				spreman = true;
			}
		}
		
		*/
		
		Element curr = kju.poll();
		String crawledURL = curr.getURL(); 				
		
		System.out.println("Kroluje se stranica: " + curr.getRbr()+ " izronjena iz " + Integer.toString(curr.getPripadnost()));

		
		
		String html = null;
		String naziv = null;
		String opis = null;
		
		/*	boolean kraj = false; //kraj trazenja prvog valjanog u kju 
		
		while(!kraj)
		{
			if(( html = base.Scrapper.daj_html(crawledURL))==null ) // preuzimanje html-a
			{
				System.out.println("! Ne valjda link : " + crawledURL);
				curr = kju.poll();
				crawledURL = curr.getURL(); 	
				kraj = false;
			}
			else
			{
				kraj = true; // :)
			}
		}
		*/
		
		String retval[];
		retval = base.Scrapper.daj_html(crawledURL);
		
		/*if (mongo.existsinDB(retval[0])) {
			System.out.println("ID: " + id);
			--id;
			continue;
		}*/
		
		
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
				//System.out.println("Dodat je stranica u listu krolovanja: "+ adresa);
			}
			else
			{
				novi.setRbr(temp_id);
				if(curr.exists_in_list(temp_id)) continue; //preskakanje duplikata unutar jedne stranice
				
				//System.out.println("Pojavio se vec poznat: "+novi.getRbr());
				
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
