package com.teamalpha.aichef;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import api.Recipe;

public class RecipesListAdapter extends BaseAdapter {

    static List<Recipe> recipesList;
    private Context c;
    private LayoutInflater mInflater;
    private Resources res;
    private TypedArray tickImage;
    static List<Boolean> checkedRecipes;
    private List<Recipe> pickedRecipeNames;
    ExecutorService executor;


    public List<Recipe> getPickedRecipeNames(){
        return pickedRecipeNames;
    }

    public RecipesListAdapter(Context c, Resources res){
        this.c = c;
        this.mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = res;
        tickImage = this.res.obtainTypedArray(R.array.tickImages);
        if(recipesList == null){
            this.recipesList = new LinkedList<Recipe>();
        }
        this.checkedRecipes = new LinkedList<Boolean>();
        this.pickedRecipeNames = new LinkedList<Recipe>();
        this.executor = Executors.newFixedThreadPool(5);

    }

    @Override
    public int getCount() {
        return recipesList.size();
    }

    @Override
    public Object getItem(int i) {
        if(recipesList.contains(i)){
            return recipesList.get(i);
        }
        else{
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        View v;
        if(view == null){
            v = mInflater.inflate(R.layout.recipes_list_layout, null);
        }
        else{
            v = view;
        }

        final Recipe recipe = recipesList.get(i);
        final ImageView recipeImg= (ImageView)v.findViewById(R.id.recipeImg);
        final CheckedTextView recipeText = (CheckedTextView)v.findViewById(R.id.recipeText);
        String web = recipe.getRecipeImageLink();

        //If the URL is null, that means the API call has not returned, hence
        //We return a view without the URL
        if(web == null){
            return v;
        }
        String[] substring = web.split("\\.");
        String file_format = substring[substring.length-1];

        //Download the recipe image file, using threads
        ContextWrapper contextWrapper = new ContextWrapper(c);
        File directory = contextWrapper.getDir(c.getFilesDir().getName(), Context.MODE_PRIVATE);
        File imgFile =  new File(directory, recipe.getRecipeID() + file_format);
        try{
            if(!imgFile.exists()){
                imgFile.createNewFile();
                executor.execute(new Runnable() {
                    public void run() {
                        try {
                            String web = recipe.getRecipeImageLink();
                            String[] substring = web.split("\\.");
                            String file_format = substring[substring.length - 1];
                            ContextWrapper contextWrapper = new ContextWrapper(c);
                            File directory = contextWrapper.getDir(c.getFilesDir().getName(), Context.MODE_PRIVATE);
                            File imgFile = new File(directory, recipe.getRecipeID() + file_format);
                            URL url = new URL(web);
                            System.setProperty("http.agent", "chrome");
                            InputStream in = new BufferedInputStream(url.openStream());
                            OutputStream out = new BufferedOutputStream(new FileOutputStream(imgFile));
                            for (int j; (j = in.read()) != -1; ) {
                                out.write(j);
                            }
                            in.close();
                            out.close();
                            notifyDataSetChanged();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        recipeImg.setImageBitmap(myBitmap);
        recipeText.setText(recipesList.get(i).getRecipeName());
        final String url = recipe.getRecipeURL();
        Button moreButton = (Button)v.findViewById(R.id.moreButton);

        //Make the button non-clickable if the thread has not finished download
        if(url == null){
            moreButton.setEnabled(false);
        }
        else{
            moreButton.setEnabled(true);
        }


        /*
        Button to go to the recipe link
         */
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });


        //LEFT OUT FOR NOW
//        recipeImg.setImageResource(recipe.getRecipeImageRes());
        //make sure recipe is consistent with Current View as determined
        //recipeText.setChecked(checkedRecipes.get(i));
//        if(recipeText.isChecked()){
//            recipeText.setCheckMarkDrawable(R.drawable.checked);
//        }
//        else{
//            recipeText.setCheckMarkDrawable(0);
//        }
//        //by checkedRecipes
//        recipeText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkedRecipes.get(i)){
//                    recipeText.setCheckMarkDrawable(0);
//                    recipeText.setChecked(false);
//                    checkedRecipes.set(i, false);
//                    pickedRecipeNames.remove(recipesList.get(i)); //remove from pickedRecipes
//                }
//                else {
//                    recipeText.setCheckMarkDrawable(R.drawable.checked);
//                    recipeText.setChecked(true);
//                    checkedRecipes.set(i, true);
//                    pickedRecipeNames.add(recipesList.get(i)); //add to pickedRecipes
//                }
//            }
//        });
        return v;
    }
}
