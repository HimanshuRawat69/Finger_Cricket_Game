package com.example.fingurecricketgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BattingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batting)

        val oneView = findViewById<ImageView>(R.id.imageView5)
        val twoView = findViewById<ImageView>(R.id.imageView6)
        val threeView = findViewById<ImageView>(R.id.imageView7)
        val fourView = findViewById<ImageView>(R.id.imageView8)
        val sixView = findViewById<ImageView>(R.id.imageView9)
        val scoreView = findViewById<TextView>(R.id.textView8)
        val screenView = findViewById<ImageView>(R.id.imageView4)
        val playagainView = findViewById<ImageView>(R.id.imageView10)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottie1)


        var runs = 0
        var wickets = 0


        fun playSound(context: Context) {
            val mediaPlayer = MediaPlayer.create(
                context,
                R.raw.wktsound
            )
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }

        fun viewEnabledFalse() {
            oneView.isEnabled = false
            twoView.isEnabled = false
            threeView.isEnabled = false
            fourView.isEnabled = false
            sixView.isEnabled = false
            lottieAnimationView.visibility = View.VISIBLE
        }

        fun viewEnabledTrue() {
            oneView.isEnabled = true
            twoView.isEnabled = true
            threeView.isEnabled = true
            fourView.isEnabled = true
            sixView.isEnabled = true
            lottieAnimationView.visibility = View.INVISIBLE
        }


        playagainView.setOnClickListener {
            val intent6 = Intent(this, BattingBowlingSelect::class.java)
            startActivity(intent6)
        }

        fun viewClick(yourHit:Int)
        {
            var randomHit = (1..6).random()
            var opponentHit = if (randomHit == 5) {
                (1..4).random()
            } else {
                randomHit
            }
            viewEnabledFalse()
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                if (opponentHit == yourHit) {
                    when(opponentHit)
                    {
                        1 -> screenView.setImageResource(R.drawable.onevsone)
                        2 -> screenView.setImageResource(R.drawable.twovstwo)
                        3 -> screenView.setImageResource(R.drawable.threevsthree)
                        4 -> screenView.setImageResource(R.drawable.fourvsfour)
                        6 -> screenView.setImageResource(R.drawable.sixvssix)
                    }
                    wickets++
                    scoreView.setText("Score: $runs-$wickets")
                    playSound(this@BattingActivity)
                    if (wickets == 10) {
                        viewEnabledFalse()
                        val intent1 =
                            Intent(this@BattingActivity, BowlingTargetActivity::class.java)
                        intent1.putExtra("TARGET", runs + 1)
                        startActivity(intent1)
                    }
                }
                else{
                    runs += yourHit
                    scoreView.setText("Score: $runs-$wickets")
                    if(yourHit==1)
                    {
                        when(opponentHit)
                        {
                            2 -> {
                                screenView.setImageResource(R.drawable.onevstwo)
                            }
                            3 -> {
                                screenView.setImageResource(R.drawable.onevsthree)
                            }
                            4 -> {
                                screenView.setImageResource(R.drawable.onevsfour)
                            }
                            6 -> {
                                screenView.setImageResource(R.drawable.onevssix)
                            }
                        }
                    }
                    else if(yourHit==2){
                        when(opponentHit)
                        {
                            1 -> {
                                screenView.setImageResource(R.drawable.twovsone)
                            }
                            3 -> {
                                screenView.setImageResource(R.drawable.twovsthree)
                            }
                            4 -> {
                                screenView.setImageResource(R.drawable.twovsfour)
                            }
                            6 -> {
                                screenView.setImageResource(R.drawable.twovssix)
                            }
                        }
                    }
                    else if(yourHit==3){
                        when(opponentHit)
                        {
                            1 -> {
                                screenView.setImageResource(R.drawable.threevsone)
                            }
                            2 -> {
                                screenView.setImageResource(R.drawable.threevstwo)
                            }
                            4 -> {
                                screenView.setImageResource(R.drawable.threevsfour)
                            }
                            6 -> {
                                screenView.setImageResource(R.drawable.threevssix)
                            }
                        }
                    }
                    else if(yourHit==4){
                        when(opponentHit)
                        {
                            1 -> {
                                screenView.setImageResource(R.drawable.fourvsone)
                            }
                            2 -> {
                                screenView.setImageResource(R.drawable.fourvstwo)
                            }
                            3 -> {
                                screenView.setImageResource(R.drawable.fourvsthree)
                            }
                            6 -> {
                                screenView.setImageResource(R.drawable.fourvssix)
                            }
                        }
                    }
                    else if(yourHit==6){
                        when(opponentHit)
                        {
                            1 -> {
                                screenView.setImageResource(R.drawable.sixvsone)
                            }
                            2 -> {
                                screenView.setImageResource(R.drawable.sixvstwo)
                            }
                            3 -> {
                                screenView.setImageResource(R.drawable.sixvsthree)
                            }
                            4 -> {
                                screenView.setImageResource(R.drawable.sixvsfour)
                            }
                        }
                    }
                }
                viewEnabledTrue()
            }
        }
        oneView.setOnClickListener {
            viewClick(1)
        }
        twoView.setOnClickListener {
            viewClick(2)
        }
        threeView.setOnClickListener {
            viewClick(3)
        }
        fourView.setOnClickListener {
            viewClick(4)
        }
        sixView.setOnClickListener {
            viewClick(6)
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
