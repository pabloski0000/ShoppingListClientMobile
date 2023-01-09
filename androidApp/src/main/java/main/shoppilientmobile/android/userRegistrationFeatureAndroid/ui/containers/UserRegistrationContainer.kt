package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.containers

import android.content.Context
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.notifications.RegistrationAlerterAndroid
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.notifications.RegistrationNotificationChannel
import main.shoppilientmobile.userRegistrationFeature.containers.SharedAndroidContainer
import main.shoppilientmobile.userRegistrationFeature.ui.RegistrationAlerter
import main.shoppilientmobile.userRegistrationFeature.useCases.ListenToRegistrationsUseCase

class UserRegistrationContainer(
    context: Context,
) {
    private val sharedAndroidContainer = SharedAndroidContainer(
        context = context,
    )
    val registrationAlerter = AlertersFactory
        .getInstance(sharedAndroidContainer.listenToRegistrationsUseCase, context)
        .registrationAlerter

    private class AlertersFactory private constructor(
        listenToRegistrationsUseCase: ListenToRegistrationsUseCase,
        context: Context,
    ) {
        private val notificationChannelsFactory = NotificationChannelsFactory.getInstance(context)
        val registrationAlerterAndroid = RegistrationAlerterAndroid(
            registrationNotificationChannel = notificationChannelsFactory.registrationNotificationChannel,
            context = context,
        )
        val registrationAlerter = RegistrationAlerter(
            listenToRegistrationsUseCase = listenToRegistrationsUseCase,
            alertee = registrationAlerterAndroid,
        )

        companion object {
            private var singleton: AlertersFactory? = null

            fun getInstance(
                listenToRegistrationsUseCase: ListenToRegistrationsUseCase,
                context: Context,
            ): AlertersFactory {
                if (singleton == null) {
                    singleton = AlertersFactory(listenToRegistrationsUseCase, context)
                }
                return singleton!!
            }
        }
    }

    private class NotificationChannelsFactory private constructor(
        context: Context,
    ) {
        val registrationNotificationChannel = RegistrationNotificationChannel(
            context = context,
        )

        companion object {
            private var singleton: NotificationChannelsFactory? = null

            fun getInstance(context: Context): NotificationChannelsFactory {
                if (singleton == null) {
                    singleton = NotificationChannelsFactory(context)
                }
                return singleton!!
            }
        }
    }
}