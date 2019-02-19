package com.teamalpha.aichef.classifier;

import java.nio.ByteBuffer;
import java.util.HashMap;


public class AIChefClassifier {

    private static final float MIN_OBJ_CHANCE = 0.5f;

    /**
     * Loads the NN from storage.
     * May take noticeable time so should be called once (and only once) at startup
     */

    public static void loadNetwork() {

    }

    /**
     *
     * @param image The XxY RGB image
     * @return A mapping from each object to it's calculated probability
     */
    public static HashMap<String, Float> calculateObjectProbabilities(ByteBuffer image) {
        return null;
    }

    /**
     *
     * @param image The XxY RGB image
     * @return Essentially, calculatedObjectProbabilities with objects with probability less than
     * MIN_OBJ_CHANCE culled
     */
    public static String getObjectsInImage(ByteBuffer image) {
        HashMap<String, Float> objProbs = calculateObjectProbabilities(image);
        for(String key : objProbs.keySet()) {
            if(objProbs.get(key) < MIN_OBJ_CHANCE) {
                objProbs.remove(key);
            }
        }

        if(objProbs.size() == 0)
            return "NOT FOUND";

        float highestProb = 0;
        String highestKey = null;

        for(String key : objProbs.keySet()) {
            if(objProbs.get(key) > highestProb) {
                highestProb = objProbs.get(key);
                highestKey = key;
            }
        }

        return highestKey;
    }
}
