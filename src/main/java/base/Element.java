package base;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;

import utils.SaveTest;

public class Element {

	private int rbr;
	private String URL; 
	private int pripadnost; //na kom rbr stranice je otkrivena, init nikome 
	private List<Integer> pripada_mu; 
	private String naziv;
	private String opis;
	
	public Element(String url)
	{
		this.URL = url;
		this.rbr = -1;
		this.pripadnost = -1;
		this.pripada_mu = new ArrayList<Integer>();
	}
	
	
	
	
	
	
	public int getRbr() {
		return rbr;
	}
	
	
	public void setRbr(int rbr) {
		this.rbr = rbr;
	}
	
	
	
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	
	
	
	public List<Integer> getPripada_mu() {
		return pripada_mu;
	}
	public void setPripada_mu(List<Integer> pripada_mu) {
		this.pripada_mu = pripada_mu;
	}
	
	
	public int getPripadnost() {
		return pripadnost;
	}
	public void setPripadnost(int pripadnost) {
		this.pripadnost = pripadnost;
	}
	
	
	public void append(int l)
	{
		pripada_mu.add(l);
	}
	
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String n) {
		naziv = n;
	}
	
	
	public String getOpis() {
		return opis;
	}
	public void setOpis(String o) {
		opis = o;
	}
	
	
	public void saveElement()
	{
		//utils.SaveTest.upisi(int.toString(rbr)+ ": ");
		//utils.SaveTest.upisi(naziv);
		//utils.SaveTest.upisi("\n\n");
		//utils.SaveTest.upisi("Otkriven u: "+int.toString(pripadnost)+"\n");
		//utils.SaveTest.upisi("Vodi na: [ ");
		
		BasicDBList vodiNa = new BasicDBList();
		for(int i = 0;i<pripada_mu.size();i++) {
			//utils.SaveTest.upisi(int.toString(pripada_mu.get(i)) + " ");
			vodiNa.add(pripada_mu.get(i));
		}
		base.MongoConnect.insertIntoMongo(rbr, naziv, opis, URL, pripadnost, vodiNa);
		//utils.SaveTest.upisi(" ]");
		//utils.SaveTest.upisi("\n\n-------------------------------------------------------\n\n");
		
	}
	
	public boolean exists_in_list (int key)
	{
		for(int i = 0;i<pripada_mu.size();i++)
		{
			if(key == pripada_mu.get(i))
				return true;
		}
			
		return false;
	}
	
	
}
