package com.madeean.comicslibrary.model.connectivity

import android.net.http.UrlRequest.Status
import kotlinx.coroutines.flow.Flow

interface ConnectivityObservable {
  fun observe(): Flow<Status>

  enum class Status {
    Available,
    Unavailable
  }
}