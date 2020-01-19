package com.example.FindFun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.rv_child_history.*
import org.json.JSONObject

class ToDoListHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rv_child_history)
        var token = getSharedPreferences("info", Context.MODE_PRIVATE)

        val userName = token.getString("userName"," ")

        val url = getString(R.string.url_server) + getString(R.string.url_todolist_read_one) + "?userName=" + userName

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                        textViewTodoName.text = jsonResponse.getString("todoName")
                        textViewDateCreated.text = jsonResponse.getString("createdAt")

                        //userList.add(user)

                        //Toast.makeText(applicationContext, "Record found :", Toast.LENGTH_LONG).show()
                        //progress.visibility = View.GONE
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))
                    //progress.visibility = View.GONE

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
                //progress.visibility = View.GONE
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

    companion object{
        const val TAG = "com.example.raindy"
    }
}