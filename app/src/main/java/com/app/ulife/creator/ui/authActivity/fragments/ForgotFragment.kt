/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:35 AM
 *
 */

package com.app.ulife.creator.ui.authActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.FragmentForgotBinding
import com.app.ulife.creator.factories.AuthVMF
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.utils.getNavOptions
import com.app.ulife.creator.utils.onDebouncedListener
import com.app.ulife.creator.viewModels.AuthVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class ForgotFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentForgotBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: AuthVMF by instance()
    private lateinit var viewModel: AuthVM

    private lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthVM::class.java]
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                val action = ForgotFragmentDirections.actionForgotFragmentToSigninFragment()
                findNavController().navigate(action)
            }
            btnSignIn.onDebouncedListener {
                val args = Bundle()
                args.putString("otp", "1234")
                args.putString("otpType", "forgot")
                args.putString("number", binding.etNumber.text.toString())

                /* Navigating to the next fragment. */
                findNavController().navigate(
                    R.id.otpVerifyFragment,
                    args,
                    getNavOptions()
                )
            }
        }
    }

//    private fun hitApi(loginReq: MobileRequest) {
//        viewModel.signIn = MutableLiveData()
//        viewModel.signIn.observe(requireActivity()) {
//            val response = Gson().fromJson(it, SignInRes::class.java)
//            Log.e("response", "$response")
//            if (response != null) {
//                if (response.status) {
//                    binding.btnSignIn.isEnabled = false
////                context?.copyToClipboard(response.response?.otp.toString())
////                context?.toast("copied to clipboard")
//
//                    Log.e("profileStatus ", "old user")
//
//                    /* Setting the animation for the navigation. */
//                    val navBuilder = NavOptions.Builder()
//                    navBuilder.setEnterAnim(R.anim.slide_in_from_right)
//                        .setExitAnim(R.anim.slide_out_to_right)
//                        .setPopEnterAnim(R.anim.slide_in_from_left)
//                        .setPopExitAnim(R.anim.slide_out_left)
//
//                    /* Passing the data to the next fragment. */
//                    val args = Bundle()
//                    args.putString("otp", response.response[0]?.otp)
////                    args.putString(
////                        "userType",
////                        response.response[0]?.usertype.toString().toLowerCase()
////                    )
//                    args.putString("number", binding.etNumber.text.toString())
//
//                    /* Navigating to the next fragment. */
//                    findNavController().navigate(R.id.otpVerifyFragment, args, navBuilder.build())
////
////                    when (response.response[0].usertype.toLowerCase()) {
////                        "new" -> {
////                            Log.e("profileStatus ", "new user")
////
////                            context?.toast("User not registered, redirecting to SignUp page !")
////
////                            /* Setting the animation for the navigation. */
////                            val navBuilder = NavOptions.Builder()
////                            navBuilder.setEnterAnim(R.anim.slide_in_from_right)
////                                .setExitAnim(R.anim.slide_out_to_right)
////                                .setPopEnterAnim(R.anim.slide_in_from_left)
////                                .setPopExitAnim(R.anim.slide_out_left)
////
////                            /* Passing the data to the next fragment. */
////                            val args = Bundle()
////                            args.putString("number", loginReq.mobile)
////
////                            /* Navigating to the next fragment. */
////                            findNavController().navigate(R.id.signUpFragment, args, navBuilder.build())
////                        }
////                        "old" -> {
////
////                        }
////                        else -> {
////                            binding.root.snackbar("Error: user not found, please contact admin.")
////                        }
////                    }
//                } else {
//                    (activity as AuthActivity).apiErrorDialog(response.message)
//                    binding.progressBar.visibility = View.GONE
//                    binding.btnSignIn.isEnabled = true
//                }
//            } else {
//                binding.btnSignIn.isEnabled = true
//                (activity as AuthActivity).apiErrorDialog(Constants.apiErrors)
//            }
//        }
//        viewModel.signIn(loginReq)
//    }
}