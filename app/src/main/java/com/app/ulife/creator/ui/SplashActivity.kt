/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:35 AM
 *
 */

package com.app.ulife.creator.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creator.BuildConfig
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.ActivitySplashBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.version.AppVersion
import com.app.ulife.creator.ui.authActivity.AuthActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.getAppVersioning
import com.app.ulife.creator.utils.isNetworkConnected
import com.app.ulife.creator.utils.snackbar
import com.app.ulife.creator.viewModels.SharedVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    var versionCode = ""
    var versionName = ""

    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferenceManager: PreferenceManager

    companion object {
        private const val SPLASH_TIME_OUT = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, factory)[SharedVM::class.java]
        preferenceManager = PreferenceManager.instance
//        setLanguage()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(binding.root)

        versionCode = BuildConfig.VERSION_CODE.toString()
        versionName = BuildConfig.VERSION_NAME
        binding.tvAppVersion.text = "$versionName"

        val slideInRight = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.slide_in_from_right
        )

        binding.apply {
            icon.startAnimation(slideInRight)
            progressBar.startAnimation(slideInRight)
//            tvAppName.startAnimation(slideInRight)
        }
        checkInternet()
    }

    private fun checkInternet() {
        if (applicationContext.isNetworkConnected) {
//            getAppVersioningFromFB()
            Handler().postDelayed({
                setupViews()
            }, 3000)
        } else {
            CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setHeaderView(R.layout.dialog_header_no_connection)
                .setTitle("No Internet Connection !!")
                .setMessage("Please check your internet connection.")
                .addButton(
                    "Try Again", -1, -1,
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .addButton(
                    "Exit App", -1, -1,
                    CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { _: DialogInterface, _: Int -> finishAffinity() }
                .onDismissListener {
                    checkInternet()
                }
                .show()
        }
    }

    private fun getAppVersioningFromFB() {
        val reference = applicationContext.getAppVersioning()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val appVersion: AppVersion? = dataSnapshot.getValue(AppVersion::class.java)
                Log.e("AppVersion ", "" + appVersion?.versionName)
                try {
                    if (appVersion?.versionName.equals(versionName)) {
                        setupFCM()
                    } else {
                        showAppUpdaterDialog(appVersion?.appUrl)
                    }
                } catch (e: Exception) {
                    binding.root.snackbar("Error: $e")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.root.snackbar("Error: $databaseError")
            }
        })
    }

    private fun setupFCM() {
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCMTAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            preferenceManager.fcmToken = token
            Log.e("FCMTAG", token + "\n pref: " + preferenceManager.fcmToken)

            /* Delaying the execution of the `setupViews()` function by 1 second. */
            Handler().postDelayed({
                setupViews()
            }, SPLASH_TIME_OUT)
        })
    }

    private fun setupViews() {
        when {
            preferenceManager.loggedIn.equals("true", ignoreCase = true) -> {
                openNextUi()
            }
            else -> {
                val i = Intent(this, AuthActivity::class.java)
                startActivity(i)
                finish()
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
            }
        }
    }

    private fun openNextUi() {
        when (preferenceManager.userType) {
            Constants.userTypeCustomer -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
//            Constants.userTypeAdmin -> {
//                startActivity(Intent(this, VendorMainActivity::class.java))
//            }
            else -> {
                preferenceManager.clear()
                startActivity(Intent(this, SplashActivity::class.java))
            }
        }
        overridePendingTransition(
            R.anim.slide_in_from_right,
            R.anim.slide_out_left
        )
        finishAffinity()
    }

    private fun showAppUpdaterDialog(urlSend: String?) {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_app_update)
        val positiveBtn = dialog.findViewById(R.id.positive_btn) as MaterialButton
        positiveBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlSend)))
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
}