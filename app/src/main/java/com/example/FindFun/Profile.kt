package com.example.FindFun

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var token = getSharedPreferences("info", Context.MODE_PRIVATE)

        val userName = token.getString("userName"," ")
        val password = token.getString("password", " ")


        val url = getString(R.string.url_server) + getString(R.string.url_user_read_one) + "?userName=" + userName + "&password=" + password

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                            textViewUsername.text = jsonResponse.getString("userName")
                            textViewFullName.text = jsonResponse.getString("name")
                            textViewPhoneNumber.text = jsonResponse.getString("contact").toString()
                            textViewEmail.text = jsonResponse.getString("email")

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