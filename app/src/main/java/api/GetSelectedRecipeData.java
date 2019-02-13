package api;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//front end need to check whether the list mRecipe is empty: if it is, show prompt for the users to select from the suggested recipes
public class GetSelectedRecipeData {

    private static List<Recipe> mSelectedRecipes;
    private static List<Ingredient> mShoppingList;
    private static JSONObject responseObj;

    private static String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/<recipeID>/information";

//    public static  getShoppingList(){ return mShoppingList; }

    //info of ingredients of the currently chosen recipe is shown upon clicking that particular recipes
    public static List<Ingredient> callIngredientsListAPI(Recipe selectedRecipe, RequestQueue mQueue){
        String id = selectedRecipe.getRecipeID();
        String curURL = url.replaceAll("<recipeID>", id);

        System.out.println(curURL);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, curURL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        responseObj = response;
                        mShoppingList =  new ArrayList<>();
                        JSONArray curIngdList = null;
                        try {
                            curIngdList = (JSONArray) responseObj.get("extendedIngredients");
                            System.out.println("Number of ingredients in the current Recipe: "+ curIngdList.length());
                            for(int c=0; c<curIngdList.length(); c++){
                                String curIngdName = (String)curIngdList.getJSONObject(c).get("name");
                                Ingredient newIngredient = new Ingredient(curIngdName);
                                System.out.println(newIngredient.getName());
                                mShoppingList.add(newIngredient);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }){

            // Passing some request headers

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("X-RapidAPI-Key", "d5b6945f4amshcf5e58babc52c8cp1930c0jsn329275235e32");
                return headers;
            }
        };

        mQueue.add(request);
        return mShoppingList;

    }

//    public List<Ingredient> getShoppingList(List<Recipe> selectedRecipesFromUser){
//        mSelectedRecipes = selectedRecipesFromUser;
//
//        for (int i=0; i<mSelectedRecipes.size(); i++){
//
//        }
//
//    }

}
