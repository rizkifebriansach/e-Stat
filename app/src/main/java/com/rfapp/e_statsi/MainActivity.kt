package com.rfapp.e_statsi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
            finish()
        }, 4000)

    }
}