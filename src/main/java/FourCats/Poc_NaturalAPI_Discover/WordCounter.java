package FourCats.Poc_NaturalAPI_Discover;

/*Coppia parola-conteggio utilizzata dal BDL*/

public class WordCounter {
	private String word;
	private Integer count;
	
	public WordCounter(String w, Integer c) {
		word=w;
		count=c;
	}
	
	public WordCounter(String w) {
		this(w,1);
	}
	
	public void incrementCounter() {
		count++;
	}
	
	public String getWord() {return word;}
	public Integer getCount() {return count;}
}
