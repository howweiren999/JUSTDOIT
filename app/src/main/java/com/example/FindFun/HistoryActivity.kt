package com.example.FindFun

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class HistoryActivity :AppCompatActivity() {
    lateinit var historyRecord:ArrayList<ToDoList>
    lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist_history)

        historyRecord=ArrayList<ToDoList>()
        adapter=HistoryAdapter(this)
        adapter.setHistoryRecords(historyRecord)

        val recyclerView=findViewById<RecyclerView>(R.id.todolist_historyview)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_sync -> {
                if(isConnected()){
                    syncHistory()

                }else{
                    Toast.makeText(applicationContext, "Offline", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun syncHistory() {
        var token = getSharedPreferences("info", Context.MODE_PRIVATE)

        val userName = token.getString("userName"," ")

        val url = getString(R.string.url_server) + getString(R.string.url_todolist_read) + "?userName=" + userName

        //Delete all user records
        historyRecord.clear()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val jsonArray: JSONArray = jsonResponse.getJSONArray("records")

                        val size: Int = jsonArray.length()

                        for(i in 0 until size){
                            var jsonUser: JSONObject = jsonArray.getJSONObject(i)
                            var hist: ToDoList = ToDoList(jsonUser.getString("todoName"),
                                jsonUser.getString("createdAt"), jsonUser.getString("userName"))

                            historyRecord.add(hist)
                        }
                        Toast.makeText(applicationContext, "Record found :$size", Toast.LENGTH_LONG).show()
                        adapter.notifyDataSetChanged()
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
    private fun isConnected(): Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    companion object{
        const val TAG = "com.example.FindFun"
    }


}