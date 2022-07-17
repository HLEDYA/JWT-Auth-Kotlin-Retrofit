package com.yuente.rest_api_auth_retrofit.network

import android.util.Log
import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import okhttp3.Interceptor
import okhttp3.Response
import java.time.ZoneOffset
import java.time.ZonedDateTime


class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requesBuilder = chain.request().newBuilder()

        // Build an HMAC signer using a SHA-256 hash
        val signer: Signer = HMACSigner.newSHA256Signer("secret-key")

        // Build a new JWT with an issuer(iss), issued at(iat), subject(sub) and expiration(exp)
        val jwt: JWT = JWT()
            .setIssuer("android myapp")
            .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
            .setSubject("subject")
            .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(5))

        // Sign and encode the JWT to a JSON string representation
        val encodedJWT: String = JWT.getEncoder().encode(jwt, signer)

        Log.d("AuthInterceptor", "encodedJWT:" + encodedJWT)

        requesBuilder.addHeader("Authorization", "Bearer $encodedJWT")

        return chain.proceed(requesBuilder.build())
    }
}