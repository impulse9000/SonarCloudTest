package fourCars.Poc_NaturalAPI_Design;

import java.util.ArrayList;
import java.util.List;

public class Actor {
    private String name;
    private List<Operation> lOperation;
    
    public Actor(){
        this.name = null;
        this.lOperation = new ArrayList<Operation>();
    }
    
    public Actor(String userName){
        this.name = userName;
        this.lOperation = new ArrayList<Operation>();
    }
    
    public void setName(String userName) {
        this.name = userName;
    }
    
    public String getName() {
        return name;
    }
    
   public void addOperation(Operation operationToAdd) {
       this.lOperation.add(operationToAdd);
   }
   
   public void addOperations(List<Operation> operationsToAdd) {
       this.lOperation.addAll(operationsToAdd);
   }
   
   public List<Operation> getOperations() {
       return lOperation;
   }
   
   public List<Operation> getOperationsByName(String operationName) {
       List<Operation> lSameNameOperations = new ArrayList<Operation>();
       for (Operation op : lOperation) {
           if (op.getName().equals(operationName))
               lSameNameOperations.add(op);
       }
       return lSameNameOperations;
   }
   
   @Override
   public String toString() {
       String OperationStr = "";
       for (Operation s : lOperation) {
           OperationStr += s.toString();
       }
       return OperationStr;
   }
   
}
