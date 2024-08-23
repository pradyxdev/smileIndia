/*
 * *
 *  * Created by Prady on 6/3/23, 11:44 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 6/3/23, 11:10 AM
 *
 */

package com.app.smile.india.ui.initialActivities.introActivity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.app.smile.india.R
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.ui.authActivity.AuthActivity
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class MyAppIntro : AppIntro2() {
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        // Make sure you don't call setContentView!

        preferenceManager = PreferenceManager.instance

        setTransformer(AppIntroPageTransformerType.Depth)
        // You can customize your parallax parameters in the constructors.
        setTransformer(
            AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
            )
        )
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.createInstance(
                title = "Welcome to \nQCharge",
                description = "This is the first slide...",
                imageDrawable = R.drawable.logo,
                backgroundDrawable = R.drawable.stacked_steps_haikei_1,
                titleColorRes = R.color.white,
                descriptionColorRes = R.color.white,
                backgroundColorRes = R.color.gr1_end,
                titleTypefaceFontRes = R.font.rubik_medium,
                descriptionTypefaceFontRes = R.font.rubik_light,
            )
        )

        addSlide(
            AppIntroFragment.createInstance(
                title = "Dummy text !",
                description = "This is the second slide...",
                imageDrawable = R.drawable.logo,
                backgroundDrawable = R.drawable.stacked_steps_haikei_3,
                titleColorRes = R.color.white,
                descriptionColorRes = R.color.white,
                backgroundColorRes = R.color.gr1_end,
                titleTypefaceFontRes = R.font.rubik_medium,
                descriptionTypefaceFontRes = R.font.rubik_light,
            )
        )

        addSlide(
            AppIntroFragment.createInstance(
                title = "...Let's get started!",
                description = "This is the last slide, I won't annoy you more!  :)",
                imageDrawable = R.drawable.logo,
                backgroundDrawable = R.drawable.stacked_steps_haikei_2,
                titleColorRes = R.color.white,
                descriptionColorRes = R.color.white,
                backgroundColorRes = R.color.gr1_end,
                titleTypefaceFontRes = R.font.rubik_medium,
                descriptionTypefaceFontRes = R.font.rubik_light,
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        openViews()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        openViews()
    }

    private fun openViews() {
        when {
            preferenceManager.loggedIn.equals("true", ignoreCase = true) -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
                finish()
            }
            else -> {
                val i = Intent(this, AuthActivity::class.java)
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
                finish()
            }
        }
    }
}