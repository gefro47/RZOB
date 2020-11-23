package com.example.rzob

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pererab.*
import java.math.BigDecimal

class Pererab : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null






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


//        textView3.text = uid
        textView.text = "$day.$month.$year"

        prew_btn.setOnClickListener {
            finish()
        }


        save_btn.setOnClickListener {
//            textView3.text = hours.toString()
            save_data()
        }



    }
    fun save_data(){
        val intent = intent
        var day = intent.getStringExtra("Day")
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")
        val uid = FirebaseAuth.getInstance().uid
        val hours = hours_edittext.text
        if (hours.toString() != ""){
            val hours1 = hours.toString()
            var Hours1 = hours1.toDouble()
            if (Hours1 != null) {
                if (Hours1 <= 2.0){
                    val hours15 = hours1
                    FirebaseDatabase.getInstance().getReference("users/$uid")
                        .child("$year").child("$month").child("$day").child("Часы С Коэф 1,5").setValue(hours15)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
                                Toast.makeText(applicationContext, "Часы С Коэф 1,5:     $hours15", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                            }
//                        textView3.text = hours15
                }
            }

            if (Hours1 != null) {
                if (Hours1 > 2.0){
                    val hours15 = "2.0"
                    val x : BigDecimal = BigDecimal(2)
                    val hours2 = Hours1.toBigDecimal()
                    val result = hours2.subtract(x)
//                        textView3.text = "$result"
                    FirebaseDatabase.getInstance().getReference("users/$uid")
                        .child("$year").child("$month").child("$day").child("Часы С Коэф 1,5").setValue(hours15)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
                                Toast.makeText(applicationContext, "Часы С Коэф 1,5:    $hours15", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                            }
                    FirebaseDatabase.getInstance().getReference("users/$uid")
                        .child("$year").child("$month").child("$day").child("Часы С Коэф 2").setValue(result.toString())
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
                                Toast.makeText(applicationContext, "Часы С Коэф 2:     $result", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                            }
                }
            }
        }
    }
}