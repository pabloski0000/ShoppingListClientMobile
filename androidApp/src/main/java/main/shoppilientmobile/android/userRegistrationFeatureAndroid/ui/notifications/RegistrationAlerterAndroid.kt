package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.ui.RegistrationAlertee
import main.shoppilientmobile.userRegistrationFeature.ui.RegistrationAlerter

class RegistrationAlerterAndroid(
    private val registrationNotificationChannel: RegistrationNotificationChannel,
    private val context: Context,
): RegistrationAlertee {
    private val notificationAndItsId = mutableMapOf<Int, Notification>()

    override fun newRegistration(registration: Registration) {
        val notification = adaptRegistration(registration)
        val notificationId = notificationAndItsId.size
        showNotification(notificationId, notification)
        saveNotificationForFutureOperations(notificationId, notification)
    }

    private fun showNotification(notificationId: Int, notification: Notification) {
        registrationNotificationChannel.showNotification(notificationId, notification)
    }

    private fun saveNotificationForFutureOperations(notificationId: Int, notification: Notification) {
        notificationAndItsId[notificationId] = notification
    }

    private fun adaptRegistration(registration: Registration): Notification {
        return NotificationCompat.Builder(context, "jsd")
            .setContentTitle(registration.nickname)
            .setContentTitle(registration.signature)
            .build()
    }

}