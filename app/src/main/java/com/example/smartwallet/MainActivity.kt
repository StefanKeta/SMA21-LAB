package com.example.smartwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.smartwallet.model.MonthlyExpenses
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tStatus: TextView
    private lateinit var eSearch: EditText
    private lateinit var eIncome: EditText
    private lateinit var eExpenses: EditText
    private lateinit var bUpdate: Button
    private lateinit var spinner: Spinner
    private lateinit var currentMonth: String
    private lateinit var databaseListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        val database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        tStatus = findViewById(R.id.tStatus)
        eIncome = findViewById(R.id.eIncome)
        eExpenses = findViewById(R.id.eExpenses)
        bUpdate = findViewById(R.id.bUpdate)
        bUpdate.setOnClickListener(this)
        spinner = findViewById(R.id.itemsSpinner)
        populateSpinner()
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    databaseReference.child("calendar")
                        .child("February")
                        .child("Income")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                eIncome.setText(snapshot.value.toString())
                                spinner.setSelection(0)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    databaseReference.child("calendar")
                        .child("February")
                        .child("Expenses")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                eExpenses.setText(snapshot.value.toString())
                                spinner.setSelection(0)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                } else if (position == 1) {
                    databaseReference.child("calendar")
                        .child("January")
                        .child("Income")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                eIncome.setText(snapshot.value.toString())
                                spinner.setSelection(1)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    databaseReference.child("calendar")
                        .child("January")
                        .child("Expenses")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                eExpenses.setText(snapshot.value.toString())
                                spinner.setSelection(1)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }

    private fun populateSpinner() {
        databaseReference
            .child("calendar")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val months = ArrayList<String>()
                    for (data in snapshot.children) {
                        months.add(data.key.toString())
                    }
                    spinner.adapter = ArrayAdapter<String>(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        months
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

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
        when (v!!.id) {
            R.id.bUpdate -> updateLogic()
        }
    }

    private fun updateLogic() {
        currentMonth = spinner.selectedItem.toString()
        val selectedItem = spinner.selectedItemPosition
        val monthlyExpenses = MonthlyExpenses(
            currentMonth,
            eExpenses.text.toString().toDouble(),
            eIncome.text.toString().toDouble()
        )
        databaseReference.child("calendar")
            .child(monthlyExpenses.month)
            .child("Income")
            .setValue(monthlyExpenses.income)

        databaseReference.child("calendar")
            .child(monthlyExpenses.month)
            .child("Expenses")
            .setValue(monthlyExpenses.expenses)
    }
}