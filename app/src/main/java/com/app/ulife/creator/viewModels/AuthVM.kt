/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:53 PM
 *
 */
package com.app.ulife.creator.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidlover5842.androidUtils.Validator.StringValidator
import com.app.ulife.creator.helpers.Coroutines
import com.app.ulife.creator.models.pincodeStateCity.GetPincodeStateCityReq
import com.app.ulife.creator.models.signUp.tmp.SignUpNewReq
import com.app.ulife.creator.models.signin.SigninReq
import com.app.ulife.creator.models.userDetails.UserDetailsReq
import com.app.ulife.creator.repositories.AuthRepo

class AuthVM(private val repository: AuthRepo) : ViewModel() {
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()
    val signInSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val terms: MutableLiveData<Boolean> = MutableLiveData()

    var userType: MutableLiveData<String> = MutableLiveData()

    init {
        signInSuccess.value = false
        error.value = Error()
    }

    fun signIn() {
        if (isValidUserNameAndPassword()) {
            signInSuccess.value = true
        }
    }

    private fun isValidUserNameAndPassword(): Boolean {
        val error = this.error.value!!
        val isValidUserName: Boolean = StringValidator.getValidator(username.value)
            .addRule { it.length > 5 }
            .doOnEmpty { error.username = "*Username can't be empty" }
            .doOnRuleFailed { error.username = "*Username can't be less than 6 characters" }
            .doOnRulePassed { error.username = null }
            .toBool()

        val isValidPassword = StringValidator.getValidator(password.value)
            .addRule { it.length > 5 }
            .doOnEmpty { error.password = "*Password required" }
            .doOnEmptyOrRuleFailed { error.password = "*Password can't be less than 6 characters" }
            .doOnRulePassed { error.password = null }
            .toBool()
        this.error.value = error
        return isValidUserName && isValidPassword
    }

    data class Error(var username: String? = null, var password: String? = null)

    fun setUserType(type: String) {
        userType.value = type
    }

    var signUp = MutableLiveData<String>()
    fun signUp(request: SignUpNewReq) {
        Coroutines.main {
            signUp.postValue(repository.signUp(request))
        }
    }

    var signIn = MutableLiveData<String>()
    fun signIn(request: SigninReq) {
        Coroutines.main {
            signIn.postValue(repository.signIn(request))
        }
    }

    var getUserDetails = MutableLiveData<String>()
    fun getUserDetails(request: UserDetailsReq) {
        Coroutines.main {
            getUserDetails.postValue(repository.getUserDetails(request))
        }
    }

    var getPincodeStateCity = MutableLiveData<String>()
    fun getPincodeStateCity(request: GetPincodeStateCityReq) {
        Coroutines.main {
            getPincodeStateCity.postValue(repository.getPincodeStateCity(request))
        }
    }
}