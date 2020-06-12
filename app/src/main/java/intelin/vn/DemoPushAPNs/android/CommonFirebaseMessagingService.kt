package intelin.vn.DemoPushAPNs.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Copyright by Intelin.
 * Creator: Do Anh Tam
 */
class CommonFirebaseMessagingService : FirebaseMessagingService() {
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FIREBASE", remoteMessage.data.toString())

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)
        val data = remoteMessage.data
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (data["idClose"] != null) {
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(data["idClose"]!!.toInt())
            return
        }
        notification(remoteMessage.notification, data)
    }

    private fun notification(msg: RemoteMessage.Notification?, data: Map<String, String>) {
        val CHANNEL_ID = "my_channel_01"
        val soundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val mBuilder = NotificationCompat.Builder(this)
                .setSound(soundUri)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(data["title"])
                .setAutoCancel(true)
                .setContentText(data["body"])
                .setStyle(NotificationCompat.BigTextStyle())
                .setChannelId(CHANNEL_ID)
                .setPriority(Notification.PRIORITY_HIGH)
            val resultIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                1,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            mBuilder.setContentIntent(pendingIntent)
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(channel)
            mNotificationManager.notify((data["id"] ?: error("0")).toInt(), mBuilder.build())
        } else {
            val mBuilder = NotificationCompat.Builder(this)
                .setSound(soundUri)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(msg?.title)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle())
                .setContentText(msg?.body)
                .setPriority(Notification.PRIORITY_HIGH)
            val resultIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                1,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pendingIntent)
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            (msg?.tag)?.toInt()?.let { mNotificationManager.notify(it, mBuilder.build()) }
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}