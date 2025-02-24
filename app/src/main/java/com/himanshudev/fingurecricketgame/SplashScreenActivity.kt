package com.himanshudev.fingurecricketgame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.himanshudev.fingurecricketgame.R

class SplashScreenActivity : AppCompatActivity() {
    companion object {
        const val ANIMATION_TIME: Long = 3000 // Adjust based on animation length
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Ensure correct layout file

        // Initialize Lottie Animation



        // Delay transition to the next screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, ANIMATION_TIME)
    }
}
