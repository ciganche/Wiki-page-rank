package base;

import java.io.Serializable;

public class SortedPageRank implements Comparable<SortedPageRank>, Serializable {

    int rbr;
    int value;

    private String naziv;
	private String opis;
	private String URL;
	
	
    public SortedPageRank(int rbr, int value){
        this.rbr = rbr;
        this.value = value;
    }

    public int compareTo(SortedPageRank o) {
        return this.value - o.value;
    }
    
    public int getValue(){
    	return value;
    }
    
    
    public int getRbr(){
    	return rbr;
    }
    public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String sting) {
		this.naziv = sting;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
    
  
}
