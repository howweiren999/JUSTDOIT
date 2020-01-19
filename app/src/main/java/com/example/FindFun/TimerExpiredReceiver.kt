package com.example.FindFun

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.FindFun.util.NotificationUtil
import com.example.FindFun.util.PrefUtil


class TimerExpiredReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
