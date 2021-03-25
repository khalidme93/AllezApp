package com.example.allezapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.example.allezapp.R

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        setupActionBar()
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        findViewById<Button>(R.id.btnFinish).setOnClickListener{
            val intent = Intent(this@FinishActivity,MainActivity::class.java )
            startActivity(intent)
        }
    }

    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_finish_activity)
        setSupportActionBar(findViewById(R.id.toolbar_finish_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.finish)
        }

        toolBar.setNavigationOnClickListener {
            onBackPressed() }
    }

}