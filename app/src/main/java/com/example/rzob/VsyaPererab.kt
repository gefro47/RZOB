package com.example.rzob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        textView5.visibility = View.INVISIBLE
        textView8.visibility = View.INVISIBLE
        textView9.visibility = View.INVISIBLE
        data_text_view.visibility = View.INVISIBLE
        k15_text_view.visibility = View.INVISIBLE
        k2_text_view.visibility = View.INVISIBLE
        textView13.visibility = View.INVISIBLE
        text_k15.visibility = View.INVISIBLE
        text_k2.visibility = View.INVISIBLE

        val intent = intent
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")

        date_text_view.text = "$month.$year"

        avans_text.setText(" Аванс 20.$month.$year: ")

        if (month != null && year != null) {
            if(month.toInt() == 12) {
                zp_text.setText(" Зарплата 5.1.${year.toInt()+1}: ")
            }else{
                zp_text.setText(" Зарплата 5.${month.toInt()+1}.$year: ")
            }
        }

            prew_btn.setOnClickListener {
            finish()
        }
        raschet()
    }

    fun raschet(){
        val intent = intent
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")
        val getdata_ZP = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val ZP = p0.child("$year").child("$month").child("Зарплата").value
                if (ZP == null || p0.child("$year").child("$month").child("Зарплата").value.toString() == "0"
                    || p0.child("$year").child("$month").child("Зарплата").value.toString() == ""){
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



                var sb1 = StringBuilder()
                var sb2 = StringBuilder()
                var sb3 = StringBuilder()
                val sum15 = arrayListOf<Double>()
                val sum2 = arrayListOf<Double>()
                for (i in p0.child("$year").child("$month").child("переработка").children) {
//                    var k15 = i.child("Часы С Коэф 1,5").value
//                    var k2 = i.child("Часы С Коэф 2").value

                    var k15 = i.child("Часы С Коэф 1,5").value
                    var k2 = i.child("Часы С Коэф 2").value
                    if (k15 == null){
                        k15 = 0.0
                    } else{
                        k15 = i.child("Часы С Коэф 1,5").value.toString().toDouble()
                    }
                    sum15.add(k15)
                    if (k2 == null) {
                        k2 = 0.0
                    } else {
                        k2 = i.child("Часы С Коэф 2").value.toString().toDouble()
                    }
                    sum2.add(k2)
                    sb1.append("${i.key}\n")
                    sb2.append("$k15\n")
                    sb3.append("$k2\n")
                }
                val sum15end = sum15.sum()
                val sum2end = sum2.sum()
                text_k15.text = "$sum15end"
                text_k2.text = "$sum2end"
                data_text_view.setText(sb1)
                if(sb1.toString() != ""){
                    textView5.visibility = View.VISIBLE
                    data_text_view.visibility = View.VISIBLE
                }
                k15_text_view.setText(sb2)
                if (sum15end != 0.0) {
                    textView8.visibility = View.VISIBLE
                    k15_text_view.visibility = View.VISIBLE
                    text_k15.visibility = View.VISIBLE
                }
                k2_text_view.setText(sb3)
                if (sum2end != 0.0) {
                    textView9.visibility = View.VISIBLE
                    k2_text_view.visibility = View.VISIBLE
                    text_k2.visibility = View.VISIBLE
                }
                if (sum15end != 0.0 || sum2end != 0.0){
                    textView13.visibility = View.VISIBLE
                }



                var data_rab_day = p0.child("$year").child("$month").child("Кол-во рабочих дней").value
                var data_otrab_day = p0.child("$year").child("$month").child("Кол-во отработанных дней").value
                if (data_rab_day == null || p0.child("$year").child("$month").child("Кол-во рабочих дней").value.toString() == "0"
                    || p0.child("$year").child("$month").child("Кол-во рабочих дней").value.toString() == ""
                    || data_otrab_day == null || p0.child("$year").child("$month").child("Кол-во отработанных дней").value.toString() == "0"
                    || p0.child("$year").child("$month").child("Кол-во отработанных дней").value.toString() == "") {
                    data_rab_day = "0"
                    data_otrab_day = "0"
                    AlertDialogDay()
                }

                val x_ZP = ZP.toString().toDouble()
                val x_rab_d = data_rab_day.toString().toDouble()
                val x_otrab_d = data_otrab_day.toString().toDouble()
                val x_sum_15 = sum15end
                val x_sum_2 = sum2end

                var raschet1 = x_ZP*0.87/x_rab_d*x_otrab_d
                var raschet2 = x_ZP*0.87/x_rab_d/8.0*x_sum_15*1.5
                var raschet3 = x_ZP*0.87/x_rab_d/8.0*x_sum_2*2.0
                var raschet123 = raschet1 + raschet2 + raschet3
                var raschet_d = DecimalFormat("#######.##")
                text_raschet.text = "${raschet_d.format(raschet123)}"
                val raschet_avans = raschet1/2
                avans_raschet_text.setText("${raschet_d.format(raschet_avans)}")
                val raschet_zp_5 = raschet123 - raschet_avans
                zp_raschet_text.setText("${raschet_d.format(raschet_zp_5)}")
                val raschet_pererab = raschet2 + raschet3
                sum_pererab_text.setText("${raschet_d.format(raschet_pererab)}")

            }
        }

        val uid = FirebaseAuth.getInstance().uid

        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(getdata_ZP)
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

    fun AlertDialogDay(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Не указаны рабочие дни")
        builder.setMessage("Расчет не возможен, укажите рабочие дни")
        builder.setPositiveButton("Окей"){dialog, i ->
            finish()
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
                                raschet()
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
}