package fourCars.Poc_NaturalAPI_Design;

import java.io.IOException;
import java.util.List;


public class Main 
{
    public static void main( String[] args ) throws IOException
    {
        
      List<Actor> lActors = SupportModule.elaborateFeature("txt_documents\\prova.feature");
      BAL bal = new BAL(lActors);
      SupportModule.createJsonFromBAL(bal, "output.json");
        
    }
}
