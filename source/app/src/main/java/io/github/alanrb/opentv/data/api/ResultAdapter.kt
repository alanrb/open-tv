package io.github.alanrb.opentv.data.api

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type
import io.github.alanrb.opentv.domain.entities.Result

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class ResultAdapter<T>(
    private val responseType: Type,
    private val failureDispatcher: NetworkFailureDispatcher
) : CallAdapter<T, Call<Result<T>>> {

    override fun responseType() = responseType
    override fun adapt(call: Call<T>): Call<Result<T>> = ResultCall(call, failureDispatcher)
}