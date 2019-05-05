Deeplocal and Google partnered for I/O 2019 to build a sample Android app for a S’mores delivery service that demonstrates two new complementary features in the Assistant ecosystem - App Actions and Slices. App Actions is a feature that navigates users directly into key areas of the app as a response to voice queries. Slices allows app developers to serve a custom UI in response to a voice query.

## Steps to Set Up and Run

1. To get started, clone the repository and import into Android Studio
2. Set up a new Firebase project
3. Add an Android configuration to your Firebase project, download the resulting google-services.json file, and copy to Smores/app/
4. Update cloud-fxn/.firebaserc with your project ID
5. Deploy the Firebase function from the command line with `$ firebase deploy --only functions`
6. Everything should now be up and running! Test the following functions:
  * Order via the standard in-app ordering flow
  * Order via a voice query in the Google Search app
  * Check the last order’s status via a voice query

## Relevant App Actions Snippets

AndroidManifest.xml
  * Inside of the `<activity>` tag, there are a few intent filters as a result of implementing [deep links](https://developer.android.com/training/app-links/deep-linking)
  * Inside of the `<application>` tag, there is a `<meta-data>` tag exposing the actions.xml file
  * Defines intents that this app can handle along with any required parameters

java/com/deeplocal/smores/MainActivity.java
  * In `onCreate()`, there is a check for extra parameters in the intent to determine whether to start normally or if it was launched via App Actions and should populate an order and show the order review screen


For more info, see the [Android Developers App Actions Guide](https://developer.android.com/guide/actions)

## Relevant Slices Snippets

AndroidManifest.xml
  * Inside of the `<application>` tag, there is a Slice provider and broadcast receiver registered
java/com/deeplocal/smores/MainActivity.java
  * In `onCreate()`, there is a check for extra parameters in the intent to determine whether to start normally or if it was launched via a Slice and should show the past orders screen

java/com/deeplocal/smores/SmoresSliceProvider.java
  * Implements a `SliceProvider` to set up the custom UI to be shown within the Google Search app

java/com/deeplocal/smores/SliceBroadcastReceiver.java
  * Implements a `BroadcastReceiver` that allows the app to dynamically update the UI of a Slice


For more info, see the [Android Developers Slices Guide](https://developer.android.com/guide/slices)
