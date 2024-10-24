package com.cerebrum.app

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class Downloader(
  context: Context
) {
  private val manager = context.getSystemService(DownloadManager::class.java)

  fun downloadPdf(
    url: String,
    name: String,
    token: String,
    withExt: Boolean = false
  ): Long {
    val request = DownloadManager.Request(url.toUri())
      .setMimeType("application/pdf")
      .addRequestHeader("Auth-Token", token)
      .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
      .setTitle(name)
      .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, if (withExt) name else "$name.pdf")
    return manager.enqueue(request)
  }

  fun downloadImage(
    url: String,
    imageType: String,
    name: String,
    token: String,
  ): Long {
    val request = DownloadManager.Request(url.toUri())
      .setMimeType("image/${imageType}")
      .addRequestHeader("Auth-Token", token)
      .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
      .setTitle(name)
      .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$name.$imageType")
    return manager.enqueue(request)
  }
}