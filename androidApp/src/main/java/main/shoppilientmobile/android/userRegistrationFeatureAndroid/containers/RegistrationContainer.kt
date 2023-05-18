package main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers

import main.shoppilientmobile.userRegistrationFeature.repositories.*
import main.shoppilientmobile.userRegistrationFeature.useCases.ConfirmUserRegistrationUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCaseImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class RegistrationContainer(
    userRepository: UserRepository,
    registrationRepository: RegistrationRepository,
    userRoleLocalDataSource: UserRoleLocalDataSource,
) {

    val userRoleRepository = UserRoleRepository(
        userRoleLocalDataSource = userRoleLocalDataSource,
    )

    val registerUserUseCase = RegisterUserUseCase(
        registrationRepository,
    )

    val registerAdminUseCase = RegisterAdminUseCaseImpl(
        registrationRepository = registrationRepository,
        userRepository = userRepository,
    )

    val confirmUserRegistrationUseCase = ConfirmUserRegistrationUseCase(
        registrationRepository,
        userRepository,
    )
}