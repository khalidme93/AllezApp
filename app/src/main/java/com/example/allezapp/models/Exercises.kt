package com.example.allezapp.models

import android.os.Parcel
import android.os.Parcelable


data class Exercises(
        var ExerciseId: String = "",
        var name: String = "",
        val image: String = "",
        var isSelected: Boolean = false,
        var isCompleted: Boolean = false

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readBoolean()!!,

            )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(ExerciseId)
        writeString(name)
        writeString(image)
        writeBoolean(isSelected)

    }
    @JvmName("setSelected1")
    fun setSelected(selected: Boolean) {
        isSelected = selected
    }
    fun getId(): String {
        return ExerciseId
    }



    @JvmName("isSelected1")
    fun isSelected(): Boolean {
        return isSelected
    }
    fun getIsSelected(): Boolean {
        return isSelected
    }

    fun getIsCompleted(): Boolean {
        return isCompleted
    }
    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
    @JvmName("getName1")
    fun getName(): String {
        return name
    }
    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }





    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Exercises> = object : Parcelable.Creator<Exercises> {
            override fun createFromParcel(source: Parcel): Exercises = Exercises(source)
            override fun newArray(size: Int): Array<Exercises?> = arrayOfNulls(size)
        }
    }
}