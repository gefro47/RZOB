package com.example.rzob

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_pererab.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null
    var p_day:String = ""
    var p_month:String = ""
    var p_year:String = ""
    var sob: Int = 0
    private var backPressedTime = 0L
    val uid = FirebaseAuth.getInstance().uid

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        }else {
            Toast.makeText(applicationContext, "Нажмите еще раз для выхода!", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_delete.visibility = View.INVISIBLE

        val fbd = SimpleDateFormat("d")
        val FBD = fbd.format(Date())
        val fbm = SimpleDateFormat("M")
        val FBM = fbm.format(Date())
        val fby = SimpleDateFormat("yyyy")
        val FBY = fby.format(Date())

        var gday: String = FBD
        var gmonth:String = FBM
        var gyear: String = FBY
        p_day = gday
        p_month = gmonth
        p_year = gyear
        DataPickView.text = gday + "." + gmonth + "." + gyear
        calendarView.setOnDateChangeListener{calendarView, year, month, day ->
            Toast.makeText(this@HomeActivity, "Selected date: $day.${month + 1}.$year", Toast.LENGTH_LONG).show()
            gday = "$day"
            p_day = gday
            gmonth = "${month + 1}"
            p_month = gmonth
            gyear = "$year"
            p_year = gyear
//            textView.text = "$day.${month + 1}.$year"
            DataPickView.text = gday + "." + gmonth + "." + gyear
            proverka()

        }


        pererab_btn.setOnClickListener{
            perenos_data()
        }

        btn_save_rab.setOnClickListener {
            savedata()
        }

        btn_delete.setOnClickListener {
            delete_data()
        }

        sign_out_button.setOnClickListener {
            signOut()
        }

        btn_vse_pererab.setOnClickListener {
            vsya_pererav_page_with_data()
        }

        parametr_btn.setOnClickListener {
            startActivity(Intent(this,Parametry::class.java))
        }


        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)


        val getdata_day = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data_rab_day = p0.child("Кол-во рабочих дней").value
                val data_otrab_day = p0.child("Кол-во отработанных дней").value
                if (data_rab_day == null || p0.child("Кол-во рабочих дней").value.toString() == "0") {
                    edit_text_rab_d.setText("0")
                    Toast.makeText(applicationContext, "Рабочие дни\nне записаны!", Toast.LENGTH_SHORT).show()
                    if (data_otrab_day == null || p0.child("Кол-во отработанных дней").value.toString() == "0") {
                        edit_text_otrab_d.setText("0")
                        Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        edit_text_otrab_d.setText("$data_otrab_day")
                        btn_delete.visibility = View.VISIBLE
                    }
                }
                else {
                    edit_text_rab_d.setText("$data_rab_day")
                    btn_delete.visibility = View.VISIBLE
                    if (data_otrab_day == null || p0.child("Кол-во отработанных дней").value.toString() == "0") {
                        edit_text_otrab_d.setText("0")
                        Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        edit_text_otrab_d.setText("$data_otrab_day")
                        btn_delete.visibility = View.VISIBLE
                    }
                }

            }
        }
//            FirebaseDatabase.getInstance().getReference("users/$uid/$p_year/$p_month")
//                    .addValueEventListener(getdata_day)
        FirebaseDatabase.getInstance().getReference("users/$uid/$p_year/$p_month")
                .addListenerForSingleValueEvent(getdata_day)


    }


    fun proverka (){
//        val uid = FirebaseAuth.getInstance().uid
        val s_month = p_month.toInt()
        if (s_month != sob) {
            val getdata_day = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val data_rab_day = p0.child("Кол-во рабочих дней").value
                    val data_otrab_day = p0.child("Кол-во отработанных дней").value
                    if (data_rab_day == null || p0.child("Кол-во рабочих дней").value.toString() == "0") {
                        edit_text_rab_d.setText("0")
                        Toast.makeText(applicationContext, "Рабочие дни\nне записаны!", Toast.LENGTH_SHORT).show()
                        if (data_otrab_day == null || p0.child("Кол-во отработанных дней").value.toString() == "0") {
                            edit_text_otrab_d.setText("0")
                            Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            edit_text_otrab_d.setText("$data_otrab_day")
                            btn_delete.visibility = View.VISIBLE
                        }
                    }
                    else {
                        edit_text_rab_d.setText("$data_rab_day")
                        btn_delete.visibility = View.VISIBLE
                        if (data_otrab_day == null || p0.child("Кол-во отработанных дней").value.toString() == "0") {
                            edit_text_otrab_d.setText("0")
                            Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            edit_text_otrab_d.setText("$data_otrab_day")
                            btn_delete.visibility = View.VISIBLE
                        }
                    }

                }
            }
//            FirebaseDatabase.getInstance().getReference("users/$uid/$p_year/$p_month")
//                    .addValueEventListener(getdata_day)
            FirebaseDatabase.getInstance().getReference("users/$uid/$p_year/$p_month")
                    .addListenerForSingleValueEvent(getdata_day)

            sob = s_month
        }
    }




    fun perenos_data(){
         val intent = Intent(this@HomeActivity, Pererab::class.java)
        intent.putExtra("Day", p_day)
        intent.putExtra("Month", p_month)
        intent.putExtra("Year", p_year)
        startActivity(intent)
    }

    fun vsya_pererav_page_with_data(){
        val intent = Intent(this@HomeActivity, VsyaPererab::class.java)
        intent.putExtra("Month", p_month)
        intent.putExtra("Year", p_year)
        startActivity(intent)
    }

    private fun signOut() {
        startActivity(MainActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut()
        googleSingInClient?.signOut()

        finish()
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }



    fun savedata(){
//        val uid = FirebaseAuth.getInstance().uid
        val rab_day = edit_text_rab_d.text
        val otrab_day = edit_text_otrab_d.text
        FirebaseDatabase.getInstance().getReference("users/$uid")
                .child(p_year).child("$p_month").child("Кол-во рабочих дней").setValue("$rab_day")
                .addOnSuccessListener {
                    if (rab_day.toString() != "0"){
                        Toast.makeText(applicationContext, "Сохранено\nРабочие дни:     $rab_day", Toast.LENGTH_SHORT).show()
                        btn_delete.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
        FirebaseDatabase.getInstance().getReference("users/$uid")
                .child(p_year).child("$p_month").child("Кол-во отработанных дней").setValue("$otrab_day")
                .addOnSuccessListener {
                    if (otrab_day.toString() != "0") {
                        Toast.makeText(applicationContext, "Сохранено\nОтработанные дни:     $otrab_day", Toast.LENGTH_SHORT).show()
                        btn_delete.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
    }

    fun delete_data(){
//        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().getReference("users/$uid")
                .child("$p_year").child("$p_month").child("Кол-во рабочих дней").removeValue()
                .addOnSuccessListener {
                    edit_text_rab_d.setText("0")
                    Toast.makeText(applicationContext, "Успешно удалено", Toast.LENGTH_SHORT).show()
                    btn_delete.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
        FirebaseDatabase.getInstance().getReference("users/$uid")
                .child("$p_year").child("$p_month").child("Кол-во отработанных дней").removeValue()
                .addOnSuccessListener {
                    edit_text_otrab_d.setText("0")
                    Toast.makeText(applicationContext, "Успешно удалено", Toast.LENGTH_SHORT).show()
                    btn_delete.visibility = View.INVISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                }
    }
}