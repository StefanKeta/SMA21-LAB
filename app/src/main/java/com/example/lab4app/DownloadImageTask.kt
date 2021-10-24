package com.example.lab4app

import android.content.Context
import android.content.Intent

import android.graphics.Bitmap
import android.os.AsyncTask
import android.graphics.BitmapFactory
import java.io.InputStream
import java.lang.Exception
import java.net.URL


class DownloadImageTask (val context: Context): AsyncTask<String,Void,Bitmap>(){

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val imageURL = urls[0]!!
        var bimage: Bitmap? = null
        try {
            val input: InputStream = URL(imageURL).openStream()
            bimage = BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bimage
    }

    override fun onPostExecute(result: Bitmap?) {
        // save bitmap result in application class
        val application = MyApplication().instance
        application.bitmap = result
        // send intent to stop foreground service
        context.startActivity(Intent(context, ImageActivity::class.java))
    }
}