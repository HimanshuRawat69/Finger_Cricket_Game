package com.himanshudev.fingurecricketgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.himanshudev.fingurecricketgame.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var gameReference: DatabaseReference
private lateinit var database: FirebaseDatabase
class MultiPlayerCreateJoinActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_create_join)
        database = FirebaseDatabase.getInstance()
        val editTextCode = findViewById<EditText>(R.id.editTextCode)
        val buttonJoin = findViewById<Button>(R.id.buttonJoin)
        val buttonCreate = findViewById<Button>(R.id.buttonCreate)
        gameReference = FirebaseDatabase.getInstance().reference.child("games")


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

        buttonCreate.setOnClickListener {

            if(!checkInternetConnection())
            {
                showToast("No Internet Connection!!")
                return@setOnClickListener
            }

            val randomCode = generateRandomCode()
            createGameLobby(randomCode)
            val intent=Intent(this@MultiPlayerCreateJoinActivity,MultiPlayerCreateBattingActivity::class.java)
            intent.putExtra("GAMEID",randomCode)
            startActivity(intent)
        }

        buttonJoin.setOnClickListener {

            if(!checkInternetConnection())
            {
                showToast("No Internet Connection!!")
                return@setOnClickListener
            }

            val code = editTextCode.text.toString()
            joinGameLobby(code)
            val intent=Intent(this@MultiPlayerCreateJoinActivity,MultiPlayerJoinBowlingActivity::class.java)
            intent.putExtra("GAMEID",code)
            startActivity(intent)
        }
    }

    private fun generateRandomCode(): String {
        val characters = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789"
        return (1..6)
            .map { characters.random() }
            .joinToString("")
    }

    private fun createGameLobby(randomCode: String) {
        val Reference = gameReference.child(randomCode)
        val game = hashMapOf(

            "Player1Hit" to 0,
            "Player1BattingRuns" to 0,
            "Player1Wickets" to 0,
            "Player2BattingRuns" to 0,
            "Player2Wickets" to 0,
            "Player2Hit" to 0,
            "Player1Joined" to false,
            "Player2Joined" to false,
            "Player1Response" to false,
            "Player2Response" to false
        )

        Reference.setValue(game)
            .addOnSuccessListener {
                Toast.makeText(this,"Game lobby created with code: $randomCode",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"Error creating game lobby: $e",Toast.LENGTH_SHORT).show()
            }
    }

    private fun joinGameLobby(code: String) {
        val gameRef= gameReference.child(code).child("Player1Joined")
        gameRef.get().addOnSuccessListener {
            var value=it.getValue(Boolean::class.java)
            if(value==true)
            {
                gameReference.child(code).child("Player2Joined").setValue(true)
                Toast.makeText(this,"You joined Game with $code",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this,"No Game with $code Go back Enter Again!!",Toast.LENGTH_SHORT).show()
            }
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
