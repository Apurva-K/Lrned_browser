package com.lrned.mybrowser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*


/*
This class contains launch UI
Where search bar is custom designed
and spinner to select multiple items for deafult search engine.

Shared preferences is used to keep record of last select search engine and load
that search engine on launch
 */
class MainActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences
    val prefName = "searchEngine"
    private val setDefaulSearchEngine = "https://www.google.com/search?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val listSearchEngines = resources.getStringArray(R.array.search_Engines)
        WebAction()
        btnSwitchToChrome.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivityChrome::class.java)
            this.startActivity(intent)
            finish()
        }
        if (spSearchEngines != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, listSearchEngines
            )
            spSearchEngines.adapter = adapter
        }

        spSearchEngines.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val url : String
                when (pos) {
                    1 -> {
                        url = "https://www.google.com/search?q="
                        sharedPref.edit().putString(prefName, url).apply()
                        webView.loadUrl(url)
                    }
                    2 -> {
                        url = "https://www.bing.com/search?q="
                        sharedPref.edit().putString(prefName, url).apply()
                        webView.loadUrl(url)
                    }
                    3 -> {
                        url = "https://in.search.yahoo.com/search?p="
                        sharedPref.edit().putString(prefName, url).apply()
                        webView.loadUrl(url)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebAction() {
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(true)
        webView.settings.setSupportMultipleWindows(true)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.setBackgroundColor(Color.WHITE)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress_bar.progress = newProgress
                if (newProgress < 100 && progress_bar.visibility == View.GONE)
                    progress_bar.visibility = View.VISIBLE
                if (newProgress == 100)
                    progress_bar.visibility = View.GONE
                else
                    progress_bar.visibility = View.VISIBLE

            }
        }
        webView.webViewClient = MyWebViewClient()
        go_button.setOnClickListener {
            try {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    web_address_edit_text.getWindowToken(),
                    0
                )
                webView.loadUrl(
                    sharedPref.getString(
                        prefName,
                        setDefaulSearchEngine
                    ) + web_address_edit_text.text.toString()
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //Show default selected search engine
        webView.loadUrl(sharedPref.getString(prefName, "").toString())

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