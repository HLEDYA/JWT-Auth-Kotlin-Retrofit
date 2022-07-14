package com.yuente.rest_api_auth_retrofit.network

import com.yuente.rest_api_auth_retrofit.models.Book
import com.yuente.rest_api_auth_retrofit.models.LoginRequest
import com.yuente.rest_api_auth_retrofit.models.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Paths.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET(Paths.BOOKS_URL)
    fun getBooks(): Call<List<Book>>
}