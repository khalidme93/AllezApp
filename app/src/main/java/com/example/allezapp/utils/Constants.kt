package com.example.allezapp.utils

import android.util.Log
import com.example.allezapp.firebase.FirebaseWorkouts
import com.example.allezapp.models.Exercises
import com.example.allezapp.utils.Constants.id
import com.example.allezapp.activities.ExerciseActivity as ExerciseActivity1

object Constants {
    // Firebase Constants
    const val USERS: String = "users"
    const val WORKOUTS: String = "workout"
    const val id: String = "documentId"
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val WEIGHT: String = "weight"
    const val EXERCISE: String = "exercises"
}

class Constant{
    companion object{
        fun defaultExerciseList(): ArrayList<Exercises> {

          val exerciseList = ArrayList<Exercises>()


            return exerciseList        }
    }
}