/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.prady.appbase.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.prady.appbase.repositories.SharedRepo
import com.app.prady.appbase.viewModels.SharedVM

@Suppress("UNCHECKED_CAST")

class SharedVMF(
    private val repository: SharedRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedVM(repository) as T
    }
}