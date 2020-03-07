package fourCars.Poc_NaturalAPI_Design;

public class Parameter{
    private String name;
    private String type;
    private Boolean required;
    
    public Parameter() {
        name = null;
        type = null;
        required = true;
    }
    
    public Parameter(String paramName) {
        this.name = paramName;
        this.type = null;
        this.required = true;
    }
    
    public Parameter(String paramName, String paramType) {
        this.name = paramName;
        this.type = paramType;
        this.required = true;
    }

    public void setName(String paramName) {
        this.name = paramName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setType(String paramType) {
        this.type = paramType;
    }
    
    public String getType() {
        return type;
    }
    
    public void setRequired(boolean isParamRequired) {
        this.required = isParamRequired;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    @Override public String toString() {
        return name; //"name: " + name + ", type: " + type + ", required: " + required + "\n";
    }
    
}
