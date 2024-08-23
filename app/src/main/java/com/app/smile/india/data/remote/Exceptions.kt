/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.smile.india.data.remote

import java.io.IOException

/* It's a Kotlin class that extends the IOException class and takes a message as a parameter */
class ApiException(message: String) : IOException(message)

/* NoInternetException is a class that extends IOException and takes a String as a parameter. */
class NoInternetException(message: String) : IOException(message)