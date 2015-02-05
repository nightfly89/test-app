# test-app
Interview Android Test App

  Test App was developed using Android Studio, which is being recommended by Google and marketed as the official 
IDE for Android development (http://developer.android.com/tools/studio/index.html).
  This work environment has been chosen because of its specific for the platform and the Gradle build system which can handle
Maven Central repositories easily.
  The four libraries added through Gradle were the Android Support Library, Volley and OkHttp for the web request and
GSON for deserializing the incoming JSON swiftly.
  The design of the activity has been slightly modified to be Android specific, by replacing the iOS inspired search field with
the native search in ActionBar. As no information was provided for the search, it simply shows a pop-up with the persons matching
the inputted query.
  A custom Api module has been created, to handle the Volley initialization, Request management and Response deserialization 
using GSON.
