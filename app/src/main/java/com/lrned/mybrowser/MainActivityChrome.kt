package com.lrned.mybrowser

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

//This class has properties to set tool bar as in chrome and use all the features provided by chrome in our custom web browser
class MainActivityChrome : AppCompatActivity() {

    private var customTabHelper: CustomTabHelper = CustomTabHelper()
    private val Url = "https://www.google.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chrome)
        val defaultIntent = intent
        if (defaultIntent != null) {
            val uri: Uri? = defaultIntent.data
            if (uri != null) {
                if (TextUtils.isEmpty(uri.path) || uri.path.equals("/")) {
                    WebAction(Url)
                } else {
                    WebAction(uri.path.toString())
                }
            } else
                WebAction(Url)
        } else
            WebAction(Url)

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun WebAction(url: String) {
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(true)
        webView.settings.setSupportMultipleWindows(true)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.setBackgroundColor(Color.WHITE)
        webView.loadUrl(url)
        TabActions(url)
    }


    //To modify view of top bar using library
    fun TabActions(url: String) {
        val builder = CustomTabsIntent.Builder()
        // modify toolbar color
        builder.setToolbarColor(
            ContextCompat.getColor(
                this@MainActivityChrome,
                R.color.colorPrimary
            )
        )
        // add share button to overflow menu
        builder.addDefaultShareMenuItem()
        val anotherCustomTab = CustomTabsIntent.Builder().build()
        val requestCode = 100
        val intent = anotherCustomTab.intent
        intent.setData(Uri.parse(url))
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // add menu item to oveflow
        builder.addMenuItem("Sample item", pendingIntent)
        // show website title
        builder.setShowTitle(true)

        // animation for enter and exit of tab
        builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()
        // check is chrom available
        val packageName = customTabHelper.getPackageNameToUse(this, url)
        if (packageName == null) {
            // if chrome not available open in web view
            WebAction(Uri.parse(url).toString())
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }
}