package com.ainsigne.mobilesocialblogapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class CallAppService : FirebaseMessagingService(), LifecycleObserver {
    private var isAppInForeground = false


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Config.updateToken(token)
        Log.d(" On New Token ", " On New $token")

    }


    override fun onMessageReceived(messageReceived: RemoteMessage) {
        super.onMessageReceived(messageReceived)
        Log.d(" On Message Received ", " On New ${messageReceived.data} ${messageReceived.notification}")
        if (messageReceived.data.containsKey("calledId")) {
            Log.d(" On Message Received ", " On New $messageReceived")
//            var broadcast = Intent()
//            val bundle = Bundle()
//            bundle.putString("calledId",messageReceived.data["calledId"])
//            broadcast.putExtras(bundle)
//            broadcast.setAction("OPEN_NEW_ACTIVITY")
//            sendBroadcast(broadcast)


            val bundle = Bundle()
            bundle.putString("calledId",messageReceived.data["calledId"])

            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtras(bundle)
// Create the TaskStackBuilder
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            try {
                if(!isAppInForeground)
                    resultPendingIntent?.send()
                else
                    showNotif(resultPendingIntent)
            }catch (e : PendingIntent.CanceledException){
                e.printStackTrace()
            }

        }
    }

    var CHANNEL_ID = "MobSoc31"
    var NOTIFICATION_CHANNEL_NAME = "MobSoc"
    var NOTIFICATION_ID = 31

    private fun showNotif(resultPendingIntent : PendingIntent?)
    {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentIntent(resultPendingIntent)

        }
        builder.setSmallIcon(R.drawable.ic_launcher_background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_LOW
            val notificationChannel = NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
            with(notificationManager) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }else{
            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegroundStart() {
        isAppInForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onForegroundStop() {
        isAppInForeground = false
    }

    override fun onDestroy() {
        super.onDestroy()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onMessageSent(messageSent: String) {
        super.onMessageSent(messageSent)
        Log.d(" On Message Sent ", " On New $messageSent")
    }
}