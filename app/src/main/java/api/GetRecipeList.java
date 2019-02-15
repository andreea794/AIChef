package api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRecipeList {
    private static JSONArray responseArr;


    private final static String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=15&ranking=1&ingredients=";
    //this is the base url

//    //only get the JSON response in this part and deal with it in the main activity
//    public static void setRequestQueue(RequestQueue queue){
//        mQueue = queue;
//    }

    //---------POPULATE THE RECIPE LIST WITH INFORMATION FROM JSON RESPONSE----------------------//
    public static void callRecipeListAPI(List<Ingredient> scannedIngredients, RequestQueue mQueue, final List<Recipe> list) {
        //need to have some way to pass the name from the scannedIngredients to the url
        final List<Recipe> mRecipeList = new ArrayList<>();

        String curURL = url;

        for (int i = 0; i < scannedIngredients.size(); i++) {
            if (i != scannedIngredients.size() - 1)
                curURL = curURL + scannedIngredients.get(i).getName() + "%2C";
            else curURL = curURL + scannedIngredients.get(i).getName();
        }
        System.out.println(curURL);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, curURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        responseArr = response;


                        int index;
                        for (index = 0; index < responseArr.length(); index++) {

                            try {
//                                System.out.println(responseArr.getJSONObject(index).getString("id"));
                                JSONObject curRJObj = responseArr.getJSONObject(index);
//                                System.out.println(curRJObj.toString());
//                                System.out.println("Recipe ID: " + curRJObj.get("id") + ", Recipe Name: " + curRJObj.get("title"));
                                List<Ingredient> curIgds = new ArrayList<>();//initialise an empty list to input ingredients so that can add ingredients
//                                System.out.println(curRJObj.getString("title"));
//                                System.out.println(curRJObj.getString("id"));
//                                System.out.println(curRJObj.getString("image"));
                                Recipe curRecipe = new Recipe(curRJObj.getString("title"), curRJObj.getString("id"), curRJObj.getString("image"),
                                        curRJObj.getString("usedIngredientCount"), curIgds);
                                mRecipeList.add(curRecipe);
                                //add in the ingredient list in the later functions which will then be called by the recipe-list UI
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        //Clear the recipe list in MainActivity and fill up with the newly retrieved recipes.
                        list.clear();
                        list.addAll(mRecipeList);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }) {
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
    }

}
