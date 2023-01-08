package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException

class UserRemoteDataSourceImpl {

    private suspend fun throwRemoteExceptionIfGoesWrong(remoteTask: suspend () -> Unit) {
        try {
            remoteTask()
        } catch (e: Exception) {
            throw RemoteDataSourceException("Remote source unexpected behaviour")
        }
    }
}