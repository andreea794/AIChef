package api;

public class Ingredient {
    final private String name;
//    final private String amount;
//    final private String unit;
//    final private String instruction;

    //-------------GETTERS----------------//
    public String getName(){
        return name;
    }
//
//    public String getAmount(){
//        return amount;
//    }
//
//    public String getUnit(){
//        return unit;
//    }


//    //----------Constructor---------------//
//    public Ingredient(String name, String amount, String unit, String originalString){
//        this.name = name;
//        this.amount = amount;
//        this.unit = unit;
//        this.instruction = originalString;
//    }

    //----------Constructor-----------------//
    public Ingredient(String name){
        this.name = name;
//        this.amount = null;
//        this.unit = null;
//        this.instruction = null;
    }

}
