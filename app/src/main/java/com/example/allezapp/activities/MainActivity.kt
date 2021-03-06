package com.example.allezapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allezapp.R
import com.example.allezapp.firebase.FirebaseWorkouts
import com.example.allezapp.firebase.FirestoreClass
import com.example.allezapp.models.User
import com.example.allezapp.models.Workouts
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MY_PROFILE_REQUEST: Int = 11
    }

    private var pickedWorkout: Workouts? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
        val navView = findViewById<NavigationView>(R.id.nav_view)

        navView.setNavigationItemSelectedListener(this)

        // Get the current logged in user details.
        FirestoreClass().loadUserData(this@MainActivity)
        FirebaseWorkouts().getWorkoutList(this)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val start = findViewById<LinearLayout>(R.id.llStart)
        start.setOnClickListener {


            if(pickedWorkout != null) {
                if(pickedWorkout!!.exercises.size > 0 ) {
                    val intent = Intent(this, ExerciseActivity::class.java)
                    intent.putExtra("pickedExercises", pickedWorkout!!.exercises)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "there is no exercises in this workout", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "You have not picked a workout", Toast.LENGTH_LONG).show()
            }
        }

    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                // A double back press function is added in Base Activity.
                doubleBackToExit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST) {
            FirestoreClass().loadUserData(this)
        } else {
            Log.e("cancelled ", "cancelled ")
        }

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this@MainActivity, MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST
                )
            }
            R.id.nav_workout -> {
                startActivity(Intent(this@MainActivity, WorkOutActivity::class.java))
            }

            R.id.nav_sign_out -> {
                // Here sign outs the user from firebase in this device.
                FirebaseAuth.getInstance().signOut()

                // Send the user to the intro screen of the application.
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        findViewById<DrawerLayout?>(R.id.drawer_layout)?.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(findViewById(R.id.toolbar_main_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_menu)
        }

        toolBar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    /**
     * A function for opening and closing the Navigation Drawer.
     */
    private fun toggleDrawer() {

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    /**
     * A function to get the current user details from firebase.
     */
    fun updateNavigationUserDetails(user: User) {
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val headerView = navView.getHeaderView(0)

        val navUserImage = headerView.findViewById<ImageView>(R.id.iv_user_image)


        Glide
            .with(this@MainActivity)
            .load(user.image) // URL of the image
            .centerCrop() // Scale type of the image.
            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
            .into(navUserImage) // the view in which the image will be loaded.

        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        navUsername.text = user.name
    }

    fun populateList(workoutsList: ArrayList<Workouts>) {
        val listView = findViewById<ListView>(R.id.workoutsList)
        if (workoutsList.size > 0) {
            listView.visibility = View.VISIBLE

            listView.adapter =
                ArrayAdapter<Workouts>(this, android.R.layout.simple_list_item_1, workoutsList)
            listView.setOnItemClickListener { adapterView, view, position: Int, id: Long ->
                pickedWorkout = workoutsList[position]
            }

        } else {
            listView.visibility = View.GONE
        }
    }
}