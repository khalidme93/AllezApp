package com.example.allezapp.firebase

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.allezapp.activities.*
import com.example.allezapp.models.Workouts
import com.example.allezapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlin.collections.ArrayList

class FirebaseWorkouts {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun createWorkout(activity: CreateWorkoutActivity, workouts: Workouts) {
        mFireStore.collection(Constants.WORKOUTS)
            .document()
            .set(workouts, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "workout created successfully.")
                Toast.makeText(activity, "workout created successfully.", Toast.LENGTH_SHORT).show()
                activity.workoutCreatedSuccessfully()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a workout.",
                    e
                )
            }
    }

    fun getWorkoutList(activity: WorkOutActivity) {
        // The collection name for WORKOUTS
        mFireStore.collection(Constants.WORKOUTS)
            // A where array query as we want the list of the workouts  in which the user is created. So here you can pass the current user id.
            .whereEqualTo("documentId", getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { documents ->
                Log.d("message", "userId" + getCurrentUserID())
                Log.d("message", "show me " + documents.documents)
                // Here we get the list of boards in the form of documents.
                Log.e(activity.javaClass.simpleName, documents.documents.toString())
                // Here we have created a new instance for Boards ArrayList.
                val workoutsList: ArrayList<Workouts> = ArrayList()
                // A for loop as per the list of documents to convert them into Exercises ArrayList.
                for (i in documents) {
                    Log.d("Workouts", "one doc: " + documents)
                    val workout = i.toObject(Workouts::class.java)
                    workout.documentId = i.id
                    workoutsList.add(workout)
                }
                // Here pass the result to the base activity.
                activity.populateWorkoutsListToUI(workoutsList)
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a workout.", e)
            }
    }

    private fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun deleteWorkout(id: String) {
        mFireStore.collection(Constants.WORKOUTS).document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun getExercisesFromWorkout(id: String, activity: ExercisesListActivity) {
        mFireStore.collection(Constants.WORKOUTS).document(id)
            .get()
            .addOnSuccessListener { document ->
                val exercises: ArrayList<String> = document.get("exercises") as ArrayList<String>;
                if (exercises.isEmpty()) {
                    activity.populateExercisesListToUI(ArrayList())
                } else {
                    FirebaseExercises().getExercisesFromIds(exercises, activity)
                }
            }
    }

    fun getWorkoutList(activity: MainActivity) {
        // The collection name for WORKOUTS
        mFireStore.collection(Constants.WORKOUTS)
            // A where array query as we want the list of the workouts  in which the user is created. So here you can pass the current user id.
            .whereEqualTo("documentId", getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { documents ->
                Log.d("message", "userId" + getCurrentUserID())
                Log.d("message", "show me " + documents.documents)
                // Here we get the list of boards in the form of documents.
                Log.e(activity.javaClass.simpleName, documents.documents.toString())
                // Here we have created a new instance for Boards ArrayList.
                val workoutsList: ArrayList<Workouts> = ArrayList()
                // A for loop as per the list of documents to convert them into Exercises ArrayList.
                for (i in documents) {
                    Log.d("Workouts", "one doc: " + documents)
                    val workout = i.toObject(Workouts::class.java)
                    workout.documentId = i.id
                    workoutsList.add(workout)
                }
                activity.populateList(workoutsList)
            }
            .addOnFailureListener { e ->

                Log.e(activity.javaClass.simpleName, "Error while creating a workout.", e)
            }
    }
}