package com.example.allezapp.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allezapp.R
import com.example.allezapp.adapters.ExerciseStatusAdapter
import com.example.allezapp.firebase.FirebaseExercises
import com.example.allezapp.models.Exercises

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var pause = false
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList: ArrayList<Exercises> = ArrayList<Exercises>()
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excerise)
        setupActionBar()
        tts = TextToSpeech(this, this)
        val exerciseList: ArrayList<String> =
            intent.getSerializableExtra("pickedExercises") as ArrayList<String>
        FirebaseExercises().getExercisesFromIds(exerciseList, this)




        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        findViewById<Button>(R.id.next).setOnClickListener {
            currentExercisePosition++

            if (currentExercisePosition < exerciseList.size) {
                setupExerciseView()
            } else {
                finish()
                val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.previous).setOnClickListener {
            currentExercisePosition--
            if (currentExercisePosition >= 0){
                setupExerciseView()
            }else{
                finish()
                val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                startActivity(intent)

            }
        }

    }


    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_exercise_activity)
        setSupportActionBar(findViewById(R.id.toolbar_exercise_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.title = resources.getString(R.string.exercises)

        }

        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (player != null) {
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setRestProgressBar() {
        findViewById<ProgressBar>(R.id.progressBar).progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                findViewById<ProgressBar>(R.id.progressBar).progress = 10 - restProgress
                findViewById<TextView>(R.id.tvTimer).text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setupRestView() {

        try {
            player = MediaPlayer.create(applicationContext, R.raw.beep_signal)
            player!!.isLooping = false
            player!!.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        findViewById<LinearLayout>(R.id.llRestView).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.llExerciseView).visibility = View.GONE
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        findViewById<TextView>(R.id.tvUpcomingExerciseName).text =
            exerciseList!![currentExercisePosition + 1].getName()
        setRestProgressBar()

    }

    private fun setExerciseProgressBar() {


        findViewById<ProgressBar>(R.id.progressBarExercise).progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                findViewById<ProgressBar>(R.id.progressBarExercise).progress = 30 - exerciseProgress
                findViewById<TextView>(R.id.tvExerciseTimer).text =
                    (30 - exerciseProgress).toString()
                findViewById<ProgressBar>(R.id.progressBarExercise).setOnClickListener {
                    if (pause == false) {
                        pause = true
                        exerciseTimer!!.cancel()
                    } else if (pause == true) {
                        pause = false
                        exerciseTimer!!.start()

                    }
                }
            }


            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun setupExerciseView() {

        findViewById<LinearLayout>(R.id.llRestView).visibility = View.GONE
        findViewById<LinearLayout>(R.id.llExerciseView).visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        setExerciseProgressBar()
        FirebaseExercises().getExerciseImage(
            exerciseList!![currentExercisePosition].image,
            findViewById<ImageView>(R.id.ivImage)
        )
        findViewById<TextView>(R.id.tvExerciseName).text =
            exerciseList!![currentExercisePosition].getName()
        Log.d("msg", "show me my image" + exerciseList!![currentExercisePosition].image)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExerciseStatusRecyclerView() {
        val rvExerciseStatus = findViewById<RecyclerView>(R.id.rvExerciseStatus)
        rvExerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)

        rvExerciseStatus.adapter = exerciseAdapter
    }

    fun startExercise(data: ArrayList<Exercises>) {
        exerciseList = data
        setupRestView()
        setupExerciseStatusRecyclerView()
    }
}

