package app.id.devfest.articlesummarization.screens.wikidetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.id.devfest.articlesummarization.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WikiDetailViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<WikiDetailUiState> =
        MutableStateFlow(WikiDetailUiState())
    val uiState: StateFlow<WikiDetailUiState> =
        _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = _uiState.value.copy(
            loading = true,
            summary = "",
            errorMsg = ""
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text("$AI_PROMPT ${prompt.take(39000)}")
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        summary = outputContent,
                        errorMsg = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    summary = "",
                    errorMsg = e.message.toString()
                )
            }
        }
    }

    companion object {
        private const val AI_PROMPT = "Summarize into maximum 5 point separate with enter without any markdown\n"
    }

}