package FourCats.Poc_NaturalAPI_Discover;

import java.util.LinkedList;

public class BDL {
	private LinkedList<WordCounter> nouns;
	private LinkedList<WordCounter> verbs;
	private LinkedList<WordCounter> predicates;
	
	public BDL() {
		nouns=new LinkedList<WordCounter>();
		verbs=new LinkedList<WordCounter>();
		predicates=new LinkedList<WordCounter>();
	}
	
	public void addNoun(String noun) {
		Boolean present = false;
		for (WordCounter w : nouns) {
			//controla se le stringhe sono uguali
			if(w.getWord().equalsIgnoreCase(noun)) {
				w.incrementCounter();
				present = true;
				break;
			}
		}
		if(!present) nouns.add(new WordCounter(noun));
	}
	public void addVerb(String verb) {
		Boolean present = false;
		for (WordCounter w : verbs) {
			//controla se le stringhe sono uguali
			if(w.getWord().equalsIgnoreCase(verb)) {
				w.incrementCounter();
				present = true;
				break;
			}
		}
		if(!present) verbs.add(new WordCounter(verb));
	}
	public void addPredicate(String predicate) {
		predicates.add(new WordCounter(predicate));
	}
	
	
	public LinkedList<WordCounter> getNouns() {return nouns;}
	
	public LinkedList<WordCounter> getVerbs() {return verbs;}
	
	public LinkedList<WordCounter> getPredicates() {return predicates;}

	
}
