package api;

public class Recipe {
    final private String recipeName;
    final private String recipeID;
    final private String recipeImageLink;
    //cant make recipe webpage url final as it has to be set in the later stage
    private String recipeURL;
    //final private List<Ingredient> ingredients;


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


//    public List<Ingredient> getIngredients(){
//        return ingredients;
//    }


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

}
