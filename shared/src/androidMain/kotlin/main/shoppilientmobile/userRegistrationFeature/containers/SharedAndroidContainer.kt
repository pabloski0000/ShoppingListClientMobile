package main.shoppilientmobile.userRegistrationFeature.containers

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.core.AsynchronousHttpClientImpl
import main.shoppilientmobile.core.builders.AsynchronousHttpClientBuilder
import main.shoppilientmobile.core.localStorage.KeyValueLocalStorage
import main.shoppilientmobile.core.localStorage.SecurityTokenKeeper
import main.shoppilientmobile.createListFeature.dataSources.ProductRemoteDataSource
import main.shoppilientmobile.createListFeature.dataSources.apis.ProductApi
import main.shoppilientmobile.createListFeature.repositories.ProductRepository
import main.shoppilientmobile.dataSources.AsynchronousHttpClientAndroid
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserRemoteDataSourceImpl
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApi
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApiWithoutKtor
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCaseImpl

class SharedAndroidContainer(
    private val context: Context,
) {
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("preferences")
        }
    )

    val registerUserUseCase: RegisterUserUseCase = createRegisterUserUseCase()
    val productRepository = createProductRepository()

    private fun createRegisterUserUseCase(): RegisterUserUseCase {
        val userApi: UserApi = UserApiWithoutKtor(
            asynchronousHttpClient = AsynchronousHttpClientAndroid(),
            securityTokenKeeper = SecurityTokenKeeper(
                keyValueLocalStorage = KeyValueLocalStorage(
                    dataStore = dataStore
                )
            ),
        )
        val userRepository: UserRepository = UserRepositoryImpl(
            userRemoteDataSource = UserRemoteDataSourceImpl(
                userApi = userApi,
            ),
        )
        return RegisterUserUseCaseImpl(
            userRepository = userRepository,
            userBuilder = UserBuilderImpl(),
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