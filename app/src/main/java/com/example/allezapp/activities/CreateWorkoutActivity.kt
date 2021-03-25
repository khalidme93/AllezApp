package com.example.allezapp.activities

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast.*
import androidx.appcompat.widget.Toolbar
import com.example.allezapp.R
import com.example.allezapp.firebase.FirebaseWorkouts
import com.example.allezapp.models.Workouts
import com.example.allezapp.utils.Constants.id
import com.google.firebase.firestore.FirebaseFirestore


@Suppress("DEPRECATION")
class CreateWorkoutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)
        setContentView(R.layout.activity_create_workout)
        setupActionBar()
        val createBtn = findViewById<TextView>(R.id.btn_createWorkout)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        createBtn.setOnClickListener {
            createWorkout()

        }
    }

    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_create_workout_activity)
        setSupportActionBar(findViewById(R.id.toolbar_create_workout_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.createWorkout)
        }

        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    fun workoutCreatedSuccessfully() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun createWorkout() {
        val workoutName = findViewById<TextView>(R.id.et_workout_name).text.toString()
        if (validateName(workoutName)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val exercisesArrayList: ArrayList<String> = ArrayList()
            val workouts = Workouts(
                    workoutName,
                    documentId = getCurrentUserID(),
                    exercisesArrayList
            )

            FirebaseWorkouts().createWorkout(this@CreateWorkoutActivity, workouts)
        }

    }

    private fun validateName(name: String): Boolean {
        return if (TextUtils.isEmpty(name)) {
            showErrorSnackBar("Please enter name.")
            false
        } else {
            true
        }
    }
}