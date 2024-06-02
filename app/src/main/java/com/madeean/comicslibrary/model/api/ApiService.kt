package com.madeean.comicslibrary.model.api

import com.madeean.comicslibrary.BuildConfig
import com.madeean.comicslibrary.getHash
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
  private const val BASE_URL = "http://gateway.marvel.com/v1/public/"

  private fun getRetrofit() : Retrofit {
    val ts = System.currentTimeMillis().toString()
    val apiSecret = BuildConfig.PRIVATE_KEY
    val apiKey = BuildConfig.PUBLIC_KEY
    val hash = getHash(ts, apiSecret, apiKey)

    val clientInterceptor = Interceptor {chain ->
      var request: Request = chain.request()
      val url: HttpUrl = request.url.newBuilder()
        .addQueryParameter("ts", ts)
        .addQueryParameter("apikey", apiKey)
        .addQueryParameter("hash", hash)
        .build()

      request = request.newBuilder().url(url).build()
      chain.proceed(request)
    }

    val client = OkHttpClient.Builder().addInterceptor(clientInterceptor).build()

    return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
  }

  val api: MarvelApi = getRetrofit().create(MarvelApi::class.java)
}