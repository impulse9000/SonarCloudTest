package FourCats.Poc_NaturalAPI_Discover;

import java.util.LinkedList;
import java.util.List;

public class ParserData {
    private List<String> nobj;

    public ParserData(){
        nobj = new LinkedList<String>();
    }

    public void addElement(String s){
        nobj.add(s);
    }

    public List<String> getList(){
        return nobj;
    }
}

