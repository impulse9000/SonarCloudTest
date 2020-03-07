package FourCats.Poc_NaturalAPI_Discover;

import java.util.LinkedList;
import java.util.List;

public class LemmatizerData {
	public class WordTag{
		private String value;
		private String tag;
		private String lemma;
		
		public WordTag(String w, String t, String l) {
			value=w;
			tag=t;
			lemma=l;
		}
		
		public String getValue() {return value;}
		public String getTag() {return tag;}
		public String getLemma() {return lemma;}
	}
	
	private List<WordTag> lemmatizationResult;
	
	public LemmatizerData() {
		lemmatizationResult = new LinkedList<WordTag>();
	}
	
	public void addElement(String word, String tag, String lemma) {
		lemmatizationResult.add(new WordTag(word,tag,lemma));
	}
	
	public List<WordTag> getList() {
		return lemmatizationResult;
	};
}
