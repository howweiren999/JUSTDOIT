package com.example.FindFun

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter internal constructor(context: Context) : RecyclerView.Adapter<ScheduleAdapter.ScheduleHistoryViewHolder>() {

    private val inflater:LayoutInflater= LayoutInflater.from(context)
    private var historyRecords= emptyList<ScheduleList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleHistoryViewHolder {
        val itemView=inflater.inflate(R.layout.rv_schedule_item,parent,false)
        return ScheduleHistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyRecords.size
    }

    override fun onBindViewHolder(holder: ScheduleHistoryViewHolder, position: Int) {
        holder.textMessage.text=historyRecords[position].message
        holder.textDate.text=historyRecords[position].date
        holder.textCreatedDate.text=historyRecords[position].createdDate

    }
    inner class ScheduleHistoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val textMessage: TextView =itemView.findViewById(R.id.textViewHMessage)
        val textDate: TextView=itemView.findViewById(R.id.textViewHDate)
        val textCreatedDate: TextView =itemView.findViewById(R.id.textViewHDateCreated)
    }
    internal fun setHistoryRecords(historyRecords: List<ScheduleList>) {
        this.historyRecords = historyRecords
        notifyDataSetChanged()
    }
}