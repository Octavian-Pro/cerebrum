package com.cerebrum.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

  // val BASE_URL = "http://46.147.242.102:8881"// "https://warthog-growing-honeybee.ngrok-free.app/" //"http://46.147.242.102:8881"
  const val BASE_URL = "https://cerebrum-help.ru:4343/"

  fun createClient() : IWebClient {
    val builder = OkHttpClient.Builder()

    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    builder.addInterceptor(loggingInterceptor)

    val okClient = builder.build()

    val moshi = Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .build();

    val retrofit = Retrofit.Builder()
      // .baseUrl()
      .baseUrl(BASE_URL)
      .client(okClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()


    return retrofit.create(IWebClient::class.java)
  }

}