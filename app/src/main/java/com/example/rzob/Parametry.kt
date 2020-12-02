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
import kotlinx.android.synthetic.main.activity_parametry.*

class Parametry : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parametry)

        btn_delete.visibility = View.INVISIBLE

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)

        proverka()

        btn_delete.setOnClickListener {
            delete_data()
        }

        btn_save.setOnClickListener {
            savedata()
        }

        btn_prew.setOnClickListener {
            finish()
        }

    }

    fun savedata(){
        val ZP = edit_text_ZP.text
        FirebaseDatabase.getInstance().getReference(pyti())
                .child("Зарплата").setValue("$ZP")
                .addOnSuccessListener {
                    if (ZP.toString() != "0"){
                        Toast.makeText(applicationContext, "Сохранено\nЗарплата:     $ZP", Toast.LENGTH_SHORT).show()
                        btn_delete.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
    }

    fun proverka (){
        val getdata_ZP = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val ZP = p0.child("Зарплата").value
                if (ZP == null || ZP.toString() == "0"){
                    edit_text_ZP.setText("0")
                    Toast.makeText(applicationContext, "Зарплата\nне записана!", Toast.LENGTH_SHORT).show()
                }
                else{
                    btn_delete.visibility = View.VISIBLE
                    edit_text_ZP.setText("$ZP")
                }
            }
        }

        FirebaseDatabase.getInstance().getReference(pyti())
                .addListenerForSingleValueEvent(getdata_ZP)
    }

    fun delete_data(){
        FirebaseDatabase.getInstance().getReference(pyti())
                .child("Зарплата").removeValue()
                .addOnSuccessListener {
                    edit_text_ZP.setText("0")
                    Toast.makeText(applicationContext, "Успешно удалено", Toast.LENGTH_SHORT).show()
                    btn_delete.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
    }

    fun pyti(): String {
//        val intent = intent
//        var day = intent.getStringExtra("Day")
//        var month = intent.getStringExtra("Month")
//        var year = intent.getStringExtra("Year")
        val uid = FirebaseAuth.getInstance().uid
        val pyt = "users/$uid"
        return pyt
    }
}