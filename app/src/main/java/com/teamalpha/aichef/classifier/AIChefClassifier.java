package com.teamalpha.aichef.classifier;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.teamalpha.aichef.MainActivity;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIChefClassifier implements Runnable {

    private static final String MODEL_PATH = "graph4.mp3";
    private static final String LABEL_PATH = "labels2.mp3";

    private static final float MIN_OBJ_CHANCE = 0.3f;
    private static final int INPUT_SIZE = 224;

    private int numClasses;

    private Interpreter tfInterpreter;
    private String[] classNames;

    private Bitmap imgToClassify;
    private boolean imgValid;
    private boolean canAcceptImg;

    private MainActivity mainActivity;

    /**
     * Loads the NN from storage.
     * May take noticeable time so should be called once (and only once) at startup
     */

    public AIChefClassifier(AssetManager assetManager, MainActivity activity) {
        this.mainActivity = activity;

        imgValid = false;
        canAcceptImg = true;

        ByteBuffer modelBuf = null;

        // Load model into buffer
        try {
            AssetFileDescriptor afd = assetManager.openFd(MODEL_PATH);
            FileInputStream in = new FileInputStream(afd.getFileDescriptor());
            FileChannel channel = in.getChannel();
            modelBuf = channel.map(FileChannel.MapMode.READ_ONLY, afd.getStartOffset(),
                                    afd.getDeclaredLength());

        } catch(IOException ioe) {
            Log.e("AIChefClassifier",
                    "Error loading network:\n" + ioe.getMessage());
            ioe.printStackTrace();
            return;
        }

        tfInterpreter = new Interpreter(modelBuf);


        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open(LABEL_PATH)));

            List<String> labelsList = new ArrayList<>();

            String line;
            while((line = reader.readLine()) != null) {
                labelsList.add(line);
            }

            numClasses = labelsList.size();
            classNames = new String[labelsList.size()];

            for(int i = 0; i < labelsList.size(); i++)
                classNames[i] = labelsList.get(i);
        }
        catch(IOException ioe) {
            Log.e("AIChefClassifier", "Error loading labels: " + ioe.getMessage());
            return;
        }
        finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                Log.e("AIChefClassifier", "Error closing reader: " + ioe.getMessage());
            }
        }

        Thread classifierThread = new Thread(this);
        classifierThread.setDaemon(true);
        classifierThread.start();
    }


    @Override
    public void run() {

        while(true) {
            if(imgValid) {
                byte[][][] image = new byte[imgToClassify.getWidth()][imgToClassify.getHeight()][3];

                for(int x = 0; x < imgToClassify.getWidth(); x++) {
                    for(int y = 0; y < imgToClassify.getHeight(); y++) {
                        int colour = imgToClassify.getPixel(x, y);
                        image[x][y][0] = (byte) ((colour >> 16) & 0xff);
                        image[x][y][1] = (byte) ((colour >> 8) & 0xff);
                        image[x][y][2] = (byte) ((colour >> 0) & 0xff);
                    }
                }

                HashMap<String, Float> objProbs = calculateObjectProbabilities(image);

                float highestProb = 0;
                String highestKey = null;

                for(String key : objProbs.keySet()) {
                    if(objProbs.get(key) > highestProb) {
                        highestProb = objProbs.get(key);
                        highestKey = key;
                    }
                }

                Log.i("AIChefClassifier", highestKey + " : " + highestProb);
                //Toast.makeText(mainActivity, "Ingredient already in list", Toast.LENGTH_SHORT).show();

                String classification = highestProb >= MIN_OBJ_CHANCE ? highestKey : "NOT FOUND";

                mainActivity.validClassificationFound(classification);

                imgValid = false;
                canAcceptImg = true;
            }
        }

    }

    /**
     *
     * @param image The XxY RGB image
     * @return A mapping from each object to it's calculated probability
     */
    public HashMap<String, Float> calculateObjectProbabilities(byte[][][] image) {

        Log.i("AIChefClassifier", "Calculating obj probs for img of dims: " + image.length
                + "x" + image[0].length + "x" + image[0][0].length);

        // First crop image to a square
        int minDim = Math.min(image.length, image[0].length);

        byte[][][] croppedImage = new byte[minDim][minDim][3];

        // Already square
        if(image.length == image[0].length) {
            croppedImage = image;
        }
        // Short width
        else if(image.length == minDim) {
            int startY = (image[0].length - minDim) / 2;

            for(int x = 0; x < minDim; x++) {
                for(int y = 0; y < minDim; y++) {
                    for(int c = 0; c < 3; c++) {
                        croppedImage[x][y][c] = image[x][y + startY][c];
                    }
                }
            }
        }
        // Short height
        else {
            int startX = (image.length - minDim) / 2;

            for(int x = 0; x < minDim; x++) {
                for(int y = 0; y < minDim; y++) {
                    for(int c = 0; c < 3; c++) {
                        croppedImage[x][y][c] = image[x + startX][y][c];
                    }
                }
            }
        }

        // Scale the image to size
        Bitmap img = Bitmap.createBitmap(minDim, minDim, Bitmap.Config.ARGB_8888);

        for(int x = 0; x < minDim; x++) {
            for(int y = 0; y < minDim; y++) {
                int r = (int) croppedImage[x][y][0];

                int g = (int) croppedImage[x][y][1];
                int b = (int) croppedImage[x][y][2];

                if(r < 0) r += 255;
                if(g < 0) g += 255;
                if(b < 0) b += 255;

                img.setPixel(x, y, (0xff << 24)
                                    | (r << 16)
                                    | (g << 8)
                                    | b);
            }
        }

        Bitmap resizedImg = Bitmap.createScaledBitmap(img, INPUT_SIZE, INPUT_SIZE, false);


//        Dialog builder = new Dialog(activity);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(activity);
//        imageView.setImageBitmap(resizedImg);
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        builder.show();
//
//        Log.i("AIChefClassifier", "Showing popup");


//        try {
//            FileOutputStream out = new FileOutputStream(new File(context.getFilesDir(), "image_output.png"));
//            Log.i("AIChefClassifier", new File(context.getFilesDir(), "image_output_" + System.currentTimeMillis() + ".png").getAbsolutePath());
//            resizedImg.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("AIChefClassifier", "Error saving img");
//        }

        String s = "";

        float[][][][] inputImage = new float[1][INPUT_SIZE][INPUT_SIZE][3];
        for(int x = 0; x < INPUT_SIZE; x++) {
            for(int y = 0; y < INPUT_SIZE; y++) {
                int pixel = resizedImg.getPixel(x, y);
                inputImage[0][x][y][0] = ((pixel >> 16) & 0xff) / 255f;
                inputImage[0][x][y][1] = ((pixel >> 8) & 0xff) / 255f;
                inputImage[0][x][y][2] = ((pixel >> 0) & 0xff) / 255f;

                s += x + " " + y + " " + pixel + "\n";
            }
        }

        Log.i("AIChefClassifier", s);



        // Run network

        float[][] output = new float[1][numClasses];

        tfInterpreter.run(inputImage, output);


        HashMap<String, Float> classProbs = new HashMap<>();
        for(int i = 0; i < numClasses; i++)
            classProbs.put(classNames[i], output[0][i]);


        return classProbs;
    }

    /**
     *
     * @param bmp The XxYx3 RGB image
     * @return Return the class with the highest probability if it is above the threshold, otherwise return "NOT FOUND"
     * MIN_OBJ_CHANCE culled
     */
    public synchronized void classify(Bitmap bmp, Context context, MainActivity activity) {
        if(!canAcceptImg)
            throw new IllegalStateException("Cannot call classify when not accepting image");


        Log.i("AIChefClassifier", "Classifying img...");

        imgToClassify = bmp;
        canAcceptImg = false;
        imgValid = true;
    }

    public boolean canAcceptImage() {
        return canAcceptImg;
    }
}
