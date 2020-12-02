package com.example.rzob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_vsya_pererab.*
import java.lang.StringBuilder

class VsyaPererab : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vsya_pererab)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)

        val intent = intent
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")

        date_text_view.text = "$month.$year"

        prew_btn.setOnClickListener {
            finish()
        }


        var getdata_hours = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var sb = StringBuilder()
                val sum15 = arrayListOf<Double>()
                val sum2 = arrayListOf<Double>()
                for (i in p0.children){
//                    var k15 = i.child("Часы С Коэф 1,5").value
//                    var k2 = i.child("Часы С Коэф 2").value
                    var k15 = i.child("Часы С Коэф 1,5").value.toString().toDouble()
                    var k2 = i.child("Часы С Коэф 2").value
                    sum15.add(k15)
                    if (k2 == null) {
                        k2 = 0.0
                    }
                    else{
                        k2 = i.child("Часы С Коэф 2").value.toString().toDouble()
                    }
                    sum2.add(k2)
                    sb.append("${i.key}          K1,5:  $k15        K2:  $k2\n")
                }
                val sum15end = sum15.sum()
                val sum2end = sum2.sum()
                textView8.text = "$sum15end     $sum2end"
                data_text_view.setText(sb)
            }
        }

        val uid = FirebaseAuth.getInstance().uid

        FirebaseDatabase.getInstance().getReference("users/$uid/$year/$month/переработка")
                .addListenerForSingleValueEvent(getdata_hours)
    }
}