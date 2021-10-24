package com.example.lab4app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Button


class WebsearchActivity : AppCompatActivity() , View.OnClickListener {
    private lateinit var webView: WebView
    private lateinit var bBtn : Button
    private lateinit var fBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_websearch)
        webView = findViewById(R.id.webview)
        bBtn = findViewById(R.id.bLoadBackground)
        fBtn = findViewById(R.id.bLoadForeground)
        bBtn.setOnClickListener(this)
        fBtn.setOnClickListener(this)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.google.com/search?q=cat&tbm=isch&source=lnms&sa=X")
    }


    private fun getClipboardURL(): String {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val abc: ClipData? = clipboard.primaryClip
        val item = abc!!.getItemAt(0)
        return item.text.toString()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bLoadBackground -> loadBackground(getClipboardURL())
            R.id.bLoadForeground -> loadForeground(getClipboardURL())
        }
    }

    private fun loadBackground(url: String) {
        val intent = Intent(this@WebsearchActivity, ImageIntentService::class.java)
        intent.putExtra(EXTRA_URL, url)
        startService(intent)
    }

    private fun loadForeground(url: String) {
        val intent = Intent(this@WebsearchActivity, ImageIntentService::class.java)
        intent.action = ForegroundImageService.STARTFOREGROUND_ACTION
        intent.putExtra(EXTRA_URL, url)
        startService(intent)
    }

 companion object{
     const val EXTRA_URL = "url"
 }
}

