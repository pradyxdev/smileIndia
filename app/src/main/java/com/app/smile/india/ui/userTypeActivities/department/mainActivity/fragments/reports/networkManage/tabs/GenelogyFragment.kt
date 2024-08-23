package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.reports.networkManage.tabs


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.smile.india.databinding.FragmentWebViewBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class GenelogyFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentWebViewBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var title = ""
    private var url = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        binding.clToolbar.visibility = View.VISIBLE
        binding.tvTitle.text = "Genelogy"

        val args = arguments
        args?.let {
            title = it.getString("title", "Home")
            url = it.getString(
                "url",
                "https://smileindiaeshop.com/User/BinaryTreeMobile?userId=" + preferenceManager.userid
            )
        }

        binding.apply {
            tvTitle.text = title
            webView.webViewClient = WebViewClient()
            // this will load the url of the website
            webView.loadUrl(url)
            Log.d("webUrl ", "$url")
            // this will enable the javascript settings, it can also allow xss vulnerabilities
            webView.settings.javaScriptEnabled = true
            // if you want to enable zoom feature
            webView.settings.setSupportZoom(true)
        }
    }

    private fun listeners() {
        binding.apply {

            btnBack.setOnClickListener {

                Navigation.findNavController(it).popBackStack()
            }
        }
    }

//    // if you press Back button this code will work
//    override fun onBackPressed() {
//        // if your webview can go back it will go back
//        if (binding.webView.canGoBack()) {
//            binding.webView.goBack()
//        } else {
//            Navigation.findNavController(binding.root).popBackStack()
//        }
//    }
}