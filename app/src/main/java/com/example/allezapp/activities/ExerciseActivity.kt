package com.example.allezapp.activities

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.allezapp.R

@Suppress("DEPRECATION")
class ExerciseActivity : AppCompatActivity(){

    private  var restTimer: CountDownTimer? = null
    private var restProgress = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excerise)
        setupActionBar()
        setupRestView()





        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


    }
    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_excercise_activity)
        setSupportActionBar(findViewById(R.id.toolbar_excercise_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        super.onDestroy()
    }

    private fun setRestProgressBar(){
        findViewById<ProgressBar>(R.id.workoutprogressBar).progress = restProgress
        restTimer = object: CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                findViewById<ProgressBar>(R.id.workoutprogressBar).progress = 10-restProgress
                findViewById<TextView>(R.id.tvStartTimer).text = (10- restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,
                        "Here now we wil start the exercise",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }
    private fun setupRestView(){
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }
}