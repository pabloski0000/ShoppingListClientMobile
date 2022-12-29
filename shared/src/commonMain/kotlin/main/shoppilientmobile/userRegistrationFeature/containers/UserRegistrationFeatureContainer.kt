package main.shoppilientmobile.userRegistrationFeature.containers

import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.core.builders.SerializableHttpClientBuilder
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserRemoteDataSourceImpl
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApiImpl
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCaseImpl

class UserRegistrationFeatureContainer {
    private val userApiImpl = UserApiImpl(
        serializableHttpClient = SerializableHttpClientBuilder().build()
    )
    private val userRepositoryImpl = UserRepositoryImpl(
        userRemoteDataSource = UserRemoteDataSourceImpl(
            userApi = userApiImpl
        )
    )
    val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl(
        userRepository = userRepositoryImpl,
        userBuilder = UserBuilderImpl(),
    )
}