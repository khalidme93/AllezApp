package com.example.allezapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allezapp.R
import com.example.allezapp.adapters.WorkoutsItemsAdapter
import com.example.allezapp.firebase.FirebaseWorkouts
import com.example.allezapp.models.Workouts
import com.example.allezapp.utils.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class WorkOutActivity : BaseActivity() {
    companion object {

        const val CREATE_WORKOUT_REQUEST_CODE: Int = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_work_out)
        setupActionBar()

        FirebaseWorkouts().getWorkoutList(this@WorkOutActivity)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        findViewById<FloatingActionButton>(R.id.fab_create_workout).setOnClickListener {
            val intent = Intent(this@WorkOutActivity, CreateWorkoutActivity::class.java)
            startActivityForResult(intent, CREATE_WORKOUT_REQUEST_CODE)
        }
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // TODO (Step 6: Call the adapter function when it is swiped for delete)
                // START
                val adapter =
                    findViewById<RecyclerView>(R.id.rvWorkout).adapter as WorkoutsItemsAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(findViewById<RecyclerView>(R.id.rvWorkout))
        // END

    }


    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_workout_activity)
        setSupportActionBar(findViewById(R.id.toolbar_workout_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.workout)
        }

        toolBar.setNavigationOnClickListener {
            onBackPressed()
            val intent = Intent(this@WorkOutActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun populateWorkoutsListToUI(workoutsList: ArrayList<Workouts>) {

        if (workoutsList.size > 0) {

            findViewById<RecyclerView>(R.id.rvWorkout).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_workout_available).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rvWorkout).layoutManager =
                LinearLayoutManager(this@WorkOutActivity)
            findViewById<RecyclerView>(R.id.rvWorkout).setHasFixedSize(true)

            // Create an instance of BoardItemsAdapter and pass the boardList to it.
            val adapter = WorkoutsItemsAdapter(this@WorkOutActivity, workoutsList)
            findViewById<RecyclerView>(R.id.rvWorkout).adapter =
                adapter // Attach the adapter to the recyclerView.

            adapter.setOnClickListener(object : WorkoutsItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Workouts) {
                    startActivity(Intent(this@WorkOutActivity, ExercisesListActivity::class.java))
                }
            })
        } else {

            findViewById<RecyclerView>(R.id.rvWorkout).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_workout_available).visibility = View.VISIBLE

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == CREATE_WORKOUT_REQUEST_CODE
        ) {
            // Get the latest boards list.
            FirebaseWorkouts().getWorkoutList(this@WorkOutActivity)
        }
        // END
        else {
            Log.e("Cancelled", "Cancelled")
        }
    }

}



