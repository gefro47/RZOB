package com.example.rzob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Raschet : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raschet)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)

        val uid = FirebaseAuth.getInstance().uid

        val intent = intent
        var day = intent.getStringExtra("Day")
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")

        var getdata_hours = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val sum15 = arrayListOf<Double>()
                val sum2 = arrayListOf<Double>()
                for (i in p0.child("$year/$month/переработка").children) {
                    var k15 = i.child("Часы С Коэф 1,5").value.toString().toDouble()
                    var k2 = i.child("Часы С Коэф 2").value
                    sum15.add(k15)
                    if (k2 == null) {
                        k2 = 0.0
                    } else {
                        k2 = i.child("Часы С Коэф 2").value.toString().toDouble()
                    }
                    sum2.add(k2)
                }
                val sum15end = sum15.sum()
                val sum2end = sum2.sum()
                if (sum15end == 0.0){
                    Toast.makeText(applicationContext, "Переработки нет!", Toast.LENGTH_SHORT).show()
                }
                var ZP = p0.child("Зарплата").value
                if (p0.child("Зарплата").value.toString() == "0" || ZP == null){
                    Toast.makeText(applicationContext, "Укажите зарплату\nв параметрах!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(getdata_hours)
    }
}