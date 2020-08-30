package com.lrned.mybrowser

import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

/*To handle when items are clicked within a webview.
Like when add any content for search in UI's edit view to keep That search in UI's webview
this class will help
 */
internal class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String
    ): Boolean {
        view.loadUrl(url)
        CookieManager.getInstance().setAcceptCookie(true)
        return true
    }
}