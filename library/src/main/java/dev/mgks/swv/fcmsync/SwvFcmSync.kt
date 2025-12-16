package dev.mgks.swv.fcmsync

import android.util.Log
import android.webkit.CookieManager
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.IllegalStateException

/**
 * Smart WebView FCM Sync.
 * Bridges the gap between Native Firebase tokens and the WebView session.
 */
class SwvFcmSync {

    data class Config(
        var cookieName: String = "FCM_TOKEN",
        var syncToConsole: Boolean = true
    )

    fun sync(url: String, config: Config = Config(), onComplete: ((String?) -> Unit)? = null) {
        try {
            // SAFE CALL: Try to get the instance. If Firebase isn't set up, this throws.
            val firebase = FirebaseMessaging.getInstance()

            firebase.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    if (config.syncToConsole) Log.w("SwvFcmSync", "Fetching FCM registration token failed", task.exception)
                    onComplete?.invoke(null)
                    return@addOnCompleteListener
                }

                val token = task.result
                if (config.syncToConsole) Log.d("SwvFcmSync", "FCM Token: $token")

                setCookie(url, config.cookieName, token)
                onComplete?.invoke(token)
            }
        } catch (e: IllegalStateException) {
            // CATCH THE CRASH: Handle missing google-services.json
            if (config.syncToConsole) {
                Log.e("SwvFcmSync", "Firebase is not initialized! Ensure google-services.json is present.", e)
            }
            onComplete?.invoke(null) // Return null so the app knows it failed
        } catch (e: Exception) {
            if (config.syncToConsole) Log.e("SwvFcmSync", "Unknown error during sync", e)
            onComplete?.invoke(null)
        }
    }

    private fun setCookie(url: String, cookieName: String, value: String) {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val cookieString = "$cookieName=$value; Path=/; Secure; SameSite=Strict"
        cookieManager.setCookie(url, cookieString)
        cookieManager.flush()
    }
}