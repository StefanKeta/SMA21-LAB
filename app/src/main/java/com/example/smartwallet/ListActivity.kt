package com.example.smartwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button

import android.widget.ListView
import android.widget.TextView
import com.example.smartwallet.adapter.PaymentAdapter
import com.example.smartwallet.model.Payment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class ListActivity : AppCompatActivity(){

    private val payments = ArrayList<String>()
    private lateinit var listView: ListView
    private lateinit var tStatus: TextView
    private lateinit var bPrevious: Button
    private lateinit var bNext: Button
    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        payments.add("45.25")
        payments.add("5.50")
        payments.add("15.60")

        val payments = ArrayList<Payment>()

        tStatus = findViewById(R.id.tStatus)
        bPrevious = findViewById(R.id.previousBtn)
        bNext = findViewById(R.id.nextBtn)
        fab = findViewById(R.id.fab)
        listView = findViewById(R.id.listView)
        val adapter = PaymentAdapter(this, R.layout.payment_item, payments)
        listView.setAdapter(adapter);

        // setup firebase
        val db = FirebaseDatabase.getInstance();
        val databaseReference = db.getReference();

        databaseReference.child("wallet").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val addedPayment = snapshot.getValue(Payment::class.java)
                if (addedPayment != null) {
                    addedPayment.timestamp = snapshot.key.toString()
                    payments.add(addedPayment)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val payment = snapshot.getValue(Payment::class.java)
                if (payment != null) {
                    payments.remove(payment)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}