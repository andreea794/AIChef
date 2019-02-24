package com.teamalpha.aichef.classifier;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.tensorflow.lite.Interpreter;

public class AIChefClassifier {

    private static final String MODEL_PATH = "graph.mp3";
    private static final String LABEL_PATH = "labels.mp3";

    private static final float MIN_OBJ_CHANCE = 0.5f;
    private static final int INPUT_SIZE = 224;

    private static int numClasses = 40;

    private static Interpreter tfInterpreter;
    private static String[] classNames;

    /**
     * Loads the NN from storage.
     * May take noticeable time so should be called once (and only once) at startup
     */

    public static void loadNetwork(AssetManager assetManager) {

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

        byte[][][] randImg = new byte[500][500][3];

        Random rand = new Random();

        for(int x = 0; x < 500; x++) {
            for(int y = 0; y < 500; y++) {
                for(int c = 0; c < 3; c++) {
                    randImg[x][y][c] = (byte) rand.nextInt(255);
                }
            }
        }

        byte[][][] img = new byte[224][224][3];

        try {
            Bitmap bmp = BitmapFactory.decodeStream(assetManager.open("image.jpg"));

            for(int x = 0; x < 224; x++) {
                for(int y = 0; y < 224; y++) {
                    int colour = bmp.getPixel(x, y);
                    img[x][y][0] = (byte)((colour >> 16) & 0xff);
                    img[x][y][1] = (byte)((colour >> 8) & 0xff);
                    img[x][y][2] = (byte)((colour >> 0) & 0xff);
                }
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

    }

    /**
     *
     * @param image The XxY RGB image
     * @return A mapping from each object to it's calculated probability
     */
    public static HashMap<String, Float> calculateObjectProbabilities(byte[][][] image) {

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

                img.setPixel(x, y, (0xff << 24)
                                    | (r << 16)
                                    | (g << 8)
                                    | b);
            }
        }

        Bitmap resizedImg = Bitmap.createScaledBitmap(img, INPUT_SIZE, INPUT_SIZE, false);

        float[][][][] inputImage = new float[1][INPUT_SIZE][INPUT_SIZE][3];
        for(int x = 0; x < INPUT_SIZE; x++) {
            for(int y = 0; y < INPUT_SIZE; y++) {
                int pixel = resizedImg.getPixel(x, y);
                inputImage[0][x][y][0] = ((pixel >> 16) & 0xff) / 255f;
                inputImage[0][x][y][1] = ((pixel >> 8) & 0xff) / 255f;
                inputImage[0][x][y][2] = ((pixel >> 0) & 0xff) / 255f;
            }
        }


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
     * @param image The XxYx3 RGB image
     * @return Return the class with the highest probability if it is above the threshold, otherwise return null
     * MIN_OBJ_CHANCE culled
     */
    public static String classify(byte[][][] image) {
        HashMap<String, Float> objProbs = calculateObjectProbabilities(image);

        float highestProb = 0;
        String highestKey = null;

        for(String key : objProbs.keySet()) {
            if(objProbs.get(key) > highestProb) {
                highestProb = objProbs.get(key);
                highestKey = key;
            }
        }

        return highestProb >= MIN_OBJ_CHANCE ? highestKey : null;
    }
}
