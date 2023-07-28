package com.kamus.cookit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kamus.cookit.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_splash_screen)

        splashScreen.setKeepOnScreenCondition{ true }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}