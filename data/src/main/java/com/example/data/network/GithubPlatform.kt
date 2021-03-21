package com.example.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Inject

class GithubPlatform @Inject constructor(cacheDir: File) {

    /**
     * Moshi is used to parse json from our Retrofit calls
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * OkHttpClient is used to intercept request and add necessary headers.
     * Headers include:
     * - Accept
     * - Cache-Control
     */
    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(cacheDir, CACHE_SIZE.toLong()))
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(ACCEPT_HEADER, "application/vnd.github.v3+json")
                .addHeader(CACHE_HEADER, "public, max-stale=$CACHE_STALE_TIME")
                .build()

            chain.proceed(request)
        }
        .build()

    /**
     * Retrofit is used to handle interacting with the Github REST API and converting the response
     * using the provided converter factory.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val githubService: GithubService = retrofit.create(GithubService::class.java)
}

private const val BASE_URL = "https://api.github.com/"
private const val ACCEPT_HEADER = "Accept"
private const val CACHE_HEADER = "Cache-Control"
private const val CACHE_STALE_TIME = 60 * 60 * 24 // One day
private const val CACHE_SIZE = 5 * 1024 * 1024 // 5MB
