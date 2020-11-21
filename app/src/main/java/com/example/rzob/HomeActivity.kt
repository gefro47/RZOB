package com.example.rzob

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    var googleSingInClient : GoogleSignInClient? = null
    var p_day:String = ""
    var p_month:String = ""
    var p_year:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        }


        pererab_btn.setOnClickListener{
            perenos_data()
        }



        sign_out_button.setOnClickListener {
            signOut()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)
    }



    fun perenos_data(){
         val intent = Intent(this@HomeActivity, Pererab::class.java)
        intent.putExtra("Day", p_day)
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
}