package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;




public class SearchQuery {
	private DBCollection coll;
	private String typed;
	
	private List<SortedPageRank> pageRankSorted;
	
	
	
	public SearchQuery(String typed, DBCollection coll, List<SortedPageRank> rank) {
		setTyped(typed);
		setCollection(coll);
		setPageRankSortedList(rank);
		
		naslovQuery();
		
	}
	
	public DBObject formQuery(String typed) {
		DBObject query = new BasicDBObject();
	    Scanner s = new Scanner(typed);
		String[] typedS = new String[10];
		DBObject[] regex = new DBObject[10];
		DBObject[] naslov = new DBObject[10];
		DBObject[] opis = new DBObject[10];
		
		BasicDBList or = new BasicDBList();
	    int	k = 0;
	    while(s.hasNext() && k < 10) {
	    	 typedS[k] = s.next();
			 regex[k] = new BasicDBObject("$regex", typedS[k]);
			 naslov[k] = new BasicDBObject("naslov", regex[k]);
			 //opis[k] = new BasicDBObject("opis", regex[k]);
			 
			 or.add(naslov[k]);
			 //or.add(opis[k]);
			 k++;
		}
	    
	    s.close();
		query = new BasicDBObject("$or", or);
		
		return query;
	}
	
	
	public DBCursor naslovQuery() {
		
		System.out.println("Eneted keywords: " + typed);
		System.out.println("СрчЕнџин: Searching " + base.PageRank.n + " pages.");
		
		
		DBObject query = formQuery(typed);
	    DBObject fields = new BasicDBObject("naslov", true).append("_id", false).append("rbr", true).append("opis", true).append("URL", true);
	    DBCursor result = coll.find(query, fields);
	    int queryCount = result.size();
	    int k = 0;
	    int kmax = 20;
	    System.out.println("Pages found: " + queryCount);
	    
	    if(queryCount > 20) 
		    System.out.println("Printing " +kmax+"/" + queryCount +" pages.");
	    
	    while (result.hasNext()) {
			 //System.out.println(result.next());
			 int i = (int) result.next().get("rbr");
			 String naslov_t = (String) result.curr().get("naslov");
			 String opis_t = (String) result.curr().get("opis");
			 String URL_t = (String) result.curr().get("URL");
			 
			 for (int j = 0; j < pageRankSorted.size(); j++) {
				 if (i == pageRankSorted.get(j).getRbr()) {
					 k++;
					 pageRankSorted.get(i).setNaziv(naslov_t);
					 pageRankSorted.get(i).setOpis(opis_t);
					 pageRankSorted.get(i).setURL(URL_t);
					 
					 if (k<kmax) 
						 printQuery(i);
					 
				 }
				 
			 }
			
	    }
		return result;
		
	}
	
	public void setCollection(DBCollection coll) {
		this.coll = coll;
	}
	
	public void setTyped(String typed) {
		this.typed = typed;
	}
	
	public void setPageRankSortedList(List<SortedPageRank> pageRankSorted) {
		this.pageRankSorted = pageRankSorted;
		
	}
	
	
	public void printQuery(int i) {

		 System.out.println("Naslov: " + pageRankSorted.get(i).getNaziv());
		 System.out.println("Opis: " + pageRankSorted.get(i).getOpis());
		 System.out.println("URL: " + pageRankSorted.get(i).getURL());
		 System.out.println("PageRank = " + pageRankSorted.get(i).getValue());
		 System.out.println("\n");
		 
		
	}
	


}
