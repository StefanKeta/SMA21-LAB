package com.example.smartwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.smartwallet.model.MonthlyExpenses
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var databaseReference : DatabaseReference
    private lateinit var tStatus : TextView
    private lateinit var eSearch : EditText
    private lateinit var eIncome : EditText
    private lateinit var eExpenses  : EditText
    private lateinit var bSearch : Button
    private lateinit var bUpdate : Button
    private lateinit var currentMonth : String
    private lateinit var databaseListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents(){
        tStatus = findViewById(R.id.tStatus)
        eSearch = findViewById(R.id.eSearch)
        eIncome = findViewById(R.id.eIncome)
        eExpenses = findViewById(R.id.eExpenses)
        bSearch = findViewById(R.id.bSearch)
        bSearch.setOnClickListener(this)
        bUpdate = findViewById(R.id.bUpdate)
        bUpdate.setOnClickListener(this)


        val database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
    }

    private fun createNewDBListener() {
        // remove previous databaseListener
        databaseReference.child(
            "calendar"
        ).child(currentMonth).removeEventListener(databaseListener)
        databaseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val monthlyExpense = dataSnapshot.getValue(
                    MonthlyExpenses::class.java
                )
                // explicit mapping of month name from entry key
                monthlyExpense!!.month = dataSnapshot.key!!
                eIncome.setText(monthlyExpense.income.toString())
                eExpenses.setText(monthlyExpense.expenses.toString())
                tStatus.text = "Found entry for $currentMonth"
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        // set new databaseListener
        databaseReference.child("calendar").child(currentMonth)
            .addValueEventListener(databaseListener)
    }



    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.bSearch -> searchLogic()
            R.id.bUpdate -> updateLogic()
        }
    }

    private fun searchLogic(){
        if(eSearch.text.isEmpty()) Toast.makeText(this,"The field must be filled",Toast.LENGTH_SHORT)
            .show()
        else{
            currentMonth = eSearch.text.toString().lowercase()
            tStatus.text = "Searching..."
            createNewDBListener()
        }
    }

    private fun updateLogic(){
        if(eSearch.text.isEmpty() || eIncome.text.isEmpty() || eExpenses.text.isEmpty())
            Toast.makeText(this,"The fields must be filled",Toast.LENGTH_SHORT)
                .show()
        else{
            val monthlyExpenses = MonthlyExpenses(eSearch.text.toString(),eExpenses.text.toString().toInt(),eIncome.text.toString().toInt())
            databaseReference
                .child("calendar")
                .child(monthlyExpenses.month)
                .child("Expenses")
                .setValue(monthlyExpenses.expenses)

            databaseReference
                .child("calendar")
                .child(monthlyExpenses.month)
                .child("Income")
                .setValue(monthlyExpenses.income)
        }
    }
}