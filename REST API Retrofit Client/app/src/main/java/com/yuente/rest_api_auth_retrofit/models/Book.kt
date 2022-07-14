package com.yuente.rest_api_auth_retrofit.models

import com.google.gson.annotations.SerializedName

data class Book(

    @SerializedName("title")
    var title: String,

    @SerializedName("author")
    var author: String,

    @SerializedName("pages")
    var pages: Int,
    
    @SerializedName("year")
    var year: Int
)

