package com.himanshudev.fingurecricketgame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.himanshudev.fingurecricketgame.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var singlePlayer=findViewById<Button>(R.id.button)
        var multiPlayer=findViewById<Button>(R.id.button2)


            // Initialize the Google Mobile Ads SDK on a background thread.




        singlePlayer.setOnClickListener {
            var intent=Intent(this,BattingBowlingSelect::class.java)
            startActivity(intent);

        }

        multiPlayer.setOnClickListener {
            var intent=Intent(this,MultiPlayerCreateJoinActivity::class.java)
            startActivity(intent);

        }

    }
    override fun onBackPressed() {
        super.onBackPressed()  // finish the activity
        finishAffinity()       // close all activities in the app
        System.exit(0)         // kill the process
    }


}