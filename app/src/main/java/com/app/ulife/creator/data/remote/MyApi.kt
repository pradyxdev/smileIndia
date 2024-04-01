/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 6:57 PM
 *
 */

package com.app.ulife.creator.data.remote

import com.app.ulife.creator.helpers.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface MyApi {
    @POST("UserRegisteration")
    suspend fun signUp(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("UserLogin")
    suspend fun signIn(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("SaveFcmId")
    suspend fun saveFCM(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("userProfile")
    suspend fun getUserDetails(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("GetBanner")
    suspend fun getBanner(
        @Body parameter: RequestBody
    ): Response<String>


    /* Creating a singleton object of MyApi. */
    companion object {
        /**
         * It creates a Retrofit instance.
         *
         * @param networkConnectionInterceptors This is the interceptor that we created in the previous
         * step.
         * @return Retrofit.Builder()
         */
        operator fun invoke(
            networkConnectionInterceptors: NetworkConnectionInterceptors
        ): MyApi {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val headerInterceptor = CustomInterceptor()

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptors)
                .addInterceptor(headerInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

    /* > This class is an interceptor that intercepts the request and adds a custom header to it */
    class CustomInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request().newBuilder()
                .build()
            return chain.proceed(request)
        }
    }
}