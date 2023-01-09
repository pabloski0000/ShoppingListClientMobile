package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.containers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.notifications.RegistrationAlerterAndroid
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.notifications.RegistrationNotificationChannel
import main.shoppilientmobile.core.localStorage.KeyValueLocalStorage
import main.shoppilientmobile.core.localStorage.SecurityTokenKeeperImpl
import main.shoppilientmobile.core.remote.AsynchronousHttpClientImpl
import main.shoppilientmobile.createListFeature.dataSources.ProductRemoteDataSource
import main.shoppilientmobile.createListFeature.dataSources.apis.ProductApi
import main.shoppilientmobile.createListFeature.repositories.ProductRepository
import main.shoppilientmobile.dataSources.StreamingHttpClientAndroid
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.RegistrationApi
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApiWithoutKtor
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.ui.RegistrationAlerter
import main.shoppilientmobile.userRegistrationFeature.useCases.ListenToRegistrationsUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCaseImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class RegistrationContainer(
    dataStore: DataStore<Preferences>
) {
    private val httpClient = AsynchronousHttpClientImpl()

    private val streamingHttpClient = StreamingHttpClientAndroid()

    private val keyValueLocalStorage = KeyValueLocalStorage(
        dataStore = dataStore
    )

    private val securityTokenKeeper = SecurityTokenKeeperImpl(
        keyValueLocalStorage = keyValueLocalStorage,
    )

    private val registrationApi = RegistrationApi(
        httpClient = httpClient,
        securityTokenKeeper = securityTokenKeeper,
    )

    private val userApi = UserApiWithoutKtor(
        httpClient = httpClient,
        streamingHttpClient = streamingHttpClient,
        securityTokenKeeper = securityTokenKeeper
    )

    private val userRepository = UserRepositoryImpl(
        userRemoteDataSource = userApi,
        keyValueLocalStorage = keyValueLocalStorage,
    )

    private val registrationRepository = RegistrationRepositoryImpl(
        registrationRemoteDataSource = registrationApi,
    )

    val registerUserUseCase = RegisterUserUseCase(
        registrationRepository = registrationRepository,
    )

    val registerAdminUseCase = RegisterAdminUseCaseImpl(
        registrationRepository = registrationRepository,
        userRepository = userRepository,
    )

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

    private class UseCasesFactory private constructor(
        context: Context,
    ) {
        private val repositoriesFactory = RepositoriesFactory.getInstance(context)
        val registerAdminUseCase = RegisterAdminUseCaseImpl(
            registrationRepository = repositoriesFactory.registrationRepository,
            userRepository = repositoriesFactory.userRepository,
        )
        val registerUserUseCase = RegisterUserUseCase(
            registrationRepository = repositoriesFactory.registrationRepository,
        )
        val listenToRegistrationsUseCase = ListenToRegistrationsUseCase(
            registrationRepository = repositoriesFactory.registrationRepository,
            userRepository = repositoriesFactory.userRepository,
        )

        companion object {
            private var singleton: UseCasesFactory? = null

            fun getInstance(context: Context): UseCasesFactory {
                if (singleton == null) {
                    singleton = UseCasesFactory(context)
                }
                return singleton!!
            }
        }

    }

    private class RepositoriesFactory private constructor(
        context: Context,
    ) {
        private val apisFactory = ApisFactory.getInstance(context)
        val registrationRepository = RegistrationRepositoryImpl(
            registrationRemoteDataSource = apisFactory.registrationApi,
        )

        val productRepository = ProductRepository(
            productRemoteDataSource = DataSourcesFactory.getInstance(context).productRemoteDataSource
        )

        companion object {
            private var singleton: RepositoriesFactory? = null

            fun getInstance(context: Context): RepositoriesFactory {
                if (singleton == null) {
                    singleton = RepositoriesFactory(context)
                }
                return singleton!!
            }
        }
    }

    private class DataSourcesFactory private constructor(
        context: Context,
    ) {
        val productRemoteDataSource = ProductRemoteDataSource(
            productApi = ApisFactory.getInstance(context).productApi,
        )

        companion object {
            private var singleton: DataSourcesFactory? = null

            fun getInstance(context: Context): DataSourcesFactory {
                if (singleton == null) {
                    singleton = DataSourcesFactory(context)
                }
                return singleton!!
            }
        }
    }

    private class ApisFactory private constructor(
        context: Context,
    ) {
        private val keepersFactory = KeepersFactory.getInstance(context)
        val registrationApi = RegistrationApi(
            httpClient = HttpClientsFactory.httpClient,
            securityTokenKeeper = keepersFactory.securityTokenKeeper,
        )

        val productApi = ProductApi(
            httpClient = HttpClientsFactory.httpClient,
        )

        companion object {
            private var singleton: ApisFactory? = null

            fun getInstance(context: Context): ApisFactory {
                if (singleton == null) {
                    singleton = ApisFactory(context)
                }
                return singleton!!
            }
        }
    }
}