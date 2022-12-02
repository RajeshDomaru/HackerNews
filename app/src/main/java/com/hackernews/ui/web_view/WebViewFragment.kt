package com.hackernews.ui.web_view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.hackernews.R
import com.hackernews.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private lateinit var binding: FragmentWebViewBinding

    private val args: WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWebViewBinding.bind(view)

        loadWebView()

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {

        with(binding) {

            (requireActivity() as AppCompatActivity).supportActionBar?.title = args.title

            wvStory.apply {

                webViewClient = object : WebViewClient() {
                    @Deprecated("Deprecated in Java")
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        url: String
                    ): Boolean {
                        view.loadUrl(url)
                        return true
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        pbWebView.visibility = View.VISIBLE
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        pbWebView.visibility = View.INVISIBLE
                    }
                }

                val webSetting: WebSettings = settings

                webSetting.javaScriptEnabled = true

                canGoBack()

                setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.action == MotionEvent.ACTION_UP
                        && canGoBack()
                    ) {

                        goBack()
                        return@OnKeyListener true

                    }
                    false
                })

                loadUrl(args.url)
                settings.apply {
                    javaScriptEnabled = true
                    allowContentAccess = true
                    domStorageEnabled = true
                    useWideViewPort = true
                }

            }

        }

    }

}