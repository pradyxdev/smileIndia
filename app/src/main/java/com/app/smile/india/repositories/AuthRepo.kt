/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:36 AM
 *
 */

package com.app.smile.india.repositories

import com.app.smile.india.data.remote.MyApi
import com.app.smile.india.data.remote.SafeApiRequest
import com.app.smile.india.models.UserIdRequest
import com.app.smile.india.models.pincodeStateCity.GetPincodeStateCityReq
import com.app.smile.india.models.signUp.tmp.SignUpNewReq
import com.app.smile.india.models.signin.SigninReq
import com.app.smile.india.models.userDetails.UserDetailsReq
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepo(
    private val api: MyApi
) : SafeApiRequest() {

    suspend fun signUp(request: SignUpNewReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, SignUpNewReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.signUp(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun signIn(request: SigninReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, SigninReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.signIn(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getUserDetails(request: UserDetailsReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UserDetailsReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserDetailsWo(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPincodeStateCity(request: GetPincodeStateCityReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPincodeStateCityReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPincodeStateCity(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun passwordRecovery(request: UserIdRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UserIdRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.passwordRecovery(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

}