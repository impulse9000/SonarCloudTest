package fourCars.Poc_NaturalAPI_Design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlackList {
    public List<String> blackList; //blacklist that contain unwanted terms (if a term is in this list you probably don't want to use it)
    public BlackList() {
        blackList = new ArrayList<String>();
        blackList.addAll(Arrays.asList("Actor","Scenario","Given","When", "Then", "feature", "scenario", "given", "when","then"));
    }
    public BlackList(List<String> listTerms) {
        blackList = new ArrayList<String>();
        blackList.addAll(listTerms);
    }
    public void addTerm(String term) { //useful if you need to check for duplicate terms
        blackList.add(term);
    }
    
    public void addTerms(List<String> listTerms) {
        blackList.addAll(listTerms);
    }
    public boolean contains(String term) {
        return blackList.contains(term);
    }
    
}
