package com.example.FindFun

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_add_pref.*

class AddPrefActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pref)
        buttonSave.setOnClickListener {
            saveUser()
        }
    }
    private fun saveUser(){

        if(TextUtils.isEmpty(editTextTimerName.text)){
            editTextTimerName.setError(getString(R.string.error_value_required))
            return
        }
        if(TextUtils.isEmpty(editTextTimerDesc.text)){
            editTextTimerDesc.setError(getString(R.string.error_value_required))
            return
        }
        if(TextUtils.isEmpty(editTextTimerVal.text)){
            editTextTimerVal.setError(getString(R.string.error_value_required))
            return
        }
        if(Integer.parseInt(editTextTimerVal.text.toString())>60 || Integer.parseInt(editTextTimerVal.text.toString())<1){
            editTextTimerVal.setError("Only 1-60 minutes is allowed")
            return
        }
        val name = editTextTimerName.text.toString()
        val description = editTextTimerDesc.text.toString()
        val value = Integer.parseInt(editTextTimerVal.text.toString())

        val intent = Intent()
        intent.putExtra(EXTRA_TIMER_NAME, name)
        intent.putExtra(EXTRA_TIMER_DESC, description)
        intent.putExtra(EXTRA_TIMER_VALUE, value)

        setResult(Activity.RESULT_OK, intent)

        finish()
    }
    companion object{
        const val EXTRA_TIMER_NAME = "com.example.FindFun.NAME" //As an instance.
        const val EXTRA_TIMER_DESC = "com.example.FindFun.DESC" //As an instance
        const val EXTRA_TIMER_VALUE = "com.example.FindFun.VALUE"
    }
}
