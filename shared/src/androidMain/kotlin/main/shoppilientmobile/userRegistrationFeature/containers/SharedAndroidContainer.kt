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
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.NewUserRegistrationApi
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApiWithoutKtor
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCaseImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class SharedAndroidContainer(
    private val context: Context,
) {
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("preferences")
        }
    )
    private val asynchronousHttpClient = AsynchronousHttpClientImpl()
    private val securityTokenKeeper = SecurityTokenKeeperImpl(
        keyValueLocalStorage = KeyValueLocalStorage(
            dataStore = dataStore
        )
    )
    private val userRepository: UserRepository = UserRepositoryImpl(
        userRemoteDataSource = UserApiWithoutKtor(
            httpClient = asynchronousHttpClient,
            streamingHttpClient = StreamingHttpClientAndroid(),
            securityTokenKeeper = securityTokenKeeper,
        ),
    )
    private val userBuilder = UserBuilderImpl()

    val registerAdminUseCase: RegisterAdminUseCase = createRegisterAdminUseCase()
    val registerUserUseCase: RegisterUserUseCase = createRegisterUserUseCase()
    val productRepository = createProductRepository()

    private fun createRegisterAdminUseCase(): RegisterAdminUseCase {
        return RegisterAdminUseCaseImpl(
            userRepository = userRepository,
            userBuilder = userBuilder,
        )
    }

    private fun createRegisterUserUseCase(): RegisterUserUseCase {
        return RegisterUserUseCase(
            userRepository = userRepository,
            newUserRegistrationRepository = RegistrationRepositoryImpl(
                newUserRegistrationRemoteDataSource = NewUserRegistrationApi(
                    httpClient = asynchronousHttpClient,
                    securityTokenKeeper = securityTokenKeeper,
                )
            ),
            userBuilder = userBuilder,
        )
    }

    private fun createProductRepository(): ProductRepository {
        val productApi: ProductApi = ProductApi(
            httpClient = AsynchronousHttpClientBuilder().addJsonParser().build()
        )
        val productRemoteDataSource = ProductRemoteDataSource(
            productApi = productApi
        )
        return ProductRepository(
            productRemoteDataSource = productRemoteDataSource
        )
    }
}