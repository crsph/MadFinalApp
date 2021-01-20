package com.example.madfinalapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madfinalapp.api.TriviaApi
import com.example.madfinalapp.api.TriviaApiService
import com.example.madfinalapp.model.Trivia
import kotlinx.coroutines.withTimeout

class TriviaRepository {
    private val triviaApiService: TriviaApiService = TriviaApi.createApi()
    private val _trivia: MutableLiveData<List<Trivia>> = MutableLiveData()
    val trivia: LiveData<List<Trivia>> get() = _trivia

    suspend fun getTrivia(category: Int) {
        try {
            // Timeout the request after 5 seconds
            val result = withTimeout(5_000) {

                // Retrieving the JSON data as a list
                triviaApiService.getTrivia(category)
            }

            _trivia.value = result.triviaList
        } catch (error: Throwable) {
            throw TriviaError("Unable to retrieve Trivia", error)
        }
    }

    class TriviaError(message: String, cause: Throwable) : Throwable(message, cause)
}