package api;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRecipeList {
//    final TextView mTextView = (TextView) findViewById(R.id.text);
    private static RequestQueue mQueue;
    private static JSONArray responseArr;
    public static List<Recipe> mRecipeList;

    private final static String url ="https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=5&ranking=1&ingredients=apples%2Cflour%2Csugar";
// ...
    //only get the JSON response in this part and deal with it in the main activity
    public static void setRequestQueue(RequestQueue queue){
        mQueue = queue;
    }

    //---------GET RECIPE LIST TO THE FRONT UI------------------//
    public static List<Recipe> getRecipeList(){
        return mRecipeList;
    }

    //---------POPULATE THE RECIPE LIST WITH INFORMATION FROM JSON RESPONSE----------------------//
    public static void callRecipeListAPI() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        responseArr = response;

                        int index;
                        for (index = 0; index < responseArr.length(); index++) {

                            try {
//                                System.out.println(responseArr.getJSONObject(index).getString("id"));
                                JSONObject curRJObj = responseArr.getJSONObject(index);
                                List<Ingredient> curIgds = new ArrayList<>();
                                Recipe curRecipe = new Recipe(curRJObj.getString("title"), curRJObj.getString("id"), curRJObj.getString("image"),
                                        curRJObj.getString("usedIngredientCount"), curIgds);
                                mRecipeList.add(curRecipe);
                                //add in the ingredient list in the later functions which will then be called by the recipe-list UI
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
        mQueue.add(request);
    }

}
