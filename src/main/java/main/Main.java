package main;

import java.io.IOException;
import java.util.Scanner;

import base.MongoConnect;
import base.PageRank;
import base.SearchQuery;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		
		System.out.println("Welcome to СрчЕнџин console app. ");
		
		Thread.sleep(1000);
		System.out.println("MongoDB setup: MongoDatabase will try to connect to localhost after entering database name and collection name. ");
		Thread.sleep(2000);
		System.out.println("Enter a database name: ");
		
		Scanner s = new Scanner(System.in);
		String dbName = s.next();
		System.out.println("Database name set to: " + dbName);
	
		System.out.println("Enter a database collection name: ");
		String collName = s.next();
		s = new Scanner(System.in);
		System.out.println("Collection name set to: " + collName);
		
		MongoConnect mongo = new MongoConnect(dbName, collName);
		
		
		System.out.println("Please select a working mode: ");
		System.out.println("\n");
		System.out.println("\t1) Making dynamic search engine [Entering starting Serbian Wiki page, crawling, calculating PageRank аnd using СрчЕнџин.] ");
		System.out.println("\t2) Calculating PageRank Value and using СрчЕнџин for existing Wiki instances in database");
		int choice = -1;
		int flag = 0;
		do {
			System.out.println("\n");
			System.out.println("Enter a number: ");
			s = new Scanner(System.in);
			choice = s.nextInt();
			
			if (choice == 1 || choice == 2 || choice == 0) {
				flag = 1;
			}
			
			
		} while (flag == 0);
		
		
		switch (choice) {
		case 0:
			System.out.println("Exiting СрчЕнџин... Хвала and goodbye.");
			return;

		case 1:
			System.out.println("Enter a starting Serbian Wiki web page for crawling: ");
			s = new Scanner(System.in);
			String urlRoot = s.next();
			flag = 1;
			do {
				System.out.println("Enter a max number of crawled pages[not recommended more than 8000]: ");
				
			} while (flag == 0);
			s = new Scanner(System.in);
			choice = s.nextInt();
			
			System.out.println("Crawling starts in 3 seconds... Sorry for very hysteric log.");
			Thread.sleep(3000);
			base.Crawler.algoritam(urlRoot, choice);
			System.out.println("Crawling finished.");
			System.out.println("\n");
			
			System.out.println("Please enter floder location for saving H matrix and PageRank list: [format: D:\\folder\\]");
			
			s = new Scanner(System.in);
			String loc = s.next();
			
			
			System.out.println("Starting PageRank calculations in 3 seconds... A little better log.");
			PageRank pr = new PageRank(mongo.coll, loc, choice);
			System.out.println("PageRank calculations done. Log above.");
			System.out.println("\n");
			
			System.out.println("Continuing to СрчЕнџин in 3 seconds...");
			
			System.out.println("\n");
			
			do {
				System.out.println("Enter a keyword[only Ћирилица]: [type \"0\" to exit the program.]");
				s = new Scanner(System.in, "UTF-8");
				String first = s.nextLine();
			
				
				
				System.out.println(first);
				if (first.contains("exit")) return;
				
				SearchQuery sq = new SearchQuery(first, mongo.coll, pr.getPageRank());
				
			} while(true);
			
			
		case 2:
			System.out.println("Trying to do calculations for existing files in specified database...");
			Thread.sleep(1000);
			int num = mongo.numberOfElementsinBase();
			if (num < 100) {
				System.out.println("Can't find more than 100 elements in database. Returning.");
				return;
			}
			System.out.println("\n");
			System.out.println("Found " + num + " elements in database.");
			System.out.println("\n");
			System.out.println("Please enter floder location for saving H matrix and PageRank list: [format: D:\\folder\\]");
			
			s = new Scanner(System.in);
			String l = s.next();
			
			
			
			PageRank p = new PageRank(mongo.coll, l, num);
			System.out.println("PageRank calculations done. Log above.");
			System.out.println("Continuing to СрчЕнџин in 3 seconds...");
			
			
			do {
				System.out.println("Enter a keyword[only Ћирилица]: [type \"0\" to exit the program.]");
				s = new Scanner(System.in, "UTF-8");
				String first = s.nextLine();
			
				
				
				
				if (first.contains("exit")) {
					System.out.println("Bye.");
					return;
				}
				
				SearchQuery sq = new SearchQuery(first, mongo.coll, p.getPageRank());
				
			} while(true);
			
			
			
		
				
		default:
			break;
		}
		
		
	
	}

}
