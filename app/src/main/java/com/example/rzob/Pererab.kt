package com.example.rzob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pererab.*
import java.math.BigDecimal

class Pererab : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null

//    val intent = intent
//    var day = intent.getStringExtra("Day")
//    var month = intent.getStringExtra("Month")
//    var year = intent.getStringExtra("Year")
//    val uid = FirebaseAuth.getInstance().uid
//    val pyt = "users/$uid/$year/$month/переработка/$day"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pererab)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)


        val intent = intent
        var day = intent.getStringExtra("Day")
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")
        val uid = FirebaseAuth.getInstance().uid

//        textView3.text = uid
        textView.text = "$day.$month.$year"

        prew_btn.setOnClickListener {
            finish()
        }


        save_btn.setOnClickListener {
//            textView3.text = hours.toString()
            save_data()
        }

        delete_btn.setOnClickListener {
            delete_data()
        }

        delete_btn.visibility = View.INVISIBLE


        var getdata_hours = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var data_hours15 = p0.child("Часы С Коэф 1,5").value
                if (data_hours15 == null) {
                    hours_edittext.setText("0.0")
                    Toast.makeText(applicationContext, "Часов переработки\n нет", Toast.LENGTH_SHORT).show()
                } else {
                    delete_btn.visibility = View.VISIBLE
                    var data_hours2 = p0.child("Часы С Коэф 2").value
                    if (data_hours2 == null){
                        val hours2 = "0.0"
                        val hours15 = data_hours15.toString()
                        val d_hours15 = hours15.toDouble()
                        val d_hours2 = hours2.toDouble()
                        var data_hours = d_hours15 + d_hours2
                        hours_edittext.setText("$data_hours")
                    }else{
                        var hours2 = data_hours2.toString()
                        val hours15 = data_hours15.toString()
                        val d_hours15 = hours15.toDouble()
                        val d_hours2 = hours2.toDouble()
                        var data_hours = d_hours15 + d_hours2
                        hours_edittext.setText("$data_hours")
                    }
                }
            }
        }
//        FirebaseDatabase.getInstance().getReference("users/$uid/$year/$month/$day")
//                .addValueEventListener(getdata_hours)
        FirebaseDatabase.getInstance().getReference(pyti())
                .addListenerForSingleValueEvent(getdata_hours)

    }

    fun pyti(): String {
        val intent = intent
        var day = intent.getStringExtra("Day")
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")
        val uid = FirebaseAuth.getInstance().uid
        val pyt = "users/$uid/$year/$month/переработка/$day"
        return pyt
    }

    fun delete_data(){
//        val intent = intent
//        var day = intent.getStringExtra("Day")
//        var month = intent.getStringExtra("Month")
//        var year = intent.getStringExtra("Year")
//        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().getReference(pyti())
//                .child("$year").child("$month").child("$day")
                .removeValue()
                .addOnSuccessListener {
                    hours_edittext.setText("0.0")
                    Toast.makeText(applicationContext, "Успешно удалено", Toast.LENGTH_SHORT).show()
                    delete_btn.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
    }

    fun save_data(){
//        val intent = intent
//        var day = intent.getStringExtra("Day")
//        var month = intent.getStringExtra("Month")
//        var year = intent.getStringExtra("Year")
//        val uid = FirebaseAuth.getInstance().uid
        val hours = hours_edittext.text
        if (hours.toString() != ""){
            val hours1 = hours.toString()
            var Hours1 = hours1.toDouble()
            if (Hours1 <= 2.0){
                val hours15 = hours1
                FirebaseDatabase.getInstance().getReference(pyti())
//                    .child("$year").child("$month").child("$day")
                        .child("Часы С Коэф 1,5").setValue(hours15)
                        .addOnSuccessListener {
//                            Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "Сохранено\nЧасы С Коэф 1,5:     $hours15", Toast.LENGTH_SHORT).show()
                            delete_btn.visibility = View.VISIBLE
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                        }
//                        textView3.text = hours15
            }
            if (Hours1 > 2.0){
                val hours15 = "2.0"
                val x : BigDecimal = BigDecimal(2)
                val hours2 = Hours1.toBigDecimal()
                val result = hours2.subtract(x)
//                        textView3.text = "$result"
                FirebaseDatabase.getInstance().getReference(pyti())
//                    .child("$year").child("$month").child("$day")
                        .child("Часы С Коэф 1,5").setValue(hours15)
                        .addOnSuccessListener {
//                            Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "Сохранено\nЧасы С Коэф 1,5:    $hours15", Toast.LENGTH_SHORT).show()
                            delete_btn.visibility = View.VISIBLE
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                        }
                FirebaseDatabase.getInstance().getReference(pyti())
//                    .child("$year").child("$month").child("$day")
                        .child("Часы С Коэф 2").setValue(result.toString())
                        .addOnSuccessListener {
//                            Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "Сохранено\nЧасы С Коэф 2:     $result", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                        }
            }
        }
    }
}