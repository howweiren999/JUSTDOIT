package com.example.FindFun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.buttonSignUp
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonSignUp.setOnClickListener {
            createUser(
                User(editTextUsername.text.toString(),
                    editTextFullName.text.toString(),
                    editTextEmail.text.toString(),
                    (editTextPhone.text.toString().toInt()),
                    editTextPassword.text.toString())
            )

        }
    }

    private fun createUser(user: User) {
        val url = getString(R.string.url_server) + getString(R.string.url_user_create) + "?userName=" + user.userName +
                "&name=" + user.name + "&email=" + user.email + "&contact=" + user.contact + "&password=" + user.password

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()

                        if (success.equals("1")) {
                            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                            //Add record to user list
                            //userList.add(user)
                            //Explicit Intent
                            val intent = Intent(this, MainActivity::class.java)
                            //start the second activity. With no return value
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext,"Record not saved", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Main", "Response: %s".format(e.message.toString()))
                }
            },
            Response.ErrorListener { response ->
                Log.d("Main", "Response: %s".format(response.message.toString()))
            }
        )

        // Access the RequestQueue through your singleton class.
        jsonObjectRequest.tag = TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    companion object {
        const val TAG = "com.example.healthcare"
    }
}
