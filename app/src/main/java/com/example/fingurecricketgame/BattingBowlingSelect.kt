package com.example.fingurecricketgame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class BattingBowlingSelect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batting_bowling_select)

        var battingView=findViewById<ImageView>(R.id.imageView2)
        battingView.setOnClickListener {
            var intent=Intent(this,BattingActivity::class.java)
            startActivity(intent)
        }

        var bowlingView=findViewById<ImageView>(R.id.imageView3)
        bowlingView.setOnClickListener {
            var intent=Intent(this,BowlingActivity::class.java)
            startActivity(intent)
        }

    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Display a Toast message to inform the user that the back button is disabled
        val intent7=Intent(this,MainActivity::class.java)
        startActivity(intent7)

        // By using the @SuppressLint annotation, we're indicating that we're intentionally not calling super.onBackPressed()
        // super.onBackPressed()
    }
}