package api;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
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

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[0];
        }
    };

    private Ingredient(Parcel parcel) {
        name = parcel.readString();
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    @Override
    public boolean equals(Object other) {
        return this.name.equals(((Ingredient) other).getName());
    }
}
