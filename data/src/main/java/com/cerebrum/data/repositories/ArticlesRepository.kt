package com.cerebrum.data.repositories

import com.cerebrum.data.IAuthStore
import com.cerebrum.data.IWebClient
import com.cerebrum.data.Langs

class ArticlesRepository(
  private val apiClient: IWebClient,
  private val authStore: IAuthStore,
) : BaseRepository() {

  suspend fun getArticles() = execute {
    apiClient.getArticles(
      authStore.getToken(),
      authStore.getLang().value
    )
  }

}