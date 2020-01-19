package com.example.FindFun

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter internal constructor(context: Context) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val inflater:LayoutInflater= LayoutInflater.from(context)
    private var historyRecords= emptyList<ToDoList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView=inflater.inflate(R.layout.rv_child_history,parent,false)
        return HistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyRecords.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.textViewToDoName.text=historyRecords[position].ToDoName
        holder.textViewDateCreated.text=historyRecords[position].createdAt

    }
    inner class HistoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val textViewToDoName: TextView =itemView.findViewById(R.id.textViewTodoName)
        val textViewDateCreated: TextView=itemView.findViewById(R.id.textViewDateCreated)
    }
    internal fun setHistoryRecords(historyRecords: List<ToDoList>) {
        this.historyRecords = historyRecords
        notifyDataSetChanged()
    }
}