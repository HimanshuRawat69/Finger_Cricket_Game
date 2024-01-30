package com.example.fingurecricketgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var singlePlayer=findViewById<Button>(R.id.button)
        var multiPlayer=findViewById<Button>(R.id.button2)

        singlePlayer.setOnClickListener {
            var intent=Intent(this,BattingBowlingSelect::class.java)
            startActivity(intent);

        }

        multiPlayer.setOnClickListener {
            var intent=Intent(this,MultiPlayerCreateJoinActivity::class.java)
            startActivity(intent);

        }

    }
}