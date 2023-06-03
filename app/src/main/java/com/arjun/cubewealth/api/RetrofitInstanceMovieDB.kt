package com.arjun.cubewealth.api

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Creating a Retrofit Instance which will be
// used to create an instance using the Retrofit Builder
// and the OkHttp Interceptor, ApiMovieDB Interface is used here
// to make the API calls available to us
class RetrofitInstanceMovieDB {
    companion object {
        private val retrofitInstance by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okhttpClientVariable = okhttp3.OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientVariable)
                .build()
        }

        val movieDBApiVar by lazy {
            retrofitInstance.create(ApiMovieDB::class.java)
        }
    }
}