package com.example.rzob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pererab.*

class Pererab : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pererab)

        val intent = intent
        var day = intent.getStringExtra("Day")
        var month = intent.getStringExtra("Month")
        var year = intent.getStringExtra("Year")

        textView.text = "$day.$month.$year"
    }
}