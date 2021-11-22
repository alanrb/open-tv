package io.github.alanrb.opentv.data.api

import io.github.alanrb.opentv.data.SharePreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class AuthInterceptor constructor(
//    private val preferenceDataStore: SharePreferenceManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
//        val accessToken = preferenceDataStore.accessToken
        request = createAuthenticatedRequest(request, "accessToken")
        return chain.proceed(request)
    }

    private fun createAuthenticatedRequest(originalRequest: Request, accessToken: String): Request {
        return originalRequest.newBuilder()
            .header("Authorization", "bearer $accessToken")
            .header("trakt-api-key", "")
            .build()
    }
}