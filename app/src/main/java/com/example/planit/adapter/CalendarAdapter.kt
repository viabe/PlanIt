package com.example.planit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planit.R
import com.example.planit.data.CalendarData

class CalendarAdapter(
    private val days: List<CalendarData>,
    private val onDateSelected: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDay: TextView = view.findViewById(R.id.textViewDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayData = days[position]
        holder.textViewDay.text = dayData.day.toString()
        holder.itemView.setOnClickListener { onDateSelected(dayData.day) }
    }

    override fun getItemCount() = days.size
}