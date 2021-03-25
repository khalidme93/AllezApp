package com.example.allezapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allezapp.R
import com.example.allezapp.adapters.MyExercisesItemsAdapter
import com.example.allezapp.firebase.FirebaseWorkouts
import com.example.allezapp.models.Exercises
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class ExercisesListActivity : BaseActivity() {

    private lateinit var FloatingActionButton: FloatingActionButton;
    private lateinit var MyExercises: ArrayList<Exercises>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises_list)
        setupActionBar()
        val id: String = intent.getStringExtra("id").toString()
        MyExercises = ArrayList();

        FirebaseWorkouts().getExercisesFromWorkout(id, this)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab_add_exercises);
        FloatingActionButton.setOnClickListener {
            val intent = Intent(this@ExercisesListActivity, AddExercisesActivity::class.java)
            intent.putExtra("myExercises", MyExercises)
            intent.putExtra("id", id)
            startActivity(intent)
        }


    }

    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_exercisesList_activity)
        setSupportActionBar(findViewById(R.id.toolbar_exercisesList_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.myExercises)
        }

        toolBar.setNavigationOnClickListener {
            onBackPressed()
            val intent = Intent(this, WorkOutActivity::class.java)
            startActivity(intent)
        }
    }

    fun populateExercisesListToUI(exercises: ArrayList<Exercises>) {
        MyExercises.addAll(exercises);
        if (MyExercises.size > 0) {

            findViewById<RecyclerView>(R.id.rvExercisesList).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_exercises_added).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rvExercisesList).layoutManager =
                LinearLayoutManager(this@ExercisesListActivity)
            findViewById<RecyclerView>(R.id.rvExercisesList).setHasFixedSize(true)

            // Create an instance of exerciseItemsAdapter and pass the exerciseList to it.
            val adapter = MyExercisesItemsAdapter(this@ExercisesListActivity, MyExercises)
            findViewById<RecyclerView>(R.id.rvExercisesList).adapter =
                adapter // Attach the adapter to the recyclerView.

            adapter.setOnClickListener(object : MyExercisesItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Exercises) {
                }
            })
        } else {
            findViewById<RecyclerView>(R.id.rvExercisesList).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_exercises_added).visibility = View.VISIBLE

        }
    }

}
