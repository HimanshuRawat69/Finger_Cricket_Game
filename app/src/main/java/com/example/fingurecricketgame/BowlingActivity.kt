package com.example.fingurecricketgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class BowlingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bowling)

        var oneView=findViewById<ImageView>(R.id.imageView5)
        var twoView=findViewById<ImageView>(R.id.imageView6)
        var threeView=findViewById<ImageView>(R.id.imageView7)
        var fourView=findViewById<ImageView>(R.id.imageView8)
        var sixView=findViewById<ImageView>(R.id.imageView9)
        var scoreView=findViewById<TextView>(R.id.textView8)
        var screenView=findViewById<ImageView>(R.id.imageView4)
        var targetView=findViewById<TextView>(R.id.textView9)
        var playagainView=findViewById<ImageView>(R.id.imageView10)



        var runs=0
        var wickets=0

        playagainView.setOnClickListener {
            val intent6=Intent(this,BattingBowlingSelect::class.java)
            startActivity(intent6)
        }
        oneView.setOnClickListener {
            var randomHit=(1..6).random()
            var opponentHit= if(randomHit==5)
            {
                (1..4).random()
            }
            else{
                randomHit
            }
            if(opponentHit==1)
            {
                screenView.setImageResource(R.drawable.onevsone)
                wickets++
                scoreView.setText("Score: $runs-$wickets")
                if(wickets==10)
                {
                    oneView.isClickable=false
                    twoView.isClickable=false
                    threeView.isClickable=false
                    fourView.isClickable=false
                    sixView.isClickable=false
                    val intent5=Intent(this,BattingTargetActivity::class.java)
                    intent5.putExtra("TARGET",runs+1)
                    startActivity(intent5)
                }
            }
            else if(opponentHit==2)
            {
                screenView.setImageResource(R.drawable.onevstwo)
                runs+=2
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==3)
            {
                screenView.setImageResource(R.drawable.onevsthree)
                runs+=3
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==4)
            {
                screenView.setImageResource(R.drawable.onevsfour)
                runs+=4
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==6)
            {
                screenView.setImageResource(R.drawable.onevssix)
                runs+=6
                scoreView.setText("Score: $runs-$wickets")
            }
            else{

            }


        }

        twoView.setOnClickListener {
            var randomHit=(1..6).random()
            var opponentHit= if(randomHit==5)
            {
                3
            }
            else{
                randomHit
            }
            if(opponentHit==1)
            {
                screenView.setImageResource(R.drawable.twovsone)
                runs+=1
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==2)
            {
                screenView.setImageResource(R.drawable.twovstwo)
                wickets++
                scoreView.setText("Score: $runs-$wickets")
                if(wickets==10)
                {
                    oneView.isClickable=false
                    twoView.isClickable=false
                    threeView.isClickable=false
                    fourView.isClickable=false
                    sixView.isClickable=false
                    val intent5=Intent(this,BattingTargetActivity::class.java)
                    intent5.putExtra("TARGET",runs+1)
                    startActivity(intent5)
                }
            }
            else if(opponentHit==3)
            {
                screenView.setImageResource(R.drawable.twovsthree)
                runs+=3
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==4)
            {
                screenView.setImageResource(R.drawable.twovsfour)
                runs+=4
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==6)
            {
                screenView.setImageResource(R.drawable.twovssix)
                runs+=6
                scoreView.setText("Score: $runs-$wickets")
            }
            else{

            }

        }

        threeView.setOnClickListener {
            var randomHit=(1..6).random()
            var opponentHit= if(randomHit==5)
            {
                3
            }
            else{
                randomHit
            }
            if(opponentHit==1)
            {
                screenView.setImageResource(R.drawable.threevsone)
                runs+=1
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==2)
            {
                screenView.setImageResource(R.drawable.threevstwo)
                runs+=2
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==3)
            {
                screenView.setImageResource(R.drawable.threevsthree)
                wickets++
                scoreView.setText("Score: $runs-$wickets")
                if(wickets==10)
                {
                    oneView.isClickable=false
                    twoView.isClickable=false
                    threeView.isClickable=false
                    fourView.isClickable=false
                    sixView.isClickable=false
                    val intent5=Intent(this,BattingTargetActivity::class.java)
                    intent5.putExtra("TARGET",runs+1)
                    startActivity(intent5)
                }
            }
            else if(opponentHit==4)
            {
                screenView.setImageResource(R.drawable.threevsfour)
                runs+=4
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==6)
            {
                screenView.setImageResource(R.drawable.threevssix)
                runs+=6
                scoreView.setText("Score: $runs-$wickets")
            }
            else{

            }

        }

        fourView.setOnClickListener {
            var randomHit=(1..6).random()
            var opponentHit= if(randomHit==5)
            {
                3
            }
            else{
                randomHit
            }
            if(opponentHit==1)
            {
                screenView.setImageResource(R.drawable.fourvsone)
                runs+=1
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==2)
            {
                screenView.setImageResource(R.drawable.fourvstwo)
                runs+=2
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==3)
            {
                screenView.setImageResource(R.drawable.fourvsthree)
                runs+=3
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==4)
            {
                screenView.setImageResource(R.drawable.fourvsfour)
                wickets++
                scoreView.setText("Score: $runs-$wickets")
                if(wickets==10)
                {
                    oneView.isClickable=false
                    twoView.isClickable=false
                    threeView.isClickable=false
                    fourView.isClickable=false
                    sixView.isClickable=false
                    val intent5=Intent(this,BattingTargetActivity::class.java)
                    intent5.putExtra("TARGET",runs+1)
                    startActivity(intent5)
                }
            }
            else if(opponentHit==6)
            {
                screenView.setImageResource(R.drawable.fourvssix)
                runs+=6
                scoreView.setText("Score: $runs-$wickets")
            }
            else{

            }


        }

        sixView.setOnClickListener {
            var randomHit=(1..6).random()
            var opponentHit= if(randomHit==5)
            {
                3
            }
            else{
                randomHit
            }
            if(opponentHit==1)
            {
                screenView.setImageResource(R.drawable.sixvsone)
                runs+=1
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==2)
            {
                screenView.setImageResource(R.drawable.sixvstwo)
                runs+=2
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==3)
            {
                screenView.setImageResource(R.drawable.sixvsthree)
                runs+=3
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==4)
            {
                screenView.setImageResource(R.drawable.sixvsfour)
                runs+=4
                scoreView.setText("Score: $runs-$wickets")
            }
            else if(opponentHit==6)
            {
                screenView.setImageResource(R.drawable.sixvssix)
                wickets++
                scoreView.setText("Score: $runs-$wickets")
                if(wickets==10)
                {
                    oneView.isClickable=false
                    twoView.isClickable=false
                    threeView.isClickable=false
                    fourView.isClickable=false
                    sixView.isClickable=false
                    val intent5=Intent(this,BattingTargetActivity::class.java)
                    intent5.putExtra("TARGET",runs+1)
                    startActivity(intent5)
                }
            }
            else{

            }

        }
    }
}