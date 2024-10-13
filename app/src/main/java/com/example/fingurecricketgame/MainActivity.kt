package com.example.fingurecricketgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var singlePlayer=findViewById<Button>(R.id.button)
        var multiPlayer=findViewById<Button>(R.id.button2)


            // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(this@MainActivity) {}

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

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