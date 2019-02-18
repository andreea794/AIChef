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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//front end need to check whether the list mRecipe is empty: if it is, show prompt for the users to select from the suggested recipes
public class GetSelectedRecipeData {
    private static int counter = 0;
    private static JSONObject responseObj;

    private static String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/<recipeID>/information";


    //info of ingredients of the currently chosen recipe is shown upon clicking that particular recipes
    public static void callIngredientsListAPI(final List<Recipe> selectedRecipes, RequestQueue queue, final List<Ingredient> shoppingList) {

        final Set<Ingredient> allIngredients = new HashSet<>();

        for(int i=0; i<selectedRecipes.size(); i++) {

            String id = selectedRecipes.get(i).getRecipeID();
            String curURL = url.replaceAll("<recipeID>", id);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, curURL, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("BREAKPOINT 1");
                            responseObj = response;
                            JSONArray curIngdList = null;
                            try {
                                curIngdList = (JSONArray) responseObj.get("extendedIngredients");
//                            System.out.println("Number of ingredients in the current Recipe: " + curIngdList.length());
                                for (int c = 0; c < curIngdList.length(); c++) {
                                    String curIngdName = (String) curIngdList.getJSONObject(c).get("name");
                                    Ingredient newIngredient = new Ingredient(curIngdName);
//                                    System.out.println(newIngredient.getName());
                                    allIngredients.add(newIngredient);
//                                    System.out.println(allIngredients.size());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //check whether it's the last api call
                            counter ++;
                            if(counter == 0) shoppingList.clear();
                            else if(counter == selectedRecipes.size()) {
                                shoppingList.addAll(allIngredients);
//                                System.out.println("ShoppingList Size after reaching the last : " + allIngredients.size());
                            }

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

            //a request need to be sent for every for-loop
            queue.add(request);
        }

    }
}
