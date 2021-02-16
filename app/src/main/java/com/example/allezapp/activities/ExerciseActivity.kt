package com.example.allezapp.activities

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.allezapp.R
import com.example.allezapp.models.Exercise
import com.example.allezapp.utils.Constant

@Suppress("DEPRECATION")
class ExerciseActivity : AppCompatActivity(){

    private  var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private  var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<Exercise>? = null

    private var currentExercisePosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excerise)
        setupActionBar()
        setupRestView()

        exerciseList = Constant.defaultExerciseList()





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

        var toolBar = findViewById<Toolbar>(R.id.toolbar_exercise_activity)
        setSupportActionBar(findViewById(R.id.toolbar_exercise_activity))

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
        findViewById<ProgressBar>(R.id.progressBar).progress = restProgress
        restTimer = object: CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                findViewById<ProgressBar>(R.id.progressBar).progress = 10-restProgress
                findViewById<TextView>(R.id.tvTimer).text = (10- restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
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
    private fun setExerciseProgressBar(){
        findViewById<ProgressBar>(R.id.progressBarExercise).progress = exerciseProgress
        exerciseTimer = object: CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                findViewById<ProgressBar>(R.id.progressBarExercise).progress = 30-exerciseProgress
                findViewById<TextView>(R.id.tvExerciseTimer).text = (30- exerciseProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,
                        "we will start the next rest screen",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }
    private fun setupExerciseView(){

        findViewById<LinearLayout>(R.id.llRestView).visibility = View.GONE
        findViewById<LinearLayout>(R.id.llExerciseView).visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        setExerciseProgressBar()
    }
}
