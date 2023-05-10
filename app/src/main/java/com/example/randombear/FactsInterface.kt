package com.example.randombear

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface FactsInterface {
    @GET(".")
    fun getText() : Call<JsonObject>
    //https://meowfacts.herokuapp.com/
}
