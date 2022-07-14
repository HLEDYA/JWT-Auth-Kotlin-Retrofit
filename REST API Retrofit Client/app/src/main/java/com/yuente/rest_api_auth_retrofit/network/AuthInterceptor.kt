package com.yuente.rest_api_auth_retrofit.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requesBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let { authCode ->
            requesBuilder.addHeader("Authorization", "Bearer $authCode")
        }

        return chain.proceed(requesBuilder.build())
    }
}