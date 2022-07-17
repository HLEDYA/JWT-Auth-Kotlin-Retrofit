package com.yuente.rest_api_auth_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yuente.rest_api_auth_retrofit.models.Book
import com.yuente.rest_api_auth_retrofit.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {


    private lateinit var apiClient: ApiClient

    lateinit var bookList: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiClient = ApiClient()
        getBookList();
    }

    fun getBookList() {
        apiClient.getApiService(this).getBooks()
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

