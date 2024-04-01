/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 6:55 PM
 *
 */

package com.app.prady.appbase.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.prady.appbase.helpers.Coroutines
import com.app.prady.appbase.models.EmptyRequest
import com.app.prady.appbase.models.UserIdRequest
import com.app.prady.appbase.models.fcm.SaveFcmReq
import com.app.prady.appbase.models.notice.NoticeReq
import com.app.prady.appbase.models.receipt.GetReceiptReq
import com.app.prady.appbase.repositories.SharedRepo

class SharedVM(private val repository: SharedRepo) : ViewModel() {

    var latitude: MutableLiveData<Double> = MutableLiveData()
    var logitude: MutableLiveData<Double> = MutableLiveData()
    var fullAddress: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        error.value = Error()
    }

    fun setLatitude(latitude: Double) {
        this.latitude.postValue(latitude)
    }

    fun setLongitude(longitude: Double) {
        logitude.postValue(longitude)
    }

    fun setFullAddress(fullAddress: String) {
        this.fullAddress.postValue(fullAddress)
    }

    data class Error(var error: String? = null)

    // Rest APIS
    var saveFCM = MutableLiveData<String>()
    fun getSaveFCM(request: SaveFcmReq) {
        Coroutines.main {
            saveFCM.postValue(repository.saveFCM(request))
        }
    }

    var getUserDetails = MutableLiveData<String>()
    fun getUserDetails(request: UserIdRequest) {
        Coroutines.main {
            getUserDetails.postValue(repository.getUserDetails(request))
        }
    }

    var getBanner = MutableLiveData<String>()
    fun getBanner(request: EmptyRequest) {
        Coroutines.main {
            getBanner.postValue(repository.getBanner(request))
        }
    }
}