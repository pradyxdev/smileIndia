/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 4:39 PM
 *
 */

package com.app.ulife.creatoron.data.remote

import com.app.ulife.creatoron.helpers.Constants
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/* It's a class that makes an API call and returns the response body if the call is successful,
otherwise it throws an exception */
abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try {
//                    message.append(JSONObject(it).getString("message"))
                    message.append(JSONObject(it))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            Constants.apiErrors = message.toString()
            throw ApiException(message.toString())
        }
    }
}