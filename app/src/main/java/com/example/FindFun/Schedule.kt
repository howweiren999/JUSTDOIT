package com.example.FindFun

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_schedule.*
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
            textViewDate.text = setMyDateFormat()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        var mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode = 100
        var mPendingIntent: PendingIntent? = null


        buttonSetAlarm.setOnClickListener{

            var setTime = Calendar.getInstance()
            var schduleTime = textViewTime.text.split(":")
            var scheduleDate = textViewDate.text.split("/")

            setTime.set(Calendar.HOUR_OF_DAY,schduleTime[0].toInt())
            setTime.set(Calendar.MINUTE,schduleTime[1].toInt())
            setTime.set(Calendar.SECOND,0)

            if(textViewDate.text =="DD/MM/YYYY")
                textViewDate.text = "Today"
            else{
                setTime.set(Calendar.YEAR, scheduleDate[2].toInt())
                setTime.set(Calendar.MONTH, scheduleDate[1].toInt())
                setTime.set(Calendar.DAY_OF_YEAR, scheduleDate[0].toInt())
            }

            val sentIntent = Intent(this,MyAlarmReceiver::class.java)
            sentIntent.putExtra(EXTRA_MESSAGE,editTextMsg.text.toString())
            mPendingIntent = PendingIntent.getBroadcast(this,requestCode,sentIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, setTime.timeInMillis,mPendingIntent)
            Toast.makeText(this,"Schedule Reminder set up from ${textViewTime.text}:00 in ${textViewDate.text}"
                ,Toast.LENGTH_SHORT).show()}



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
}


