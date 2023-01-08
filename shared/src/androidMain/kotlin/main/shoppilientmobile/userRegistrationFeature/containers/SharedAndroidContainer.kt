package main.shoppilientmobile.userRegistrationFeature.containers

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.core.builders.AsynchronousHttpClientBuilder
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
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.ui.RegistrationAlerter
import main.shoppilientmobile.userRegistrationFeature.useCases.ListenToRegistrationsUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCaseImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class SharedAndroidContainer(
    context: Context,
) {
    private val useCasesFactory = UseCasesFactory.getInstance(context)
    val registerAdminUseCase = useCasesFactory.registerAdminUseCase
    val registerUserUseCase = useCasesFactory.registerUserUseCase
    val productRepository = RepositoriesFactory.getInstance(context).productRepository
    val registrationAlerter = AlertersFactory
        .getInstance(useCasesFactory.listenToRegistrationsUseCase)
        .registrationAlerter

    private class AlertersFactory private constructor(
        listenToRegistrationsUseCase: ListenToRegistrationsUseCase,
    ) {
        val registrationAlerter = RegistrationAlerter(
            listenToRegistrationsUseCase = listenToRegistrationsUseCase,
        )

        companion object {
            private var singleton: AlertersFactory? = null

            fun getInstance(listenToRegistrationsUseCase: ListenToRegistrationsUseCase): AlertersFactory {
                if (singleton == null) {
                    singleton = AlertersFactory(listenToRegistrationsUseCase)
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
        val userRepository = UserRepositoryImpl(
            userRemoteDataSource = apisFactory.userApi,
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
            userBuilder = BuildersFactory.userBuilder,
            securityTokenKeeper = keepersFactory.securityTokenKeeper,
        )
        val userApi = UserApiWithoutKtor(
            httpClient = HttpClientsFactory.httpClient,
            streamingHttpClient = HttpClientsFactory.streamingHttpClient,
            securityTokenKeeper = keepersFactory.securityTokenKeeper
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

    private class KeepersFactory private constructor(
        context: Context,
    ) {
        companion object {
            private var singleton: KeepersFactory? = null

            fun getInstance(context: Context): KeepersFactory {
                if (singleton == null) {
                    singleton = KeepersFactory(context)
                }
                return singleton!!
            }
        }

        val securityTokenKeeper = SecurityTokenKeeperImpl(
            keyValueLocalStorage = KeyValueStoragesFactory.getInstance(context).keyValueLocalStorage
        )
    }

    private class KeyValueStoragesFactory private constructor(
        context: Context,
    ) {
        companion object {
            private var singleton: KeyValueStoragesFactory? = null

            fun getInstance(context: Context): KeyValueStoragesFactory {
                if (singleton == null) {
                    singleton = KeyValueStoragesFactory(context)
                }
                return singleton!!
            }
        }

        val keyValueLocalStorage = KeyValueLocalStorage(
            dataStore = DataStoreFactory.getInstance(context).dataSource
        )
    }

    private object BuildersFactory {
        val userBuilder = UserBuilderImpl()
    }

    private object HttpClientsFactory {
        val httpClient = AsynchronousHttpClientImpl()
        val streamingHttpClient = StreamingHttpClientAndroid()
    }

    private class DataStoreFactory private constructor(
        context: Context,
    ) {
        companion object {
            private var singleton: DataStoreFactory? = null

            fun getInstance(context: Context): DataStoreFactory {
                if (singleton == null) {
                    singleton = DataStoreFactory(context)
                }
                return singleton!!
            }
        }

        val dataSource = PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("preferences")
            }
        )
    }
}