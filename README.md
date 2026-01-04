# Android WebView FCM Sync

[![](https://jitpack.io/v/mgks/android-webview-fcm-sync.svg)](https://jitpack.io/#mgks/android-webview-fcm-sync)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A lightweight utility to synchronize Firebase Cloud Messaging (FCM) tokens into Android WebView Cookies. This allows your server to identify the web session with the specific Android device for targeted push notifications.

Extracted from the core of **[Android Smart WebView](https://github.com/mgks/Android-SmartWebView)**.

<img src="https://github.com/mgks/android-webview-fcm-sync/blob/main/preview.gif?raw=true" width="200">

When users log in via a WebView, your server creates a web session but has no idea which Android device it belongs to, so you cannot reliably send push notifications later. This library fixes that gap by fetching the native FCM token and injecting it as a secure cookie, allowing your server to read it on page load and cleanly link the web session ID to the correct FCM device token.

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
MIT

> **{ github.com/mgks }**
> 
> ![Website Badge](https://img.shields.io/badge/Visit-mgks.dev-blue?style=flat&link=https%3A%2F%2Fmgks.dev) ![Sponsor Badge](https://img.shields.io/badge/%20%20Become%20a%20Sponsor%20%20-red?style=flat&logo=github&link=https%3A%2F%2Fgithub.com%2Fsponsors%2Fmgks)
