package com.example.rzob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import java.text.DecimalFormat

class VsyaPererab : AppCompatActivity() {

    var googleSingInClient: GoogleSignInClient? = null

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


//        var getdata_hours = object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                var sb = StringBuilder()
//                val sum15 = arrayListOf<Double>()
//                val sum2 = arrayListOf<Double>()
//                for (i in p0.children) {
////                    var k15 = i.child("Часы С Коэф 1,5").value
////                    var k2 = i.child("Часы С Коэф 2").value
//                    var k15 = i.child("Часы С Коэф 1,5").value.toString().toDouble()
//                    var k2 = i.child("Часы С Коэф 2").value
//                    sum15.add(k15)
//                    if (k2 == null) {
//                        k2 = 0.0
//                    } else {
//                        k2 = i.child("Часы С Коэф 2").value.toString().toDouble()
//                    }
//                    sum2.add(k2)
//                    sb.append("${i.key}          K1,5:  $k15        K2:  $k2\n")
//                }
//                val sum15end = sum15.sum()
//                val sum2end = sum2.sum()
//                textView8.text = "$sum15end     $sum2end"
//                data_text_view.setText(sb)
//            }
//        }

        val getdata_ZP = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val ZP = p0.child("$year").child("$month").child("Зарплата").value
                if (ZP == null || ZP.toString() == "0"){
                    ZP_textView.setText("0")
//                    Toast.makeText(applicationContext, "В параметрах\nне указана зарплата!", Toast.LENGTH_SHORT).show()
                    AlertDialog0()
                }
                else{
                    if (ZP != p0.child("Зарплата").value){
                        AlertDialog()
                        ZP_textView.setText("$ZP")
                    }else{
                        ZP_textView.setText("$ZP")
                    }
                }



                var sb = StringBuilder()
                val sum15 = arrayListOf<Double>()
                val sum2 = arrayListOf<Double>()
                for (i in p0.child("$year").child("$month").child("переработка").children) {
//                    var k15 = i.child("Часы С Коэф 1,5").value
//                    var k2 = i.child("Часы С Коэф 2").value
                    var k15 = i.child("Часы С Коэф 1,5").value.toString().toDouble()
                    var k2 = i.child("Часы С Коэф 2").value
                    sum15.add(k15)
                    if (k2 == null) {
                        k2 = 0.0
                    } else {
                        k2 = i.child("Часы С Коэф 2").value.toString().toDouble()
                    }
                    sum2.add(k2)
                    sb.append("${i.key}          K1,5:  $k15        K2:  $k2\n")
                }
                val sum15end = sum15.sum()
                val sum2end = sum2.sum()
                text_k15.text = "$sum15end"
                text_k2.text = "$sum2end"
                data_text_view.setText(sb)



                val data_rab_day = p0.child("$year").child("$month").child("Кол-во рабочих дней").value
                val data_otrab_day = p0.child("$year").child("$month").child("Кол-во отработанных дней").value
                if (data_rab_day == null || p0.child("$year").child("$month").child("Кол-во рабочих дней").value.toString() == "0") {
                    text_rab_d.setText("0")
                    Toast.makeText(applicationContext, "Рабочие дни\nне записаны!", Toast.LENGTH_SHORT).show()
                    if (data_otrab_day == null || p0.child("$year").child("$month").child("Кол-во отработанных дней").value.toString() == "0") {
                        text_otrab_d.setText("0")
                        Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        text_otrab_d.setText("$data_otrab_day")
                    }
                }
                else {
                    text_rab_d.setText("$data_rab_day")
                    if (data_otrab_day == null || p0.child("$year").child("$month").child("Кол-во отработанных дней").value.toString() == "0") {
                        text_otrab_d.setText("0")
                        Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        text_otrab_d.setText("$data_otrab_day")
                    }
                }
                val x_ZP = ZP_textView.text.toString().toDouble()
                val x_rab_d = text_rab_d.text.toString().toDouble()
                val x_otrab_d = text_otrab_d.text.toString().toDouble()
                val x_sum_15 = text_k15.text.toString().toDouble()
                val x_sum_2 = text_k2.text.toString().toDouble()

                var raschet1 = x_ZP*0.87/x_rab_d*x_otrab_d
                var raschet2 = x_ZP*0.87/x_rab_d/8.0*x_sum_15*1.5
                var raschet3 = x_ZP*0.87/x_rab_d/8.0*x_sum_2*2.0
                var raschet123 = raschet1 + raschet2 + raschet3
                var raschet_d = DecimalFormat("#######.##")
                text_raschet.text = "${raschet_d.format(raschet123)}"


            }
        }

//        val getdata_day = object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val data_rab_day = p0.child("Кол-во рабочих дней").value
//                val data_otrab_day = p0.child("Кол-во отработанных дней").value
//                if (data_rab_day == null || p0.child("Кол-во рабочих дней").value.toString() == "0") {
//                    text_rab_d.setText("0")
//                    Toast.makeText(applicationContext, "Рабочие дни\nне записаны!", Toast.LENGTH_SHORT).show()
//                    if (data_otrab_day == null || p0.child("Кол-во отработанных дней").value.toString() == "0") {
//                        text_otrab_d.setText("0")
//                        Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
//                    }
//                    else {
//                        text_otrab_d.setText("$data_otrab_day")
//                    }
//                }
//                else {
//                    text_rab_d.setText("$data_rab_day")
//                    x_day_rab = text_rab_d.text.toString().toDouble()
//                    if (data_otrab_day == null || p0.child("Кол-во отработанных дней").value.toString() == "0") {
//                        text_otrab_d.setText("0")
//                        Toast.makeText(applicationContext, "Отработанные дни\nне записаны!", Toast.LENGTH_SHORT).show()
//                    }
//                    else {
//                        text_otrab_d.setText("$data_otrab_day")
//                    }
//                }
//            }
//        }



        val uid = FirebaseAuth.getInstance().uid

        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(getdata_ZP)

//        FirebaseDatabase.getInstance().getReference("users/$uid/$year/$month")
//            .addListenerForSingleValueEvent(getdata_day)
//
//        FirebaseDatabase.getInstance().getReference(pyti())
//            .addListenerForSingleValueEvent(getdata_hours)


    }


    fun AlertDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("З/п в этом месяце отличается от з/п указанной в параметрах")
        builder.setMessage("Записать новую з/п в этот месяц?")
        builder.setPositiveButton("Да"){dialog, i ->
            saveZP()
        }
        builder.setNegativeButton("Нет"){dialog, i ->

        }
        builder.show()
    }

    fun AlertDialog0(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("В этом месяце не указана з/п")
        builder.setMessage("Записать з/п в этот месяц?")
        builder.setPositiveButton("Да"){dialog, i ->
            saveZP()
        }
        builder.setNegativeButton("Нет"){dialog, i ->

        }
        builder.show()
    }

    fun AlertDialogP(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("В параметрах не указана з/п")
        builder.setMessage("Перейти в параметры?")
        builder.setPositiveButton("Да"){dialog, i ->
            finish()
            startActivity(Intent(this,Parametry::class.java))
        }
        builder.setNegativeButton("Нет"){dialog, i ->

        }
        builder.show()
    }

    fun saveZP(){
        val intent = intent
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")
        val uid = FirebaseAuth.getInstance().uid

        val getdata_ZP = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val ZP = p0.child("Зарплата").value
                if (ZP == null || ZP.toString() == "0"){
                    AlertDialogP()
                }else{
                    FirebaseDatabase.getInstance().getReference("users/$uid")
                        .child("$year").child("$month").child("Зарплата").setValue("$ZP")
                        .addOnSuccessListener {
                            if (ZP.toString() != "0"){
                                Toast.makeText(applicationContext, "Зарплата в этом месяце сохранена!", Toast.LENGTH_SHORT).show()
                                ZP_textView.text = "$ZP"
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Ошибка, попробуй позже!", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(getdata_ZP)
    }

//    fun pyti(): String {
////        val intent = intent
////        var day = intent.getStringExtra("Day")
//        var month = intent.getStringExtra("Month")
//        var year = intent.getStringExtra("Year")
//        val uid = FirebaseAuth.getInstance().uid
//        val pyt = "users/$uid/$year/$month/переработка"
//        return pyt
//
//    }
//
}