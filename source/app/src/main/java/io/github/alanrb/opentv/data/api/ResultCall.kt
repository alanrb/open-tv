package io.github.alanrb.opentv.data.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.github.alanrb.opentv.domain.entities.Result

class ResultCall<T>(
    proxy: Call<T>,
    private val failureDispatcher: NetworkFailureDispatcher
) : CallDelegate<T, Result<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                if (response.body() != null) {
                    Result.Success<T>(response.body())
                } else {
                    Result.Success(null)
                }
            } else {
                failureDispatcher.failureFromCode(code, response.errorBody()?.string())
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = failureDispatcher.failureFromThrowable(t)
            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone(), failureDispatcher)
}
