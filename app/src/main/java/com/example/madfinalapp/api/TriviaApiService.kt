package com.example.madfinalapp.api

import com.example.madfinalapp.model.TriviaList
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("/api.php?amount=8&difficulty=medium&type=multiple")
    suspend fun getTrivia(@Query("category") category : Int) : TriviaList
}