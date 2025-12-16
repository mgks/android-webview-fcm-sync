package dev.mgks.swv.sample

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.mgks.swv.fcmsync.SwvFcmSync

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview)
        val statusText = findViewById<TextView>(R.id.text_status)
        val btnSync = findViewById<Button>(R.id.btn_test)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient() // Ensure links open in WebView

        // 1. Load a test page with a "Check" button
        val verificationUrl = "https://www.whatismybrowser.com/detect/are-cookies-enabled"
        val html = """
            <html>
            <body style='padding: 20px; font-family: sans-serif; text-align: center;'>
                <h2>FCM Sync Test</h2>
                <p>1. Click 'Sync FCM Token' button below.</p>
                <p>2. Then click the link below to verify the cookie was set.</p>
                <br>
                <a href='$verificationUrl' style='padding: 15px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; display: inline-block;'>
                   Verify Cookies 🍪
                </a>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL("https://www.whatismybrowser.com", html, "text/html", "utf-8", null)

        // 2. Sync Logic (No Auto-Reload)
        btnSync.setOnClickListener {
            statusText.text = "Syncing..."

            val syncer = SwvFcmSync()

            // We sync to the domain of the verification URL
            syncer.sync(verificationUrl) { token ->
                if (token != null) {
                    statusText.text = "Success! Token synced to CookieManager.\nToken: "+token+""
                } else {
                    statusText.text = "Sync Failed. Check Logcat."
                }
            }
        }
    }
}