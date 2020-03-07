package fourCars.Poc_NaturalAPI_Design;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import FourCats.Poc_NaturalAPI_Discover.LemmatizerAccess;
import FourCats.Poc_NaturalAPI_Discover.LemmatizerAccessInterface;
import FourCats.Poc_NaturalAPI_Discover.LemmatizerData;
import FourCats.Poc_NaturalAPI_Discover.ParserAccess;
import FourCats.Poc_NaturalAPI_Discover.ParserAccessInterface;
import FourCats.Poc_NaturalAPI_Discover.ParserData;


public class SupportModule {
    public static String getFeatureNameFromGherkin(String gherkinString) {
        //get feature name by picking the text between keywords "Feature:" and "Scenario:"
        int indexFeatureStart = gherkinString.indexOf("Feature:")+9;
        int indexFeatureEnd = gherkinString.indexOf("Scenario")-1;
        return gherkinString.substring(indexFeatureStart,indexFeatureEnd);
    }
    
    public static String getScenarioNameFromGherkin(String gherkinString) {
        //get scenario name by picking the text between first row and "Given:"
        int indexFeatureStart = 1;
        int indexFeatureEnd = -1;
        if (gherkinString.indexOf("As a:")!=-1)
            indexFeatureEnd = gherkinString.indexOf("As a:")-1;
        else
            indexFeatureEnd = gherkinString.indexOf("Given")-1;
        return gherkinString.substring(indexFeatureStart,indexFeatureEnd);
    }
    
    public static String getActorNameFromGherkin(String gherkinString) {
        //get scenario actor by picking the text between keywords "As a:" and "Given:"
        //if there isn't the keyword "As as:" return "All"
        int indexActorStart = -1;
        int indexActorEnd = 0;
        if (gherkinString.indexOf("As a:")!=-1) {
            indexActorStart = gherkinString.indexOf("As a:")+6;
            indexActorEnd = gherkinString.indexOf("Given")-1;
            if (indexActorStart <= indexActorEnd)
                return gherkinString.substring(indexActorStart,indexActorEnd);
        }
        return "All";
    }
   
    
    public static List<Parameter> getParametersFromNouns(LemmatizerData lemData) {
        BlackList blackList = new BlackList();
        List<Parameter> lParameters = new ArrayList<Parameter>();
        for (LemmatizerData.WordTag wtag : lemData.getList()) {
            if(wtag.getTag().contains("NN") && !blackList.contains(wtag.getLemma())) {
                lParameters.add(new Parameter(wtag.getLemma()));  
                blackList.addTerm(wtag.getLemma());
            }
        }
        return lParameters;
    }
    
    
   public static String loadFile(String filename) throws IOException {
       BufferedReader br = new BufferedReader(new FileReader(filename));
       String doc = null;
       try {
           StringBuilder sb = new StringBuilder();
           String line = br.readLine();
           while (line != null) {
               sb.append(line);
               sb.append("\n");
               line = br.readLine();
           }
           doc = sb.toString();
          }
       catch(Exception ioe) {
           System.out.println("error");
       }
       finally {
           br.close();
       }
      return doc;
   }
   
   public static List<Actor> elaborateFeature(String featurePath) throws IOException {
      String doc = loadFile(featurePath);
      //run document lemmatization
      LemmatizerAccessInterface lemmatizer = new LemmatizerAccess();
      LemmatizerData result = lemmatizer.lemmatizeSentence(doc);
      
      ParserAccessInterface depparser = new ParserAccess();
      List<Actor> lActors = new ArrayList<Actor>();
      ParserData dobjParsedData = null;
      String[] arrScenarios = doc.split("Scenario:"); //split all scenarios to different strings
      for (String scenario : Arrays.asList(arrScenarios).subList(1, arrScenarios.length)) {
          System.out.println("--------------------------------------GHERKIN SCENARIO: '" + SupportModule.getScenarioNameFromGherkin(scenario) + "'--------------------------------------");
          System.out.println(scenario);
          dobjParsedData = depparser.parseSentence(scenario);
          Actor actor = new Actor(SupportModule.getActorNameFromGherkin(scenario));//actor in the current scenario
          actor.addOperations(SupportModule.suggestOperations(dobjParsedData, scenario, actor, lActors)); //add operations to the actor
          boolean actorFound = false; //check if the actor is already in the Actors list (lActors) 
          if (lActors!=null) {
              for (Actor u : lActors) {
                  if (actor.getName().equals(u.getName())) {
                      //actor already in the Actors list -> add operations the already existing actor
                      u.addOperations(actor.getOperations());
                      actorFound = true;
                      break;
                  }
              }
              if(!actorFound) 
                  lActors.add(actor);
          }
          else {
              lActors = new ArrayList<Actor>();
              lActors.add(actor);
          }
      }
      
      System.out.println("Candidate parameters for operations: \n" + SupportModule.getParametersFromNouns(result) + "\n");
      
      return lActors;
       
   }
   
   public static void createJsonFromBAL(BAL bal, String outputPath) throws IOException {
       //Creating the ObjectMapper object
         ObjectMapper mapper = new ObjectMapper();
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         //Converting the Object to JSONString
         String jsonString;
         try {
             jsonString = mapper.writeValueAsString(bal);
             System.out.println(jsonString);
             mapper.writeValue(new File(outputPath), bal);
             System.out.println("\n File json creato!");
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }
   }
   
   public static String extractParameterFromOperationName(Operation operation) {
       int indexFeatureStart = operation.getName().indexOf("_")+1;
       return operation.getName().substring(indexFeatureStart);
   }
   
   public static void suggestParameter(Operation operation) throws IOException {
       List<Parameter> candidatesParameters = new ArrayList<Parameter>();
       String mainCandidateParam = extractParameterFromOperationName(operation);
       System.out.println("--------------------------------------PARAMETER SUGGESTION FOR '" + operation.getName() + "'--------------------------------------");
       System.out.println("Would you like to add '" + mainCandidateParam + "' as a parameter? 1. YES, 2. NO\n");
       BufferedReader reader =
               new BufferedReader(new InputStreamReader(System.in));
       String input = reader.readLine();
       if (input.equals("1")) {
           System.out.println("Please, insert the type for the parameter '" + mainCandidateParam +  "': (void, string, int, bool, double, float...)" );
           System.out.println("Otherwise, press the enter key.\n" );
           input = reader.readLine(); //parameter type
           if (input.equals(""))
               candidatesParameters.add(new Parameter(mainCandidateParam));
           else 
               candidatesParameters.add(new Parameter(mainCandidateParam, input));
           for (Parameter p : candidatesParameters)
               operation.addParameter(p);
       }
       //blackList.addTerm(suggestedOp); 
   }
   
   /**
    * suggests operations to the actor who can accept or refuse to add them to the list of selected operations
    * if an operation is already defined for a specific actor, then show alert and ask what to do (update or continue)
    * @return the list of selected operations
    * @param dobjParsedData is the output of the parseSentece method of the NLP for the specific scenario
    * @param scenario is the string representation of the scenario we want to work with (It's a single scenario in a feature)
    * @param ActorScenario is the actor to who the scenario is related
    * @param lAllActors is the list of all Actors who will be or who have already been added to the BAL
    */
   public static List<Operation> suggestOperations(ParserData dobjParsedData, String scenario, Actor ActorScenario, List<Actor> lAllActors) throws IOException {
       //suggest operation given all the dobj found by NLP for the specific scenario 
       BlackList blackList = new BlackList();
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String input = null;
       Operation selectedOperation = null;
       String suggestedOpFormatted = null; //formatted like 'withdraw_cash' (with the underscore)
       List<Operation> candidatesOperations = new ArrayList<Operation>(); //list of suggested operation that have been accepted
       for (String suggestedOp: dobjParsedData.getList()) {
           suggestedOpFormatted = suggestedOp.replaceAll(" ", "_");
           //check that the suggested operation is not in the blackList and other optimizations for the suggestion
           if(!blackList.contains(suggestedOpFormatted) && !blackList.contains(suggestedOpFormatted.substring(suggestedOpFormatted.indexOf("_")+1))) {
               selectedOperation = new Operation(suggestedOpFormatted);
               if(SupportModule.isDuplicateOperation(selectedOperation, ActorScenario, lAllActors)) { 
                   //if the operation is already associated with that actor then show alert and ask what to do
                   System.out.println("--------------------------------------------!DUPLICATE OPERATION ALERT!--------------------------------------------" );
                   System.out.println("I really think that the operation: '" + selectedOperation.getName() + "' can be helpful to you");
                 
                   List <Operation> lOperationAlredyDefined = SupportModule.getDuplicateOperations(selectedOperation, ActorScenario, lAllActors);
                   System.out.println("Those operations '"  + lOperationAlredyDefined.toString() + "' have already been defined for the actor '" + ActorScenario.getName() + "'");
                   System.out.println("Would you like to update one of them? 1. YES, 2. NO\n" );
                   input = reader.readLine();
                   if (input.equals("1")) {
                       int switchCase = 1;
                       if (lOperationAlredyDefined.size()>1) {
                           System.out.println("Choose which operation you would like to update:" );
                           for (Operation definedOp: lOperationAlredyDefined) {
                               System.out.println(switchCase + ". " + definedOp);
                               switchCase++;
                               input = reader.readLine();
                           }
                       }
                       SupportModule.updateOperation(lOperationAlredyDefined.get(Integer.valueOf(input)-1));
                   }
               }
               else {
                   System.out.println("--------------------------------------------NEW OPERATION SUGGESTION--------------------------------------------" );
                   System.out.println("Would you like to add '" + suggestedOpFormatted +  "' to your operations? 1. YES, 2. NO\n" );
                   input = reader.readLine();
                   if (input.equals("1")) {
                       System.out.println("Please, insert the return type for the operation '" + suggestedOpFormatted +  "': (void, string, int, bool, double, float...)" );
                       System.out.println("Otherwise, press the enter key.\n" );
                       input = reader.readLine(); //input for the return type of the operation
                       if (!input.equals(""))
                           selectedOperation.setType(input);             
                       candidatesOperations.add(selectedOperation);
                       SupportModule.suggestParameter(selectedOperation);
                       blackList.addTerm(suggestedOpFormatted);
                   }
               }
           }
       }
       return candidatesOperations;
      
   }
   
 //check if an operation with the same name is already associate with an actor who has the same name of the given actor
   public static boolean isDuplicateOperation(Operation operationToCheck, Actor relativeActor, List<Actor> lActors) {
       List<Operation> operationsAlreadyInList = new ArrayList<Operation>();
       for (Actor actorAlreadyInList : lActors) {
           operationsAlreadyInList = actorAlreadyInList.getOperations();
           if (actorAlreadyInList.getName().equals(relativeActor.getName())){
               for (Operation operationInList : operationsAlreadyInList) {
                   if (operationInList.getName().equals(operationToCheck.getName()))
                       return true;
               }
           }      
       }
       return false;
   }
   
   // return a list of operations which has the same name of the operationToCheck
   // and that are already been associated with an actor (from lActors) who has the same name of the given actor(relativeActor)
   public static List<Operation> getDuplicateOperations(Operation operationToCheck, Actor relativeActor, List<Actor> lActors) {
       List<Operation> lDuplicateOperations = new ArrayList<Operation>(); //to return
       List<Operation> operationsAlreadyInList = new ArrayList<Operation>();
       for (Actor actorAlreadyInList : lActors) {
           operationsAlreadyInList = actorAlreadyInList.getOperations();
           if (actorAlreadyInList.getName().equals(relativeActor.getName())){
               for (Operation operationInList : operationsAlreadyInList) {
                   if (operationInList.getName().equals(operationToCheck.getName())) {
                       lDuplicateOperations.add(operationInList);
                   }
               }
           }      
       }
       return lDuplicateOperations;
   }
   
   public static void updateOperation(Operation operationToUpdate) throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String input = null;
       System.out.println("-----------------------------------------OPERATION TO UPDATE: '" + operationToUpdate.toString() + "'-----------------------------------------" );
       System.out.println("1. Add new parameter");
       System.out.println("2. Update return type");
       System.out.println("3. Abort update");
       input = reader.readLine();
       switch(Integer.parseInt(input)) {
           case 1:{
               System.out.println("-----------------------------------------ADD PARAMETER TO: '" + operationToUpdate.toString() + "'-----------------------------------------");
               Parameter paramToAdd = new Parameter();
               System.out.println("Insert parameter name");
               input = reader.readLine();
               if (!input.equals("")) {
                   paramToAdd.setName(input);
                   System.out.println("Insert parameter type");
                   input = reader.readLine();
                   if (!input.equals("")) {
                       paramToAdd.setType(input);
                   }
                   System.out.println("Is this parameter required? 1. TRUE, 2. FALSE");
                   boolean required = true;
                   input = reader.readLine();
                   switch(Integer.parseInt(input)) {
                       case 1:{ 
                           required = true;
                           break;
                           }
                       case 2:{
                           required = false;
                           break;
                       }
                       default:{
                           required = true;
                           break;
                       }   
                   }
                   paramToAdd.setRequired(required);
                   operationToUpdate.addParameter(paramToAdd);
                   System.out.println("Done! This is your updated opearation: '" + operationToUpdate.toString() + "'");
               }
               else {
                   System.out.println("Error. No changes have been made.");
               }
               break;
           }
           case 2:{
               System.out.println("-----------------------------------------UPDATE RETURN TYPE FOR: '" + operationToUpdate.toString() + "'-----------------------------------------");
               System.out.println("Insert the new return type for the operation");
               input= reader.readLine();
               if (!input.equals("")) {
                   operationToUpdate.setType(input);
                   System.out.println("Done! This is your updated opearation: '" + operationToUpdate.toString() + "'");
               }
               else if(input.equals("") || input.equals(operationToUpdate.getType())) {
                   System.out.println("Error. No changes have been made.");
               }
               break;
           }
           case 3:{
               System.out.println("Abort. No changes have been made.");
               break;
           }
           default:
               System.out.println("Error. No changes have been made.");
               break;
       }
   }
}
