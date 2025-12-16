# Android WebView FCM Sync

[![](https://jitpack.io/v/mgks/android-webview-fcm-sync.svg)](https://jitpack.io/#mgks/android-webview-fcm-sync)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A lightweight utility to synchronize Firebase Cloud Messaging (FCM) tokens into Android WebView Cookies. This allows your server to identify the web session with the specific Android device for targeted push notifications.

Extracted from the core of **[Android Smart WebView](https://github.com/mgks/Android-SmartWebView)**.

<img src="https://github.com/mgks/android-webview-fcm-sync/blob/main/preview.gif?raw=true" width="200">

## The Problem
When a user logs into your website inside a WebView, your server creates a web session. However, your server doesn't know *which* Android device ID (FCM Token) belongs to that web session.

## The Solution
This library fetches the native FCM token and injects it as a secure Cookie (`FCM_TOKEN=...`). When the WebView loads your website, your server reads this cookie and links the Web Session ID to the FCM Device Token.

## Installation

**Step 1. Add JitPack**
```groovy
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

**Step 2. Add Dependency**
```groovy
dependencies {
    implementation 'com.github.mgks:android-webview-fcm-sync:1.0.0'
    implementation 'com.google.firebase:firebase-messaging:23.4.0' // Required
}
```

## Setup Requirements

Because this library relies on Firebase, your App must be configured for Firebase:

1.  Add `google-services.json` to your `app/` folder.
2.  Apply the Google Services plugin in your `app/build.gradle`:
    ```groovy
    plugins {
        id 'com.android.application'
        id 'com.google.gms.google-services'
    }
    ```

## Usage

```kotlin
val fcmSync = SwvFcmSync()
val myUrl = "https://my-website.com"

// Call this BEFORE loading the URL (e.g. in onCreate)
fcmSync.sync(myUrl) { token ->
    if (token != null) {
        // Token is now in the CookieManager
        // Safe to load the page now
        webView.loadUrl(myUrl)
    }
}
```

### Configuration
```kotlin
val config = SwvFcmSync.Config(
    cookieName = "PUSH_DEVICE_ID", // Default: FCM_TOKEN
    syncToConsole = true           // Log actions to Logcat
)
fcmSync.sync(url, config)
```

## License
MIT License