/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/8/23, 10:19 AM
 *
 */

package com.app.prady.appbase.ui.userTypeActivities.customer.mainActivity

import android.animation.LayoutTransition
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.ActivityMainBinding
import com.app.prady.appbase.factories.SharedVMF
import com.app.prady.appbase.helpers.Constants
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.models.EmptyResponse
import com.app.prady.appbase.models.fcm.SaveFcmReq
import com.app.prady.appbase.ui.SplashActivity
import com.app.prady.appbase.utils.ConnectionLiveData
import com.app.prady.appbase.utils.GPSTracker
import com.app.prady.appbase.utils.isNetworkConnected
import com.app.prady.appbase.viewModels.SharedVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

    private lateinit var connectionLiveData: ConnectionLiveData
    private var isHandlerRunning = true
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(this, factory)[SharedVM::class.java]
//        DynamicColors.applyToActivitiesIfAvailable(application)
        setupNavController()

//        hitApis()
//        setupHandler()

        //        connectionLiveData = ConnectionLiveData(this)
//        connectionLiveData.observe(this) { isNetworkAvailable ->
//            isNetworkAvailable?.let {
//                updateUI(it)
//            }
//        }
    }

    private fun hitApis() {
//        getUserDetails(MobileRequest(preferenceManager.phone.toString()))

    }

    private fun hitHandlerApis() {
        if (GPSTracker(this).canGetLocation()) {
            viewModel.setLatitude(GPSTracker(this).latitude)
            viewModel.setLongitude(GPSTracker(this).longitude)
        } else {
            GPSTracker(this).location
        }
    }

    private fun setupHandler() {
        val delay: Long = TimeUnit.SECONDS.toMillis(8)
        handler = Handler(Looper.myLooper()!!)
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isHandlerRunning) {
                    hitHandlerApis()
                    handler.postDelayed(this, delay)
                }
            }
        }, delay)
    }

    override fun onResume() {
        super.onResume()
        isHandlerRunning = true
    }

    override fun onPause() {
        super.onPause()
        isHandlerRunning = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isHandlerRunning = false
    }

//    private fun getUserDetails(mobileNoReq: MobileRequest) {
//        LoadingUtils.showDialog(this, false)
//        viewModel.getUserDetails = MutableLiveData()
//        viewModel.getUserDetails.observe(this) {
//            val response = Gson().fromJson(it, UserDetailsRes::class.java)
//            Log.e("getUserDetails ", "$response")
//            if (response != null) {
//                if (response.status) {
//                    LoadingUtils.hideDialog()
//                    preferenceManager.userid = response.response[0]?.userId
//                    preferenceManager.phone = response.response[0]?.mobile
//                    saveFCM(
//                        SaveFcmReq(
//                            preferenceManager.userid.toString(),
//                            preferenceManager.fcmToken.toString()
//                        )
//                    )
//                } else {
//                    LoadingUtils.hideDialog()
//                    apiErrorDialog(response.message)
//                }
//            } else {
//                LoadingUtils.hideDialog()
//                apiErrorDialog(Constants.apiErrors)
//            }
//        }
//        viewModel.getUserDetails(mobileNoReq)
//    }

    private fun saveFCM(request: SaveFcmReq) {
        viewModel.saveFCM = MutableLiveData()
        viewModel.saveFCM.observe(this) {
            val response = Gson().fromJson(it, EmptyResponse::class.java)
            Log.e("savefcm ", "$response")
            if (response != null) {
                if (response.status) {
//                Log.e("savefcm ", "${response.message}")
                } else {
                    apiErrorDialog(response.message)
                }
            } else {
                apiErrorDialog(Constants.apiErrors)
            }
        }
        viewModel.getSaveFCM(request)
    }

    private fun setupNavController() {
        // toolbar
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.historyFragment
            )
        )

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//        navController = navHostFragment.navController
//
//        //setup bottom view
//        binding.bottomNavigationView.setupWithNavController(navController)

//        val badge = binding.bottomNavigationView.getOrCreateBadge(1)
//        badge.isVisible = true
//        // An icon only badge will be displayed unless a number is set:
//        badge.number = 15

//        binding.toolbar.logo = resources.getDrawable(R.drawable.logo_txt)
        binding.toolbar.setTitleTextAppearance(this, R.style.TitleTextAppearance)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController


        setupActionBarWithNavController(navController)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            when {
//                resources.configuration.isNightModeActive -> {
//                    binding.bottomNavigationView.setBackgroundColor(resources.getColor(R.color.md_theme_dark_background))
//                }
//                else -> binding.bottomNavigationView.setBackgroundColor(resources.getColor(R.color.white))
//            }
//        }

        /* This is to set the menu items in the bottom navigation bar. */

//        val popUpMenu = PopupMenu(this, null)
//        popUpMenu.inflate(R.menu.main_menu)
//        binding.bottomNavigationView.setupWithNavController(popUpMenu.menu, navController)
//
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.homeFragment) {
//                binding.toolbar.setNavigationIcon(R.drawable.icon_3)
//            } else {
//                //
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    fun setToolbarIcon(icon: Int) {
        binding.toolbar.setNavigationIcon(icon)
    }

    fun setToolbarVisibility(isVisible: Boolean) {
        binding.appBarLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        if (isVisible) {
            binding.appBarLayout.visibility = View.VISIBLE
        } else {
            binding.appBarLayout.visibility = View.GONE
        }
    }

    fun setBottombarVisibility(isVisible: Boolean) {
//        binding.bottomNavigationView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
//        if (isVisible) {
//            binding.bottomNavigationView.visibility = View.VISIBLE
//        } else {
//            binding.bottomNavigationView.visibility = View.GONE
//        }
    }

    fun openDrawer() {
        binding.drawerlayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        binding.drawerlayout.closeDrawer(GravityCompat.START)
    }

    private fun updateUI(it: Boolean) {
        if (it) {
            Log.e("Network ", "Available")
        } else {
            Log.e("Network ", "Not Available")
            noInternetDialog()
        }
    }

    fun setLanguage(language: String) {
        preferenceManager.languagePrefs = language
        val local = Locale(language)
        Locale.setDefault(local)
        val configuration = Configuration()
        configuration.setLocale(local)
        resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)
        this.recreate()
    }

    private fun noInternetDialog() {
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
                if (applicationContext.isNetworkConnected) {
                    Log.e("Network ", "Restored")
                } else {
                    noInternetDialog()
                }
            }
            .show()
    }

    fun apiErrorDialog(errMsg: String) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_header_api_failed)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val title = dialog.findViewById<TextView>(R.id.tv_title)

        val subtitle = dialog.findViewById<TextView>(R.id.tv_sub_title)
        subtitle.text = errMsg

        val btnClose = dialog.findViewById<TextView>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.attributes = lp
    }

    private fun showLogoutDialog() {
        closeDrawer()

        CFAlertDialog.Builder(this).setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_logout).setTitle("LOGOUT OUT !!")
            .setMessage("Are you sure you want to log out from the app ?").addButton(
                "Yes",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                preferenceManager.clear()
                startActivity(Intent(this, SplashActivity::class.java))
                finishAffinity()
                dialog.dismiss()
            }.addButton(
                "No",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }.setCancelable(true).show()
    }
}