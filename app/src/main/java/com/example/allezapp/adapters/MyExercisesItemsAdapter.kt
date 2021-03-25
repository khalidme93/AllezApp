package com.example.allezapp.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allezapp.R
import com.example.allezapp.firebase.FirebaseExercises
import com.example.allezapp.models.Exercises
import de.hdodenhof.circleimageview.CircleImageView


open class MyExercisesItemsAdapter(

        private val context: Context,
        private var list: ArrayList<Exercises>,
        private var myExercises: ArrayList<Exercises>? = null

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(myExercises == null) {
            myExercises = ArrayList<Exercises>()
        }
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_exercises,
                        parent,
                        false
                )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("lists", "testar lista: " + list)
        val model = list[position]
        if(myExercises != null) {
            Log.d("lists", "inside if first")
            Log.d("lists", "checking allex: " + myExercises + " == " + model.ExerciseId)

            for( exercise in myExercises!!) {

                Log.d("lists", "checking exerciseId: " + exercise.ExerciseId + " == " + model.ExerciseId)
                if(exercise.ExerciseId == model.ExerciseId) {
                    model.setSelected(true)
                    Log.d("lists", "model selected: " + model)


                }
            }
        }

        if (holder is MyViewHolder) {

            Glide
                    .with(context)
                    .load(FirebaseExercises().getExerciseImage(model.image, holder.itemView.findViewById<CircleImageView>(R.id.iv_exercise_image)))
                    .centerCrop()
                    .placeholder(R.drawable.ic_exercise_place_holder)
            Log.d("message", "image" + model.image)

            holder.itemView.findViewById<TextView>(R.id.tv_exercise_name).text = model.name
            holder.itemView.setBackgroundColor(if (model.isSelected()) Color.GRAY else Color.WHITE)

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }



            }

        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter..
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * An interface for onclick items.
     */
    interface OnClickListener {
        fun onClick(position: Int, model: Exercises)
    }


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}