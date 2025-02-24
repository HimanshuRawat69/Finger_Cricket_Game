package com.himanshudev.fingurecricketgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.himanshudev.fingurecricketgame.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private lateinit var database: FirebaseDatabase
private lateinit var gameReference: DatabaseReference
class MultiPlayerCreateBattingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_create_batting)

        database = FirebaseDatabase.getInstance()
        val oneView=findViewById<ImageView>(R.id.imageView5)
        val twoView=findViewById<ImageView>(R.id.imageView6)
        val threeView=findViewById<ImageView>(R.id.imageView7)
        val fourView=findViewById<ImageView>(R.id.imageView8)
        val sixView=findViewById<ImageView>(R.id.imageView9)
        val scoreView=findViewById<TextView>(R.id.textView8)
        val screenView=findViewById<ImageView>(R.id.imageView4)
        val playagainView=findViewById<ImageView>(R.id.imageView10)
        val gameIdView=findViewById<TextView>(R.id.textView10)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottie1)
        val active=findViewById<ImageView>(R.id.imageView11)


        val scope= CoroutineScope(Dispatchers.IO)

        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        fun vibratePhone(context: Context) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For Android API level 26 and above
                val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                // For Android API level below 26
                vibrator.vibrate(500) // Vibrate for 500 milliseconds
            }
        }

        fun playSound(context: Context) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.wktsound) // Replace 'sound' with the name of your sound file
            mediaPlayer.start()

            // Release the MediaPlayer when the sound has finished playing
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }


        fun checkInternetConnection():Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            // Check if there's a network connection
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
                // Connected to the internet
                // You can perform your network operations here
        }



        fun makeVisible()
        {
            CoroutineScope(Dispatchers.Main).launch {
                oneView.isEnabled = false
                twoView.isEnabled = false
                threeView.isEnabled = false
                fourView.isEnabled = false
                sixView.isEnabled = false
                lottieAnimationView.visibility = View.VISIBLE
            }
        }
        fun notmakeVisible()
        {
            CoroutineScope(Dispatchers.Main).launch {
                oneView.isEnabled = true
                twoView.isEnabled = true
                threeView.isEnabled = true
                fourView.isEnabled = true
                sixView.isEnabled = true
                lottieAnimationView.visibility = View.GONE
            }
        }

        fun checkPlayer2Joined()
        {
            val childReference=gameReference.child("Player2Joined")

            childReference.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val value = snapshot.getValue<Boolean>()
                    if(value==true)
                    {
                        showToast("Player 2 Has Joined!")
                        active.setImageResource(R.drawable.active_dot)
                        CoroutineScope(Dispatchers.Main).launch {
                            notmakeVisible()
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
        })
        }

        val gameId=intent.getStringExtra("GAMEID")
        gameIdView.text="Game ID: $gameId"
        gameReference = database.reference.child("games").child(gameId!!)



        var runs=0
        var wickets=0

        gameReference.child("Player1Joined").setValue(true)
        makeVisible();
        checkPlayer2Joined()
        var player2ResponseListener: ValueEventListener? = null

         fun removePlayer2ResponseListener() {
            player2ResponseListener?.let {
                gameReference.child("Player2Response").removeEventListener(it)
                player2ResponseListener = null
            }
        }
        playagainView.setOnClickListener {
            val intent6= Intent(this,MultiPlayerCreateJoinActivity::class.java)
            startActivity(intent6)
        }


        oneView.setOnClickListener {

           if(!checkInternetConnection())
           {
               showToast("No Internet Connection!!")
               return@setOnClickListener
           }
            gameReference.child("Player1Hit").setValue(1)
            gameReference.child("Player1Response").setValue(true)
            makeVisible()

            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val player2Response = dataSnapshot.getValue(Boolean::class.java)

                                if (player2Response == true) {
                                    gameReference.child("Player2Hit")
                                        .get().addOnSuccessListener { hitSnapshot ->
                                            val player2hit = hitSnapshot.getValue(Int::class.java)

                                            when (player2hit) {
                                                1 -> {
                                                    wickets++
                                                    gameReference.child("Player1Wickets")
                                                        .setValue(wickets)
                                                    scoreView.text = "Score: $runs-$wickets"
                                                    screenView.setImageResource(R.drawable.onevsone)
                                                    vibratePhone(this@MultiPlayerCreateBattingActivity)
                                                    playSound(this@MultiPlayerCreateBattingActivity)
                                                    if(wickets==10)
                                                    {
                                                        gameReference.child("Player1Response")
                                                            .setValue(false)
                                                        oneView.isClickable=false
                                                        twoView.isClickable=false
                                                        threeView.isClickable=false
                                                        fourView.isClickable=false
                                                        sixView.isClickable=false
                                                        val intent1= Intent(this@MultiPlayerCreateBattingActivity,MultiPlayerCreateBowlingTargetActivity::class.java)
                                                        intent1.putExtra("TARGET",runs+1)
                                                        intent1.putExtra("GAMEID",gameId)
                                                        startActivity(intent1)
                                                    }
                                                }

                                                2 -> {
                                                    runs++
                                                    gameReference.child("Player1BattingRuns")
                                                        .setValue(runs)
                                                    scoreView.text = "Score: $runs-$wickets"
                                                    screenView.setImageResource(R.drawable.onevstwo)
                                                }

                                                3 -> {
                                                    runs++
                                                    gameReference.child("Player1BattingRuns")
                                                        .setValue(runs)
                                                    scoreView.text = "Score: $runs-$wickets"
                                                    screenView.setImageResource(R.drawable.onevsthree)
                                                }

                                                4 -> {
                                                    runs++
                                                    gameReference.child("Player1BattingRuns")
                                                        .setValue(runs)
                                                    scoreView.text = "Score: $runs-$wickets"
                                                    screenView.setImageResource(R.drawable.onevsfour)
                                                }

                                                6 -> {
                                                    runs++
                                                    gameReference.child("Player1BattingRuns")
                                                        .setValue(runs)
                                                    scoreView.text = "Score: $runs-$wickets"
                                                    screenView.setImageResource(R.drawable.onevssix)
                                                }
                                            }

                                            gameReference.child("Player1Response")
                                                .setValue(false)
                                            CoroutineScope(Dispatchers.Main).launch {
                                                withContext(Dispatchers.Main)
                                                {
                                                    delay(300)
                                                    notmakeVisible()
                                                    removePlayer2ResponseListener()
                                                }

                                        }
                                            }


                                }


                            }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }

        }


        twoView.setOnClickListener {
            if(!checkInternetConnection())
            {
                showToast("No Internet Connection!!")
                return@setOnClickListener
            }

            gameReference.child("Player1Hit").setValue(2)
            gameReference.child("Player1Response").setValue(true)
            makeVisible()

            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            // Fetch the value of Player2Hit
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit = hitSnapshot.getValue(Int::class.java)

                                    when (player2hit) {
                                        1 -> {
                                            runs+=2
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovsone)

                                        }

                                        2 -> {
                                            wickets++
                                            gameReference.child("Player1Wickets")
                                                .setValue(wickets)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovstwo)
                                            vibratePhone(this@MultiPlayerCreateBattingActivity)
                                            playSound(this@MultiPlayerCreateBattingActivity)
                                            if(wickets==10)
                                            {
                                                gameReference.child("Player1Response")
                                                    .setValue(false)
                                                oneView.isClickable=false
                                                twoView.isClickable=false
                                                threeView.isClickable=false
                                                fourView.isClickable=false
                                                sixView.isClickable=false
                                                val intent1= Intent(this@MultiPlayerCreateBattingActivity,MultiPlayerCreateBowlingTargetActivity::class.java)
                                                intent1.putExtra("TARGET",runs+1)
                                                intent1.putExtra("GAMEID",gameId)
                                                startActivity(intent1)
                                            }
                                        }

                                        3 -> {
                                            runs+=2
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovsthree)
                                        }

                                        4 -> {
                                            runs+=2
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovsfour)
                                        }

                                        6 -> {
                                            runs+=2
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovssix)
                                        }
                                    }

                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer2ResponseListener()
                                        }

                                    }
                                }


                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }

        }

        threeView.setOnClickListener {
            if(!checkInternetConnection())
            {
                showToast("No Internet Connection!!")
                return@setOnClickListener
            }

            gameReference.child("Player1Hit").setValue(3)
            gameReference.child("Player1Response").setValue(true)
            makeVisible()

            // Step 2: Listen for Player2Response changes
            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    when (player2hit) {
                                        1 -> {
                                            runs+=3
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevsone)

                                        }

                                        2 -> {
                                            runs+=3
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevstwo)
                                        }

                                        3 -> {

                                            wickets++
                                            gameReference.child("Player1Wickets")
                                                .setValue(wickets)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevsthree)
                                            vibratePhone(this@MultiPlayerCreateBattingActivity)
                                            playSound(this@MultiPlayerCreateBattingActivity)
                                            if(wickets==10)
                                            {
                                                gameReference.child("Player1Response")
                                                    .setValue(false)
                                                oneView.isClickable=false
                                                twoView.isClickable=false
                                                threeView.isClickable=false
                                                fourView.isClickable=false
                                                sixView.isClickable=false
                                                val intent1= Intent(this@MultiPlayerCreateBattingActivity,MultiPlayerCreateBowlingTargetActivity::class.java)
                                                intent1.putExtra("TARGET",runs+1)
                                                intent1.putExtra("GAMEID",gameId)
                                                startActivity(intent1)
                                            }
                                        }

                                        4 -> {
                                            runs+=3
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevsfour)
                                        }

                                        6 -> {
                                            runs+=3
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevssix)
                                        }
                                    }

                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer2ResponseListener()
                                        }

                                    }
                                }


                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }

        }

        fourView.setOnClickListener {
            if(!checkInternetConnection())
            {
                showToast("No Internet Connection!!")
                return@setOnClickListener
            }

            gameReference.child("Player1Hit").setValue(4)
            gameReference.child("Player1Response").setValue(true)
            makeVisible()

            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {

                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    when (player2hit) {
                                        1 -> {
                                            runs+=4
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvsone)

                                        }

                                        2 -> {
                                            runs+=4
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvstwo)
                                        }

                                        3 -> {
                                            runs+=4
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvsthree)

                                        }

                                        4 -> {
                                            wickets++
                                            gameReference.child("Player1Wickets")
                                                .setValue(wickets)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvsfour)
                                            vibratePhone(this@MultiPlayerCreateBattingActivity)
                                            playSound(this@MultiPlayerCreateBattingActivity)
                                            if(wickets==10)
                                            {
                                                gameReference.child("Player1Response")
                                                    .setValue(false)
                                                oneView.isClickable=false
                                                twoView.isClickable=false
                                                threeView.isClickable=false
                                                fourView.isClickable=false
                                                sixView.isClickable=false
                                                val intent1= Intent(this@MultiPlayerCreateBattingActivity,MultiPlayerCreateBowlingTargetActivity::class.java)
                                                intent1.putExtra("TARGET",runs+1)
                                                intent1.putExtra("GAMEID",gameId)
                                                startActivity(intent1)
                                            }
                                        }

                                        6 -> {
                                            runs+=4
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvssix)
                                        }
                                    }

                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer2ResponseListener()
                                        }

                                    }
                                }


                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }

        }

        sixView.setOnClickListener {
            if(!checkInternetConnection())
            {
                showToast("No Internet Connection!!")
                return@setOnClickListener
            }

            gameReference.child("Player1Hit").setValue(6)
            gameReference.child("Player1Response").setValue(true)
            makeVisible()

            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    when (player2hit) {
                                        1 -> {
                                            runs+=6
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvsone)

                                        }

                                        2 -> {
                                            runs+=6
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvstwo)
                                        }

                                        3 -> {
                                            runs+=6
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvsthree)

                                        }

                                        4 -> {
                                            runs+=6
                                            gameReference.child("Player1BattingRuns")
                                                .setValue(runs)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvsfour)

                                        }

                                        6 -> {
                                            wickets++
                                            gameReference.child("Player1Wickets")
                                                .setValue(wickets)
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvssix)
                                            vibratePhone(this@MultiPlayerCreateBattingActivity)
                                            playSound(this@MultiPlayerCreateBattingActivity)
                                            if(wickets==10)
                                            {
                                                gameReference.child("Player1Response")
                                                    .setValue(false)
                                                oneView.isClickable=false
                                                twoView.isClickable=false
                                                threeView.isClickable=false
                                                fourView.isClickable=false
                                                sixView.isClickable=false
                                                val intent1= Intent(this@MultiPlayerCreateBattingActivity,MultiPlayerCreateBowlingTargetActivity::class.java)
                                                intent1.putExtra("TARGET",runs+1)
                                                intent1.putExtra("GAMEID",gameId)
                                                startActivity(intent1)
                                            }
                                        }
                                    }

                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer2ResponseListener()
                                        }

                                    }
                                }


                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }

        }

    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Display a Toast message to inform the user that the back button is disabled
        Toast.makeText(this, "Back button is disabled for this activity", Toast.LENGTH_SHORT).show()

        // By using the @SuppressLint annotation, we're indicating that we're intentionally not calling super.onBackPressed()
        // super.onBackPressed()
    }
}