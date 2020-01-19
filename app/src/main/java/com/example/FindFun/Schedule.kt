package com.example.FindFun

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_schedule.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


const val EXTRA_MESSAGE = "EXTRA_MESSAGE"


class Schedule : AppCompatActivity() {


    private val cal = Calendar.getInstance()
    private fun setMyTimeFormat() : String{
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat)
        return sdf.format(cal.getTime())
    }

    private fun setMyDateFormat() : String{
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat)
        return sdf.format(cal.getTime())
    }

    private fun myTimePicker() :TimePickerDialog.OnTimeSetListener{
        val timeSetListener = object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(p0: TimePicker?,
                                   hour: Int,
                                   minute: Int){
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                textViewTime.text = setMyTimeFormat()
            }
        }
        return timeSetListener
    }

    val dateSetListener = object :DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            textViewHDate.text = setMyDateFormat()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        var mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode = 100
        var mPendingIntent: PendingIntent? = null
        var token = getSharedPreferences("info", Context.MODE_PRIVATE)

        val userName = token.getString("userName"," ")


        buttonHistory.setOnClickListener{
            val intent = Intent(this, ScheduleHistory::class.java)

        //start the second activity. With no return value
        startActivity(intent)
    }

        buttonSetAlarm.setOnClickListener{

            var setTime = Calendar.getInstance()
            var schduleTime = textViewTime.text.split(":")
            var scheduleDate = textViewHDate.text.split("/")
            val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
            val currentDateTimeStringNoSpace = currentDateTimeString.replace("\\s".toRegex(), "")


            setTime.set(Calendar.HOUR_OF_DAY,schduleTime[0].toInt())
            setTime.set(Calendar.MINUTE,schduleTime[1].toInt())
            setTime.set(Calendar.SECOND,0)

            if(textViewHDate.text =="DD/MM/YYYY")
                textViewHDate.text = "Today"
            else{
                setTime.set(Calendar.YEAR, scheduleDate[2].toInt())
                setTime.set(Calendar.MONTH, scheduleDate[1].toInt())
                setTime.set(Calendar.DAY_OF_YEAR, scheduleDate[0].toInt())
            }

            val sentIntent = Intent(this,MyAlarmReceiver::class.java)
            sentIntent.putExtra(EXTRA_MESSAGE,editTextMsg.text.toString())
            mPendingIntent = PendingIntent.getBroadcast(this,requestCode,sentIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, setTime.timeInMillis,mPendingIntent)
            Toast.makeText(this,"Schedule Reminder set up from ${textViewTime.text}:00 in ${textViewHDate.text}"
                ,Toast.LENGTH_SHORT).show()




            createSchduleList(
                ScheduleList(
                    textViewTime.text.toString()+"|"+textViewHDate.text.toString(),
                    editTextMsg.text.toString(),
                    userName.toString(),

                    currentDateTimeStringNoSpace
                )
            )
        }



        buttonCancel.setOnClickListener {
            if(mPendingIntent!=null)
                mAlarmManager.cancel(mPendingIntent)
            Toast.makeText(this,"Schedule Reminder has been stopped",Toast.LENGTH_SHORT).show()

        }

        buttonSetTime.setOnClickListener { TimePickerDialog (
            this,myTimePicker(),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()}

        buttonSetDate.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view:View){
                DatePickerDialog(this@Schedule,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
        )
    }

    private fun createSchduleList(scheduleList: ScheduleList) {
        val url = getString(R.string.url_server) + getString(R.string.url_scheduleList_create) +
                "?date=" + scheduleList.date + "&message=" + scheduleList.message + "&userName=" + scheduleList.userName + "&createdDate=" + scheduleList.createdDate

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
                            val intent = Intent(this, HomePage::class.java)
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
        jsonObjectRequest.tag = SignUp.TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}


