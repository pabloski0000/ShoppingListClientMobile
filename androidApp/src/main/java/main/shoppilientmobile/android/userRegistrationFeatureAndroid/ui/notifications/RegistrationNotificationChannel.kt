package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat

class RegistrationNotificationChannel(
    private val context: Context,
) {
    init {
        val notificationChannel = createNotificationChannel()
        subscribeNotificationChannelToDevice(notificationChannel)
    }

    fun showNotification(notificationId: Int, notification: Notification) {
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel(): NotificationChannel {
        val channelName = "users_channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        return NotificationChannel("0", channelName, importance)
    }

    private fun subscribeNotificationChannelToDevice(notificationChannel: NotificationChannel) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}