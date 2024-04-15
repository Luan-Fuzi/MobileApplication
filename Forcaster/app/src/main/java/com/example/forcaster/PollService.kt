package com.example.forcaster

import android.app.AlarmManager
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.forcaster.database.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PollService:IntentService("PollService") {
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onHandleIntent(intent: Intent?) {
        Log.d("PollService", "onHandleIntent: ")
//    获取今日天气
        serviceScope.launch {
            val result = WeatherRepository(this@PollService).fetchWeatherNow()
            if(result.code!="200"){
                Log.d("PollService", "onHandleIntent: ${result.code}")
                return@launch
            }
            val weatherNow = result.now
//            将今日天气发送到通知栏
            val channel = NotificationChannel("weather", "天气", NotificationManager.IMPORTANCE_HIGH)

            val notificationmanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationmanager.createNotificationChannel(channel)

//            val notification = NotificationCompat.Builder(this@PollService, "weather")
            val notification = NotificationCompat.Builder(this@PollService, "weather")
                .setSmallIcon(R.drawable.w100)
                .setContentTitle("今日天气")
                .setContentText("${weatherNow.text}，${weatherNow.temp}度")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            notificationmanager.notify(1, notification)

        }


}

companion object{
        fun setServiceAlarm(context: Context, isOn: Boolean) {

            val intent = Intent(context, PollService::class.java)
            val pendingIntent = PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (isOn) {
                alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    1000 * 60,
                    pendingIntent
                )
                Log.d("PollService", "setServiceAlarm: ")

            } else {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
                Log.d("PollService", "cancelServiceAlarm: ")
            }
        }
    }



}