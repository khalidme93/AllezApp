package com.example.allezapp.models

import android.os.Parcel
import android.os.Parcelable



data class Workouts(
        val name: String = "",
        var documentId: String = "",
        val exercises: ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.createStringArrayList()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(documentId)
        writeStringList(exercises)
    }

    override fun toString(): String {
        return name
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Workouts> = object : Parcelable.Creator<Workouts> {
            override fun createFromParcel(source: Parcel): Workouts = Workouts(source)
            override fun newArray(size: Int): Array<Workouts?> = arrayOfNulls(size)
        }
    }
}
