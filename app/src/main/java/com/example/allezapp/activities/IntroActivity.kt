package com.example.allezapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import com.example.allezapp.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        var signUPButtonIntro = findViewById<Button>(R.id.btn_sign_up_intro)
        signUPButtonIntro.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        var signInButtonIntro = findViewById<Button>(R.id.btn_sign_in_intro)

        signInButtonIntro.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }



    }
}