package com.yuente.rest_api_auth_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yuente.rest_api_auth_retrofit.models.Book
import com.yuente.rest_api_auth_retrofit.models.LoginRequest
import com.yuente.rest_api_auth_retrofit.models.LoginResponse
import com.yuente.rest_api_auth_retrofit.network.ApiClient
import com.yuente.rest_api_auth_retrofit.network.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.error
import kotlin.math.log


class MainActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    lateinit var bookList: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        var authToken = "";

        // Login
        apiClient.getApiService()
            .login(LoginRequest(email = "yusuf@gmail.com", password = "qwerty"))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("main", "login error")
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()
                    authToken = loginResponse?.authToken.toString();

                    Log.d("main", "response code : " + response.code())
                    Log.d("main", "authToken : " + authToken)

                    if (response.code() == 200 && authToken != "") {
                        Log.d("main", "Login is successful!")
                        sessionManager.saveAuthToken(authToken)
                        getBookList()
                    } else {
                        Log.e("main", "login is not allowed")
                    }
                }

            })
    }

    fun getBookList() {
        val authToken = sessionManager.fetchAuthToken();
        if (authToken == null) {
            Log.e("main", "authToken does not exist. Probably login required.")
            return
        }
        apiClient.getApiService().getBooks(token = "Bearer ${authToken}")
            .enqueue(object : Callback<List<Book>> {
                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    Log.e("main", "get book list error")
                }

                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.code() == 200) {
                        Log.d("main", "book list received")
                        bookList = (response.body() as MutableList<Book>?)!!
                        Log.d("main", bookList.toString())
                    } else {
                        Log.e("main", "book list receive error code: " + response.code())
                    }
                }
            })
    }


}

