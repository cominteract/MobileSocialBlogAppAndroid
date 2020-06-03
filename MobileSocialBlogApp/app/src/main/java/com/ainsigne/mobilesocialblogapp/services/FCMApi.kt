package com.ainsigne.mobilesocialblogapp.services

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class FCMApi {
    val webservice: FCMService by lazy {

//        val httpClient = OkHttpClient.Builder()
//        val client = httpClient.addInterceptor { chain ->
//            var request: Request = chain.request()
//            val url: HttpUrl =
//                request.url().newBuilder().
//                    addQueryParameter("client_id", CLIENT_ID).
//                    addQueryParameter("client_secret", CLIENT_SECRET).
//                    addQueryParameter("v", V).build()
//            request = request.newBuilder().url(url).build()
//            chain.proceed(request)
//        }.build()


        val logging = HttpLoggingInterceptor()
// set your desired log level
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient()
// add your other interceptors …

// add logging as last interceptor
// add your other interceptors …

// add logging as last interceptor
        // <-- this is the important line!


        val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).addInterceptor(logging).build()


        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/").client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(FCMService::class.java)
    }
}