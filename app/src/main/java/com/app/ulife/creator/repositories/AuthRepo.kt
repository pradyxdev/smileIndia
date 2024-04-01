/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:36 AM
 *
 */

package com.app.ulife.creator.repositories

import com.app.ulife.creator.data.remote.MyApi
import com.app.ulife.creator.data.remote.SafeApiRequest
import com.app.ulife.creator.models.signin.SigninReq
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepo(
    private val api: MyApi
) : SafeApiRequest() {

//    suspend fun signUp(request: SignupReq): String {
//        var response = String()
//        try {
//            val jsonObject = Gson().toJson(request, SignupReq::class.java)
//            val body = jsonObject.toString()
//                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
//            response = apiRequest { api.signUp(body) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return response
//    }

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

}