package com.example.allezapp.utils

import com.example.allezapp.R
import com.example.allezapp.models.Exercise

object Constants {
    // Firebase Constants
    // This  is used for the collection name for USERS.
    const val USERS: String = "users"
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val WEIGHT: String = "weight"
}

class Constant{
    companion object{
        fun defaultExerciseList(): ArrayList<Exercise> {

            val exerciseList = ArrayList<Exercise>()

            val pushUp =
                    Exercise(1, "Push Up", R.drawable.chest, false, false)
            exerciseList.add(pushUp)

            val plankRotation = Exercise(2, "Plank Rotation", R.drawable.chest2, false, false)
            exerciseList.add(plankRotation)

            val shoulderPress = Exercise(3, "Shoulder Press", R.drawable.chest3, false, false)
            exerciseList.add(shoulderPress)

            val diamondPushUp =
                    Exercise(4, "Diamond Push Up", R.drawable.back, false, false)
            exerciseList.add(diamondPushUp)

            val superMan =
                    Exercise(
                            5,
                            "Superman",
                            R.drawable.back2,
                            false,
                            false
                    )
            exerciseList.add(superMan)

            val kneeRoll = Exercise(6, "Knee Roll", R.drawable.back3, false, false)
            exerciseList.add(kneeRoll)

            val lunges =
                    Exercise(
                            7,
                            "Lunges",
                            R.drawable.ben,
                            false,
                            false
                    )
            exerciseList.add(lunges)

            val squat = Exercise(8, "Squat", R.drawable.ben2, false, false)
            exerciseList.add(squat)

            val abdominalCrunches =
                    Exercise(
                            9, "Abdominal Crunches",
                            R.drawable.abs,
                            false,
                            false
                    )
            exerciseList.add(abdominalCrunches)

            val burpees = Exercise(10, "burpees", R.drawable.cardio, false, false)
            exerciseList.add(burpees)

            val highKnee =
                    Exercise(
                            11,
                            "High Knee",
                            R.drawable.cardio2,
                            false,
                            false
                    )
            exerciseList.add(highKnee)



            return exerciseList
        }
    }
}