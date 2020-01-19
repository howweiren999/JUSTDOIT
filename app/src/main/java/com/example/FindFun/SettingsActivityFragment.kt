package com.example.FindFun

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import com.example.FindFun.R.xml.preferences
import com.pavelsikun.seekbarpreference.SeekBarPreferenceCompat

class SettingsActivityFragment : PreferenceFragmentCompat() {
    var time:Int? = 10
    val adapter = PrefListAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var token:SharedPreferences = context!!.getSharedPreferences("info", Context.MODE_PRIVATE)
        time = token.getInt("currentTime", 10)
        val seekbar:SeekBarPreferenceCompat = findPreference(TAG) as SeekBarPreferenceCompat
        if(time!=null){

            seekbar.currentValue=Integer.parseInt(time.toString()

            )
        }
        Log.d("Gay", "gay"+time)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {



        addPreferencesFromResource(preferences)



    }
    companion object{
        const val TAG = "com.example.FindFun.timer.timer_length"
        const val fragment = "com.example.FindFun.SettingsActivityFragment"
        fun newInstance(time:Int): SettingsActivityFragment {
            val frag = SettingsActivityFragment()
            val bundle = Bundle()
            bundle.putInt("currentTime", time)
            frag.arguments = bundle
            return frag
        }
    }
}