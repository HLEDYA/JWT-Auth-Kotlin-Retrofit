package com.yuente.rest_api_auth_retrofit.network

import com.yuente.rest_api_auth_retrofit.models.Book
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET(Paths.BOOKS_URL)
    fun getBooks(): Call<List<Book>>
}