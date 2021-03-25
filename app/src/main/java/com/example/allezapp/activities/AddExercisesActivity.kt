package com.example.allezapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allezapp.R
import com.example.allezapp.adapters.ExercisesItemsAdapter
import com.example.allezapp.firebase.FirebaseExercises
import com.example.allezapp.models.Exercises
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddExercisesActivity : BaseActivity() {
    private lateinit var myExercises:ArrayList<Exercises>
    private lateinit var adapter: ExercisesItemsAdapter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercises)
        setupActionBar()
        FirebaseExercises().getExercisesList(this@AddExercisesActivity)
        myExercises = intent.getParcelableArrayListExtra<Exercises>("myExercises") as ArrayList<Exercises>
        val id:String = intent.getStringExtra("id").toString()



        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        findViewById<FloatingActionButton>(R.id.fab_save_exercises).setOnClickListener {
            myExercises = adapter.getMyExercises()!!;
            val myExerciseIds:List<String> = myExercises.map { x -> x.ExerciseId }
                FirebaseExercises().addExercisesToWorkout(id, myExerciseIds)
                val intent = Intent(this@AddExercisesActivity, ExercisesListActivity::class.java)
                intent.putExtra("id",id )

                startActivity(intent)
                Toast.makeText(this, "Your exercises is added to your workout list ", 5).show()


        }
    }

    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_addexercisesList_activity)
        setSupportActionBar(findViewById(R.id.toolbar_addexercisesList_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.AddExercises)
        }

        toolBar.setNavigationOnClickListener { onBackPressed()
        finish()}
    }

    fun populateExercisesListToUI(allExercises: ArrayList<Exercises>) {

        if (allExercises.size > 0) {

            findViewById<RecyclerView>(R.id.rvAddExercisesList).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_exercises_indataBase).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rvAddExercisesList).layoutManager = LinearLayoutManager(this@AddExercisesActivity)
            findViewById<RecyclerView>(R.id.rvAddExercisesList).setHasFixedSize(true)

            // Create an instance of exerciseItemsAdapter and pass the exerciseList to it.
            adapter = ExercisesItemsAdapter(this@AddExercisesActivity, allExercises, myExercises)
            findViewById<RecyclerView>(R.id.rvAddExercisesList).adapter = adapter // Attach the adapter to the recyclerView.

            adapter.setOnClickListener(object: ExercisesItemsAdapter.OnClickListener{
                override fun onClick(position: Int, model: Exercises) {
                }
            })
        } else {
            findViewById<RecyclerView>(R.id.rvAddExercisesList).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_exercises_indataBase).visibility = View.VISIBLE
        }
    }
}