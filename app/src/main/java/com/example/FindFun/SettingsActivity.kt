package com.example.FindFun

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.pavelsikun.seekbarpreference.SeekBarPreferenceCompat
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.activity_timer.toolbar
import org.json.JSONArray
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1
    lateinit var prefList: ArrayList<TimerPref>
    lateinit var adapter: PrefListAdapter
    private var userName =" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        var token = getSharedPreferences("info", Context.MODE_PRIVATE)
        userName = token.getString("userName","").toString()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        prefList = ArrayList<TimerPref>()
        syncPref()
        adapter = PrefListAdapter(this)
        adapter.setPref(prefList)



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPref)
        recyclerView.adapter = adapter
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        fabAddPref.setOnClickListener {
            val intent = Intent(this, AddPrefActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                data?.let{
                    val timerPref = TimerPref(userName, it.getStringExtra(AddPrefActivity.EXTRA_TIMER_NAME), it.getStringExtra(AddPrefActivity.EXTRA_TIMER_DESC), it.getIntExtra(AddPrefActivity.EXTRA_TIMER_VALUE, 10))
                    createPref(timerPref)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun createPref(timerPref: TimerPref) {
        val url = getString(R.string.url_server) + getString(R.string.timerPrefURL) + "?username=" + timerPref.username +
                "&timerName=" + timerPref.timerName + "&timerValue=" + timerPref.time+ "&timerDesc=" + timerPref.timerDesc

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val success: String = response.get("success").toString()

                        if(success.equals("1")){
                            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                            //Add record to user list
                            prefList.add(timerPref)

                            syncPref()
                        }else{
                            Toast.makeText(applicationContext, "Record not saved", Toast.LENGTH_LONG).show()
                        }
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        jsonObjectRequest.tag = TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    private fun syncPref() {
        val url = getString(R.string.url_server) + getString(R.string.url_timerpref_read)+"?username=" +userName

        //Delete all user records
        prefList.clear()
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val strResponse = response.toString()
                        val jsonResponse = JSONObject(strResponse)
                        val jsonArray: JSONArray = jsonResponse.getJSONArray("records")

                        val size: Int = jsonArray.length()

                        for (i in 0..size - 1) {
                            var jsonUser: JSONObject = jsonArray.getJSONObject(i)
                            var pref:TimerPref = TimerPref(
                                jsonUser.getString("username"),
                                jsonUser.getString("timerName"),
                                jsonUser.getString("timerDesc"),
                                jsonUser.getInt("timerValue"))

                            prefList.add(pref)

                        }
                        Toast.makeText(
                            applicationContext,
                            "Record found :" + size,
                            Toast.LENGTH_LONG
                        ).show()
                        adapter.notifyDataSetChanged()

                    }
                } catch (e: Exception) {
                    Log.d("Main", "Response: %s".format(e.message.toString()))

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
            }
        )
        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        jsonObjectRequest.tag = TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    private fun isConnected(): Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
    companion object{
        const val TAG = "com.example.FindFun"
    }
}
