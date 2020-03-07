package fourCars.Poc_NaturalAPI_Design;

import java.util.ArrayList;
import java.util.List;

public class Operation {
    private String name;
    private List<Parameter> param;
    private String type;
    
    public Operation() {
        this.name = null;
        this.param = new ArrayList<Parameter>();
        this.type = null;
    }
    public Operation(String operationName) {
        this.name = operationName;
        this.param = new ArrayList<Parameter>();
        this.type = null;
    }
    
    public Operation(String operationName, String operationType) {
        this.name = operationName;
        this.param = new ArrayList<Parameter>();
        if(operationType.equals(""))
            this.type = null;
         else
            this.type = operationType;
    }
    
    public void setName(String operationName) {
        this.name = operationName;
    }
    
    public String getName() {
        return name;
    }
    
    public void addParameterName(String paramName) {
        Parameter par = new Parameter();
        par.setName(paramName);
        this.param.add(par);
    }
    
    public void addParameter(Parameter parameter) {
        this.param.add(parameter);
    }
    public void updateParameterName(String paramName, String newName) {
        for (Parameter p : param) {
            if (p.getName()==paramName) {
                p.setName(newName);
                return;
            }
        }
    }
    
    public void updateParameterType(String paramName, String newType) {
        for (Parameter p : param) {
            if (p.getName()==paramName) {
                p.setType(newType);
                return;
            }
        }
    }
    
    public void updateIsParameterRequired(String paramName, boolean isRequired) {
        for (Parameter p : param) {
            if (p.getName()==paramName) {
                p.setRequired(isRequired);
                return;
            }
        }
    }
    
    public List<Parameter> getParameters() {
        return param;
    }
    
    public void setType(String operationType) {
        this.type = operationType;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        String operationParams = "";
        boolean firstParam = true;
        for (Parameter par : param) {
            if (firstParam) {
                operationParams+=par.getType()+ " " + par.getName();
                firstParam = false;
            }
            else {
                operationParams+=", " + par.getType()+ " " + par.getName();
            }
        }
        return this.type + " " + this.name + "(" + operationParams + ")";
    }
    
    
}
