package com.example.smartwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.smartwallet.model.Payment
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var nameET : EditText
    private lateinit var costsET : EditText
    private lateinit var spinner: Spinner
    private lateinit var timestampTV: TextView
    private lateinit var saveBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        initComponents()
    }

    private fun initComponents(){
        nameET = findViewById(R.id.nameET)
        costsET = findViewById(R.id.costsET)
        spinner = findViewById(R.id.typeSpinner)
        timestampTV = findViewById(R.id.timestampET)
        saveBtn = findViewById(R.id.savePaymentBtn)
        saveBtn.setOnClickListener(this)
        timestampTV.text = getCurrentTime()
        val paymentTypes = listOf(
            "Entertainment",
            "Food",
            "Taxes",
            "Travel",
            "Else"
        )
        spinner.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,paymentTypes)
        spinner.setSelection(0)
    }

    private fun getCurrentTime() : String{
        val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val now = Date()
        return sdfDate.format(now)
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.savePaymentBtn -> addPayment()
        }
    }

    private fun addPayment(){
        if(nameET.text.isEmpty() || costsET.text.isEmpty() || timestampTV.text.isEmpty() ){
            Toast.makeText(this,"Please enter all fields",Toast.LENGTH_SHORT)
                .show()
        }
        else{
            val payment = Payment(
                timestampTV.text.toString(),
                costsET.text.toString().toDouble(),
                nameET.text.toString(),
                spinner.selectedItem.toString(),
            )
            val db = FirebaseDatabase.getInstance()
            val databaseReference = db.reference
            databaseReference
                .child("payments")
                .child("${payment.name}-${payment.timestamp}")
                .setValue(payment)
                .addOnSuccessListener {
                    startActivity(Intent(this@AddActivity,ListActivity::class.java))
                }
        }
    }
}