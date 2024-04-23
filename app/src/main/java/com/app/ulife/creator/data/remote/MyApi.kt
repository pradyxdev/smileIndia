/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 6:57 PM
 *
 */

package com.app.ulife.creator.data.remote

import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager.Companion.instance
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
    @POST("Home/InsertUser")
    suspend fun signUp(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("Home")
    suspend fun signIn(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("SaveFcmId")
    suspend fun saveFCM(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("Home/GetUserDetail")
    suspend fun getUserDetailsWo(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("Home/GetStateCity")
    suspend fun getPincodeStateCity(
        @Body parameter: RequestBody
    ): Response<String>

    //    @POST("Home/GetUserDetail")
    @POST("User/UserRequest")
    suspend fun getUserDetails(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("GetBanner")
    suspend fun getBanner(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/UserRequest")
    suspend fun getUserRequest(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/Recharge")
    suspend fun doRecharge(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/GetCircleCodes")
    suspend fun getCircle(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/GetRechargePlans")
    suspend fun getPlansList(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/GetDTHPlans")
    suspend fun getDthPlansList(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/MobileLookup")
    suspend fun doMobileLookUp(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("User/GetOperatorCodes")
    suspend fun getOperatorNew(
        @Body parameter: RequestBody
    ): Response<String>

    // paysprint api's
    @POST("PaysPrint/OperatorList")
    suspend fun getPsOperator(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/HLRCheck")
    suspend fun hlrCheck(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/PlanList")
    suspend fun getPsMobPlanList(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/DoRecharge")
    suspend fun doPsMobRecharge(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/BillOperatorList")
    suspend fun getPsBbpsOperator(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/BillFetch")
    suspend fun getPsFetchBbpsOperator(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/FastagOperatorList")
    suspend fun getPsFastagOperator(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/FastagBillFetch")
    suspend fun getPsFetchFastagOperator(
        @Body parameter: RequestBody
    ): Response<String>

    @POST("PaysPrint/FastagRecharge")
    suspend fun doPsFastagRecharge(
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
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
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
//            val request = chain.request().newBuilder()
            val request = chain.request().newBuilder().addHeader(
                "Authorization",
                "Bearer " + instance.token
            )
                .build()
            return chain.proceed(request)
        }
    }
}