package com.cerebrum.app.compose.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerebrum.app.CerebrumApp
import com.cerebrum.data.ArticleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel: ViewModel() {

  private val repository = CerebrumApp.module.articlesRepository

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading

  private val _articles = MutableStateFlow<List<ArticleResponse>>(listOf())
  val articles: StateFlow<List<ArticleResponse>> = _articles.asStateFlow()

  fun load() {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        _articles.value = repository
          .getArticles()
          .getOrThrow()
      } catch (e: Throwable) {

      } finally {
        _isLoading.value = false
      }
    }
  }
}