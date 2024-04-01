/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creator.repositories.AuthRepo
import com.app.ulife.creator.viewModels.AuthVM

@Suppress("UNCHECKED_CAST")

class AuthVMF(
    private val repository: AuthRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthVM(repository) as T
    }
}