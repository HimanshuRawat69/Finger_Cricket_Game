package com.example.fingurecricketgame

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
private lateinit var database: FirebaseDatabase
private lateinit var gameReference: DatabaseReference
class MultiPlayerJoinBattingTargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_join_batting_target)

        database = FirebaseDatabase.getInstance()

        val oneView = findViewById<ImageView>(R.id.imageView5)
        val twoView = findViewById<ImageView>(R.id.imageView6)
        val threeView = findViewById<ImageView>(R.id.imageView7)
        val fourView = findViewById<ImageView>(R.id.imageView8)
        val sixView = findViewById<ImageView>(R.id.imageView9)
        val scoreView = findViewById<TextView>(R.id.textView8)
        val screenView = findViewById<ImageView>(R.id.imageView4)
        val playagainView = findViewById<ImageView>(R.id.imageView10)
        val gameIdView = findViewById<TextView>(R.id.textView10)
        val lottieAnimationView = findViewById<ProgressBar>(R.id.lottie1)
        val targetView=findViewById<TextView>(R.id.textView9)

        var target=intent.getIntExtra("TARGET",0)
        targetView.text="Target: $target Runs"
        val gameId=intent.getStringExtra("GAMEID")
        gameIdView.text="Game ID: $gameId"
        gameReference = database.reference.child("games").child(gameId!!)

        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        fun checkInternetConnection():Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            // Check if there's a network connection
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
            // Connected to the internet
            // You can perform your network operations here
        }

        playagainView.setOnClickListener {
            val intent6= Intent(this,MultiPlayerCreateJoinActivity::class.java)
            startActivity(intent6)
        }
    }
}