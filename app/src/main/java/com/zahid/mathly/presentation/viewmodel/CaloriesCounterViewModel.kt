package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.zahid.mathly.data.remote.OpenAIService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CaloriesCounterViewModel @Inject constructor(
    val openAIService: OpenAIService
) : ViewModel() 