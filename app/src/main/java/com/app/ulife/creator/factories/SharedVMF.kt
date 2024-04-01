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
import com.app.ulife.creator.repositories.SharedRepo
import com.app.ulife.creator.viewModels.SharedVM

@Suppress("UNCHECKED_CAST")

class SharedVMF(
    private val repository: SharedRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedVM(repository) as T
    }
}