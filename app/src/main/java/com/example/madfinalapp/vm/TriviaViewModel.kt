package com.example.madfinalapp.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madfinalapp.dao.TriviaDao
import com.example.madfinalapp.dao.TriviaRecordRoomDatabase
import com.example.madfinalapp.model.TriviaRecord
import com.example.madfinalapp.repository.TriviaRepository
import kotlinx.coroutines.launch

class TriviaViewModel(application: Application) : AndroidViewModel(application) {
    private val triviaRepository = TriviaRepository()

    val trivia = triviaRepository.trivia

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val errorText: LiveData<String> get() = _errorText

    fun getTrivia(category: Int) {
        viewModelScope.launch {
            try {
                triviaRepository.getTrivia(category)
            } catch (error: TriviaRepository.TriviaError) {
                _errorText.value = error.message
                Log.e("Trivia error", error.cause.toString())
            }
        }
    }
}