# AI Chef

AI Chef is a demo Android application that uses some simple image classification to identify vegetables and recommend recipes based on the scanned ingredients. The application was developed for the Group Project course in Part 1B of the Computer Science Tripos at the University of Cambridge. 

# Prerequisites

The project aimed to use ARM's Compute Library and in doing so utilized the ARM NN platform. TensorFlow Lite runs on top of several abstraction layers for ARM NN, and so to fully utilize ARM NN you need to ideally use an Android smartphone with a Mali GPU or at least Cortex-A CPU's.  

# Installation 

1. Create a RapiAPI account by subscribing to any of the plans listed at https://rapidapi.com/spoonacular/api/recipe-food-nutrition/pricing. 
2. Import the repository into Android Studio. 
3. Paste your RapidAPI account key in the strings.xml file in the string element named API_KEY.  
4. Connect an Android smartphone with USB debugging enabled and launch the application on the target device.

# Usage 

1. Aim the camera at a vegetable ingredient to scan. 
2. Press and hold the scan button on the main page to begin scanning. 
3. If scanning is successful, a dialog will appear asking for confirmation to add said ingredient to a list of scanned ingredients. 
4. Repeat steps 1 to 3 to add several ingredients to the scanned list. 
5. Use the slide-up panel on the main page to manage scanned ingredients and invoke the recipe API to gather recipes. 

A detailed overview of the suggested recipes can be found in the page reachable via the 'RECIPES LIST' navigation button. A cumulative shopping list of ingredients for the selected recipes is available via the 'SHOPPING LIST' navigation button in the recipe overview page. 

