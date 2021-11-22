package io.github.alanrb.opentv.data.api

import io.github.alanrb.opentv.domain.entities.Result
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

sealed class NetworkFailure(val message: String? = null) : Result.Failure() {
    object SessionExpired : NetworkFailure()
    object SocketTimeout : NetworkFailure()
    object NotFound : NetworkFailure()
    object GoneError : NetworkFailure()
    object NetworkError : NetworkFailure()
    object UnknownError : NetworkFailure()
    object InvalidCredentials : NetworkFailure()

    class NetworkFailureDispatcherImpl() :
        NetworkFailureDispatcher {

        override fun failureFromCode(code: Int, body: Any?): NetworkFailure {
            val messageError = JSONObject(body as String)
            return when (code) {
                401 -> {
                    SessionExpired
                }
                404 -> {
                    NotFound
                }
                410 -> GoneError
                else -> UnknownError
            }
        }

        override fun failureFromThrowable(throwable: Throwable): Failure {
            return when (throwable) {
                is IOException -> NetworkError
                is SocketTimeoutException -> SocketTimeout
                else -> UnknownError
            }
        }
    }
}