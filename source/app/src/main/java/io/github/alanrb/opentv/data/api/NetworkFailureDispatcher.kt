package io.github.alanrb.opentv.data.api

import io.github.alanrb.opentv.domain.entities.Result

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

interface NetworkFailureDispatcher {

    fun failureFromCode(code: Int, body: Any?): Result.Failure

    fun failureFromThrowable(throwable: Throwable): Result.Failure
}