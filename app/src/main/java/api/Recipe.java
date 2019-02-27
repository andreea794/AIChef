package api;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    final private String recipeName;
    final private String recipeID;
    final private String recipeImageLink;
    //cant make recipe webpage url final as it has to be set in the later stage
    private String recipeURL;


    //-------------GETTERS-----------//
    public String getRecipeName(){
        return recipeName;
    }

    public String getRecipeID(){
        return recipeID;
    }

    public String getRecipeURL(){
        return recipeURL;
    }

    public void setRecipeURL(String url){ recipeURL = url;}

    public String getRecipeImageLink(){
        return recipeImageLink;
    }

    //-------------SETTERS----------------//
    public Recipe(String name, String id, String image){
        this.recipeName = name;
        this.recipeID = id;
        this.recipeImageLink = image;
        //this.ingredients = ingredients;
    }

    //for testing purpose only
    public Recipe(String id){
        this.recipeID = id;
        this.recipeImageLink = null;
        this.recipeName = null;
        this.recipeURL = null;
        //this.ingredients = null;
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[0];
        }
    };

    private Recipe(Parcel parcel) {
        recipeName = parcel.readString();
        recipeID = parcel.readString();
        recipeImageLink = parcel.readString();
        recipeURL = parcel.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(recipeName);
        parcel.writeString(recipeID);
        parcel.writeString(recipeImageLink);
        parcel.writeString(recipeURL);
    }
}
