package com.example.smartwallet

import android.content.Context
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.widget.Toast
import com.example.smartwallet.model.Payment
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*
import kotlin.collections.ArrayList


object AppState {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun updateLocalBackup(context:Context, payment:Payment, toAdd:Boolean) {
        val fileName = payment.timestamp;

        try {
            if (toAdd) {
                // save to file
            } else {
                context.deleteFile(fileName);
            }
        } catch (e:IOException) {
            Toast.makeText(context, "Cannot access local data.", Toast.LENGTH_SHORT).show();
        }
    }

    fun hasLocalStorage( context:Context):Boolean {
        return context.filesDir.listFiles()!!.isNotEmpty();
    }

    fun loadFromLocalBackup(context: Context): ArrayList<Payment> {
        try {
            val payments = ArrayList<Payment>()
            for (file in Objects.requireNonNull(context.filesDir.listFiles())) {
                run {
                    val fileInputStream = context.openFileInput(file.name)
                    val inputStream = ObjectInputStream(fileInputStream)
                    val payment = inputStream.readObject() as Payment
                    payments.add(payment)
                    inputStream.close()
                    fileInputStream.close()
                }
            }
            return payments
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot access local data.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return ArrayList<Payment>()
    }

}