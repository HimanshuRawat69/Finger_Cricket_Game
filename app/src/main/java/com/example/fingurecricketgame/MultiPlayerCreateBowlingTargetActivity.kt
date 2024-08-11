package com.example.fingurecricketgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private lateinit var database: FirebaseDatabase
private lateinit var gameReference: DatabaseReference
class MultiPlayerCreateBowlingTargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_create_bowling_target)

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
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottie1)
        val targetView=findViewById<TextView>(R.id.textView9)

        var target=intent.getIntExtra("TARGET",0)
        targetView.text="Target: $target Runs"
        val gameId=intent.getStringExtra("GAMEID")
        gameIdView.text="Game ID: $gameId"
        gameReference = database.reference.child("games").child(gameId!!)
        var runs = 0
        var wickets = 0
        var player1hit = 0

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

        fun check_Result(wicket:Int,runs:Int)
        {
            if(runs>=target)
            {
                val remainingWkt=10-wickets
                targetView.setText("You loose by $remainingWkt Wickets")
                oneView.isClickable=false
                twoView.isClickable=false
                threeView.isClickable=false
                fourView.isClickable=false
                sixView.isClickable=false

            }
            else if(wickets==10)
            {
                val remainingRuns=target-runs-1
                targetView.setText("You win by $remainingRuns Runs")
                oneView.isClickable=false
                twoView.isClickable=false
                threeView.isClickable=false
                fourView.isClickable=false
                sixView.isClickable=false
            }
        }

        var player2ResponseListener: ValueEventListener? = null
        fun removePlayer1ResponseListener() {
            player2ResponseListener?.let {
                gameReference.child("Player2Response").removeEventListener(it)
                player2ResponseListener = null  // Set it to null to indicate that the listener is removed
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

            // Step 2: Listen for Player2Response changes
            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            // Fetch the value of Player2Hit
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    // Update UI based on Player2Hit value
                                    when (player2hit) {
                                        1 -> {
                                            wickets++
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.onevsone)
                                            playSound(this@MultiPlayerCreateBowlingTargetActivity)

                                        }

                                        2 -> {
                                            runs+=2
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.onevstwo)
                                        }

                                        3 -> {
                                            runs+=3
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.onevsthree)
                                        }

                                        4 -> {
                                            runs+=4
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.onevsfour)
                                        }

                                        6 -> {
                                            runs+=6
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.onevssix)
                                        }
                                    }

                                    // Update Player1Response to false
                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    check_Result(wickets,runs)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer1ResponseListener()
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

            // Step 2: Listen for Player2Response changes
            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            // Fetch the value of Player2Hit
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    // Update UI based on Player2Hit value
                                    when (player2hit) {
                                        1 -> {
                                            runs+=1
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovsone)

                                        }

                                        2 -> {
                                            wickets++
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovstwo)
                                            playSound(this@MultiPlayerCreateBowlingTargetActivity)

                                        }

                                        3 -> {
                                            runs+=3
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovsthree)
                                        }

                                        4 -> {
                                            runs+=4
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovsfour)
                                        }

                                        6 -> {
                                            runs+=6
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.twovssix)
                                        }
                                    }

                                    // Update Player1Response to false
                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    check_Result(wickets,runs)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer1ResponseListener()
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
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            // Fetch the value of Player2Hit
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    // Update UI based on Player2Hit value
                                    when (player2hit) {
                                        1 -> {
                                            runs+=1
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevsone)

                                        }

                                        2 -> {
                                            runs+=2
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevstwo)
                                        }

                                        3 -> {

                                            wickets++
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevsthree)
                                            playSound(this@MultiPlayerCreateBowlingTargetActivity)

                                        }

                                        4 -> {
                                            runs+=4
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevsfour)
                                        }

                                        6 -> {
                                            runs+=6
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.threevssix)
                                        }
                                    }

                                    // Update Player1Response to false
                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    check_Result(wickets,runs)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer1ResponseListener()
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

            // Step 2: Listen for Player2Response changes
            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            // Fetch the value of Player2Hit
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    // Update UI based on player1hit value
                                    when (player2hit) {
                                        1 -> {
                                            runs+=1
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvsone)

                                        }

                                        2 -> {
                                            runs+=2
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvstwo)
                                        }

                                        3 -> {
                                            runs+=3
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvsthree)

                                        }

                                        4 -> {
                                            wickets++
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvsfour)
                                            playSound(this@MultiPlayerCreateBowlingTargetActivity)
                                            if(wickets==10)
                                            {
                                                var winruns=target-runs
                                                targetView.text="You win by $winruns"
                                                gameReference.child("Player1Response")
                                                    .setValue(false)
                                                oneView.isClickable=false
                                                twoView.isClickable=false
                                                threeView.isClickable=false
                                                fourView.isClickable=false
                                                sixView.isClickable=false

                                            }
                                        }

                                        6 -> {
                                            runs+=6
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.fourvssix)
                                        }
                                    }

                                    // Update Player1Response to false
                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    check_Result(wickets,runs)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer1ResponseListener()
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

            // Step 2: Listen for Player2Response changes
            scope.launch {
                player2ResponseListener = gameReference.child("Player2Response").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val player2Response = dataSnapshot.getValue(Boolean::class.java)

                        if (player2Response == true) {
                            // Fetch the value of player1hit
                            gameReference.child("Player2Hit")
                                .get().addOnSuccessListener { hitSnapshot ->
                                    val player2hit =
                                        hitSnapshot.getValue(Int::class.java)

                                    // Update UI based on player1hit value
                                    when (player2hit) {
                                        1 -> {
                                            runs+=1
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvsone)

                                        }

                                        2 -> {
                                            runs+=2
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvstwo)
                                        }

                                        3 -> {
                                            runs+=3
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvsthree)

                                        }

                                        4 -> {
                                            runs+=4
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvsfour)

                                        }

                                        6 -> {
                                            wickets++
                                            scoreView.text = "Score: $runs-$wickets"
                                            screenView.setImageResource(R.drawable.sixvssix)
                                            playSound(this@MultiPlayerCreateBowlingTargetActivity)
                                            if(wickets==10)
                                            {
                                                var winruns=target-runs
                                                targetView.text="You win by $winruns"
                                                gameReference.child("Player1Response")
                                                    .setValue(false)
                                                oneView.isClickable=false
                                                twoView.isClickable=false
                                                threeView.isClickable=false
                                                fourView.isClickable=false
                                                sixView.isClickable=false

                                            }
                                        }
                                    }

                                    // Update Player1Response to false
                                    gameReference.child("Player1Response")
                                        .setValue(false)
                                    check_Result(wickets,runs)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.Main)
                                        {
                                            delay(300)
                                            notmakeVisible()
                                            removePlayer1ResponseListener()
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