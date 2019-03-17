# EazyTime App
Simple time tracking app for Android in Kotlin.

## What is it?

Android-App to detect when I arrived or left the office. In addition, I can manage my working hours manually and assign different projects. The app allows me to view my working hours of the past few days.


## Technologies
- Android SDK v28 (min v21)
- Kotlin 1.3.11
- Dagger 2.20

## Getting Started

The app is not released yet, so you have to build a debug apk. 
Download the app by cloning this repository and use the "gradlew installDebug" command to build and install the project directly on your connected device or running emulator.

### GoogleMap and Places API

The app uses Google Maps and Places API for the geofence-feature. If you want to use this feature, you need to get your own API-Key from [Google Cloud Platforms](https://cloud.google.com/maps-platform/).
Make sure to register your API key for **Maps SDK for Android** and **Places API**.

Add the key to the resource-file 'google_api.xml' by replacing YOUR_API_KEY with the value of your API key.
'''
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="google_api_key" translatable="false" templateMergeStrategy="preserve">
        YOUR_API_KEY
    </string>
</resources>
'''

### Connect your device 

Follow these steps to connect your device:

1. Connect your device by USB
2. Enable 'Developer options > USB debugging' on your device (Developer options is hidden by default. To make it available, go to 'Settings > About phone' and tap 'Build number' seven times)
3. Now you should see your device with `adb device -l` (if not use `adb usb` to activate usb-connection)
> optional connection by wifi
4. Activate Wifi-Connection
5. `adb tcpip 5555`
6. `adb connect <XXX.XXX.X.XXX>:5555` > add the ip-adress of your device ('Settings > About phone > Status')
7. Enjoy
