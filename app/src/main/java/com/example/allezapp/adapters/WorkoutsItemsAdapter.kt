package com.example.allezapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.allezapp.R
import com.example.allezapp.activities.ExercisesListActivity
import com.example.allezapp.firebase.FirebaseWorkouts
import com.example.allezapp.models.Workouts
import java.util.*


open class WorkoutsItemsAdapter(private val context: Context,
                                private var list: ArrayList<Workouts>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private var onClickListener: OnClickListener? = null


    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(


                LayoutInflater.from(context).inflate(
                        R.layout.item_workout,
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


        val model = list[position]

        if (holder is MyViewHolder) {


            holder.itemView.findViewById<TextView>(R.id.tv_name_workout).text = model.name
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ExercisesListActivity::class.java).putExtra("id", model.documentId)
                context.startActivity(intent)
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
        fun onClick(position: Int, model: Workouts) {}


    }
    fun removeAt(position: Int) {
        FirebaseWorkouts().deleteWorkout(list[position].documentId)

        list.removeAt(position)
        notifyItemRemoved(position)


    }



    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}