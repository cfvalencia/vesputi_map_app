# Vesputi Map App

Challenge App From Vesputi team (https://www.vesputi.com/). This app loads a list of locations from a given URL it thatn saves that data to a persistent room database. The app loads a map created using Mapbox SDK and displays the positions as Markers. Users are expected to click on the markers and a BottomSheetView should display specific marker information.

The main-Screen is a full screen map view with a few markers. 


# Requirements

- The map shall be based on the Mapbox-SDK.
- If one clicks on a marker, a new window opens.
- This new window is a card / Bottom-Sheet, sliding in from the bottom, like in the google maps app. 
- The new window contains the content of the respective marker. 
- You can take a json that we will use for this project anyways:
- https://midgard.netzmap.com/features.json?app_mode=swh-mein-halle-mobil
  (But take only the ones with "type“: “SimplePoi“)

# Libraries 

- Retrofit : https://square.github.io/retrofit/
- Material : https://material.io/
- Room : https://developer.android.com/jetpack/androidx/releases/room?gclid=CjwKCAjwkN6EBhBNEiwADVfya29LN9ghpsFwJbNWhrS3evvi_k93VmqD9XxwYKz61Lu6tzNHCGOcxRoCpEoQAvD_BwE&gclsrc=aw.ds
- MapBox : https://www.mapbox.com/
- Moshi: https://github.com/square/moshi

# MVVM Pattern  

- ![mvvm-architecture](https://user-images.githubusercontent.com/25370892/117573434-62ddf700-b09d-11eb-8336-dcf7ea9a16f4.png)


# Achievied Requierements

- Create map using Mapbox-SDK
- Add markers from URL
- On marker clicked: display marker specific info


# Improvements 

- Manage screen rotations 
- UI/UX is very basic and has the views as offered by default 
- Add Unit test
- Improve marker click  

# Instructions 
 
 - Users can clone the repo directly to their machine and build it using Android Studio 
 - Apk available inside the distribution folder 
 - The App displays markers obtained from the URL give, Users can click on markers and will see Marker specific info displayed in bottomSheetview.
 
