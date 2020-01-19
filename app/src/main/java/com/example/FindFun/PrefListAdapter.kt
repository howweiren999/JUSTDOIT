package com.example.FindFun

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView


class PrefListAdapter internal constructor(context: Context) :

    RecyclerView.Adapter<PrefListAdapter.PrefViewHolder>() {
    var token:SharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pref = emptyList<TimerPref>() // Cached copy of user
    val intent:Intent = Intent(context, SettingsActivity::class.java)
    val mcon :Context = context
    inner class PrefViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timerNameTextView: TextView = itemView.findViewById(R.id.textViewPrefName)
        val timerDescTextView: TextView = itemView.findViewById(R.id.textViewPrefDesc)
        val timerValTextView: TextView = itemView.findViewById(R.id.textViewPrefVal)
        val button:Button = itemView.findViewById(R.id.buttonUse)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefViewHolder {
        val itemView = inflater.inflate(R.layout.timer_pref_layout, parent, false)
        return PrefViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PrefViewHolder, position: Int) {
        val current = pref[position]
        holder.timerNameTextView.text = current.timerName
        holder.timerDescTextView.text = current.timerDesc
        holder.timerValTextView.text = current.time.toString() + ":00"
        holder.button.setOnClickListener {onClick(current.time)}
    }
    fun onClick(currentTime:Int) {
         val editor = token.edit()
         editor.putInt("currentTime",currentTime)
         editor.commit()
        mcon.startActivity(intent)
    }
    internal fun setPref(pref: List<TimerPref>) {
        this.pref = pref

        notifyDataSetChanged()
    }

    override fun getItemCount() = pref.size
    companion object{
        const val TAG = "com.example.FindFun.timer.timer_length"
        const val fragment = "com.example.FindFun.SettingsActivityFragment"

    }
}