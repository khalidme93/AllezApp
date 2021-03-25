package com.example.allezapp.firebase

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.example.allezapp.activities.AddExercisesActivity
import com.example.allezapp.activities.ExerciseActivity
import com.example.allezapp.activities.ExercisesListActivity
import com.example.allezapp.models.Exercises
import com.example.allezapp.utils.Constants
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseExercises {
    private val mFireStore = FirebaseFirestore.getInstance()
    val storageRef = FirebaseStorage.getInstance()

    fun getExercisesList(activity: AddExercisesActivity) {

        // The collection name for BOARDS
        mFireStore.collection(Constants.EXERCISE)
            // A where array query as we want the list of the board in which the user is assigned. So here you can pass the current user id.
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of boards in the form of documents.
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                // Here we have created a new instance for Boards ArrayList.
                val exercisesList: ArrayList<Exercises> = ArrayList()

                // A for loop as per the list of documents to convert them into Boards ArrayList.
                for (i in document.documents) {

                    val exercise = i.toObject(Exercises::class.java)!!
                    exercise.ExerciseId = i.id

                    exercisesList.add(exercise)
                }

                // Here pass the result to the base activity.
                activity.populateExercisesListToUI(exercisesList)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting list.", e)
            }
    }

    fun getExercisesFromIds(exercises: ArrayList<String>, activity: ExercisesListActivity) {
        mFireStore.collection(Constants.EXERCISE)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firebase", "docs: " + documents.size());
                val exercisesList: ArrayList<Exercises> = ArrayList()

                for (i in documents) {
                    if (exercises.contains(i.id)) {
                        val exercise = i.toObject(Exercises::class.java)!!
                        exercise.ExerciseId = i.id
                        exercisesList.add(exercise)
                    }
                }
                activity.populateExercisesListToUI(exercisesList)
            }
    }

    fun addExercisesToWorkout(id: String, myExercises: List<String>) {
        Log.d("show me", "my exs" + myExercises)
        mFireStore.collection(Constants.WORKOUTS).document(id)
            .update("exercises", myExercises)
            .addOnSuccessListener {
                Log.d("data  ", "Data added ")
            }
            .addOnFailureListener {
                Log.d("data  ", "Data could not be added ")
            }

    }


    public fun getExerciseImage(image: String, imagePlaceHolder: ImageView) {
        storageRef.getReference("Exercises/").child(image).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArr ->
                imagePlaceHolder.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        byteArr,
                        0,
                        byteArr.size
                    )
                )
                Log.d("msg", "img changed")

            }
            .addOnFailureListener {
                Log.d("failed", "failed " + it)
            }
    }

    fun getExercisesFromIds(exercises: ArrayList<String>, activity: ExerciseActivity) {
        mFireStore.collection(Constants.EXERCISE)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firebase", "docs: " + documents.size());
                val exercisesList: ArrayList<Exercises> = ArrayList()

                for (i in documents) {
                    if (exercises.contains(i.id)) {
                        val exercise = i.toObject(Exercises::class.java)!!
                        exercise.ExerciseId = i.id
                        exercisesList.add(exercise)
                    }
                }
                activity.startExercise(exercisesList)
            }
    }


}