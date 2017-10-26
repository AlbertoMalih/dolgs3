package com.example.dolgs

import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dolgs.Constants.SHOWING_ELEMENT
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SimpleItemRecyclerViewAdapter(private val containThisActivity: MainActivity, private val noteValues: List<Debt>) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_list_content, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val showingDebt = noteValues[position]
        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(containThisActivity, holder.itemView)
            popup.inflate(R.menu.actions_with_note_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_item_delete_note -> {
                        containThisActivity.deleteDebt(noteValues[position], position)
                    }
                }
                false
            }
            popup.show()
            true
        }
        holder.mView.setOnClickListener { v ->
            val context = v.context
            context.startActivity(
                    Intent(context, ItemDetailActivity::class.java).putExtra(SHOWING_ELEMENT, noteValues[position])
            )

        }
        holder.here_name.text = showingDebt.namePartner
        holder.age.text = showingDebt.age
        holder.date.text = dateFormatter.format(showingDebt.date)
        holder.type.text = containThisActivity.resources.getStringArray(R.array.who_must_strs)[showingDebt.typeDebt.ordinal]
    }

    override fun getItemCount(): Int {
        return noteValues.size
    }


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var date: TextView = mView.findViewById(R.id.date)
        var type: TextView = mView.findViewById(R.id.type)
        var age: TextView = mView.findViewById(R.id.age)
        var here_name: TextView = mView.findViewById(R.id.here_name)
    }

    companion object {
        val dateFormatter: DateFormat = SimpleDateFormat("yyyy:MM:dd:::HH:mm", Locale.getDefault())
    }
}