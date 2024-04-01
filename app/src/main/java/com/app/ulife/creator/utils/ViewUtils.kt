/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.utils


import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
//    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
//        snackbar.setBackgroundTint(resources.getColor(R.color.md_theme_light_error))
//        snackbar.setActionTextColor(resources.getColor(R.color.white))
        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }
    }.show()
}

fun View.snackbar(message: String, duration: String) {
    when (duration) {
        "short" -> {
            Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snackbar ->
//                snackbar.setBackgroundTint(resources.getColor(R.color.md_theme_light_error))
//                snackbar.setActionTextColor(resources.getColor(R.color.white))
                snackbar.setAction("Dismiss") {
                    snackbar.dismiss()
                }
            }.show()
        }
        "long" -> {
            Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
//                snackbar.setBackgroundTint(resources.getColor(R.color.md_theme_light_error))
//                snackbar.setActionTextColor(resources.getColor(R.color.white))
                snackbar.setAction("Dismiss") {
                    snackbar.dismiss()
                }
            }.show()
        }
        "indefinite" -> {
            Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).also { snackbar ->
//                snackbar.setBackgroundTint(resources.getColor(R.color.md_theme_light_error))
//                snackbar.setActionTextColor(resources.getColor(R.color.white))
                snackbar.setAction("Dismiss") {
                    snackbar.dismiss()
                }
            }.show()
        }
    }
}

fun View.showAlert(message: String) {
    val builder = AlertDialog.Builder(context)
    //set  for alert dialog
    builder.setMessage(message)
    builder.setPositiveButton("Ok") { dialogInterface, which ->
        dialogInterface.dismiss()
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/*
fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}
*/

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.fetchColor(id: Int): Int = ContextCompat.getColor(this, id)

/*fun ImageView.setImageFromUrl(url: String) {
    //Picasso.get().load(url).into(this);
    Timber.d("url image to load: " + url)

    url.replace(",", "")


    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val drawable = ContextCompat.getDrawable(context, R.drawable.video_placeholder)

    //val requestOption = RequestOptions()
    *//*Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade(400))
        //.apply(requestOption)
        //.placeholder(drawable)
        .error(drawable)
        .into(this)*//*

    Picasso.get().load(url).into(this);
}*/

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}


fun TextView.openDateChooser(context: Context) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            text = ("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year)
        },
        year,
        month,
        day
    )
    dpd.show()
}

fun TextView.openDateChooserString() {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context as Activity,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            text = ("" + +dayOfMonth + "/" + (monthOfYear + 1))
        },
        year,
        month,
        day
    )
    dpd.show()
}

fun EditText.setErrorWithFocus(message: String) {
    requestFocus()
    error = message
}

fun EditText.checkIfEmpty(): Boolean {
    return text.toString().isEmpty()
}

fun EditText.checkIfValidPassword(): Boolean {
    return text.toString().length < 6
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.openTimePicker() {
    val currentTime = Calendar.getInstance()
    val hour = currentTime[Calendar.HOUR]
    val minute = currentTime[Calendar.MINUTE]
    val mTimePicker = TimePickerDialog(
        context,
        { timePicker, selectedHour, selectedMinute ->

            val _24HourTime = "$selectedHour:$selectedMinute"
            val _24HourSDF = SimpleDateFormat("HH:mm")
            val _12HourSDF = SimpleDateFormat("hh:mm a")
            val _24HourDt = _24HourSDF.parse(_24HourTime)
//                System.out.println(_24HourDt)
//                println(_12HourSDF.format(_24HourDt))
            setText(_12HourSDF.format(_24HourDt))
        },
        hour,
        minute,
        false
    )
//        mTimePicker.setTitle("Select Time")
    mTimePicker.show()
}

fun EditText.openDateChooser(context: Context) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        context, { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            setText("" + (monthOfYear + 1) + "-" + dayOfMonth + "-" + year)
        },
        year,
        month,
        day
    )
//    if (minDatee == 0L) {
//        dpd.datePicker.minDate = minDatee - 2000
//    }
    dpd.show()
}

fun Context.calculateNoOfColumns(context: Context, width: Int): Int {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / width).toInt()
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    applicationContext.toast("Copied to clipboard!")
}

val Context.isNetworkConnected: Boolean
    get() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            } ?: false
        else
            @Suppress("DEPRECATION")
            manager.activeNetworkInfo?.isConnectedOrConnecting == true
    }

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int
): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attrColor))
    val textColor = typedArray.getColor(0, 0)
    typedArray.recycle()
    return textColor
}
