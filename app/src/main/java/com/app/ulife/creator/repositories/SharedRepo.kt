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
import com.app.ulife.creator.models.EmptyRequest
import com.app.ulife.creator.models.UserIdRequest
import com.app.ulife.creator.models.fcm.SaveFcmReq
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SharedRepo(
    private val api: MyApi
) : SafeApiRequest() {
    suspend fun saveFCM(request: SaveFcmReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, SaveFcmReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.saveFCM(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getUserDetails(request: UserIdRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UserIdRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserDetails(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }
    suspend fun getBanner(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getBanner(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }
}