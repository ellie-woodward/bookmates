package com.example.myapplication.ui.gamelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Game List Fragment"
    }
    val text: LiveData<String> = _text
}