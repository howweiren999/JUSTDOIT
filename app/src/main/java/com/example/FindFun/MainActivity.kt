package com.example.FindFun

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var token = getSharedPreferences("info", Context.MODE_PRIVATE)
        var loginStatus = token.getString("loginStatus","false")
        //var loginStatus = "false"

        if(loginStatus == "false") {
            buttonSignUp.setOnClickListener {
                val intent = Intent(this, SignUp::class.java)

                //start the second activity. With no return value
                startActivity(intent)
            }

            buttonLogin.setOnClickListener {
                //JSONProccess1(etUsername.text.toString(),etPassword.text.toString())

                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                val loginURL =
                    getString(R.string.url_server) + getString(R.string.url_user_read_one) + "?userName=" + username + "&password=" + password

                //output = (TextView) findViewById(R.id.jsonData);
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, loginURL, null,
                    Response.Listener { response ->
                        try {
                            if (response != null) {
                                val strResponse = response.toString()
                                val jsonResponse = JSONObject(strResponse)

                                if (jsonResponse != null) {
                                    Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT)
                                        .show()


                                    val intent = Intent(this, HomePage::class.java)


                                    var editor = token.edit()
                                    editor.putString("userName", username)
                                    editor.putString("password", password)
                                    editor.putString("loginStatus", "true")
                                    editor.commit()
                                    startActivity(intent)

                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { e ->
                        Log.e("Volley", "Error" + e.message.toString())
                        Toast.makeText(this, "Username or password wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

            }
        }else
        {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }

    /*private fun JSONProccess1(username:String,password:String) {
        val loginURL = getString(R.string.url_server) + getString(R.string.url_user_read_one) + "?userName=" + username + "&password=" + password

        //output = (TextView) findViewById(R.id.jsonData);
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, loginURL, null,
            Response.Listener { response ->
                try {
                    if (response != null) {
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                        if(jsonResponse != null){
                            Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()


                            val intent = Intent(this, Profile::class.java)
                            intent.putExtra("userName", username)

                            var editor = token.edit()
                            editor.putString("userName",username)
                            editor.commit()
                            startActivity(intent)

                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { e ->
                Log.e("Volley", "Error" + e.message.toString())
                Toast.makeText(this, "Username or password wrong", Toast.LENGTH_SHORT).show()
            }
        )
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }*/
}
