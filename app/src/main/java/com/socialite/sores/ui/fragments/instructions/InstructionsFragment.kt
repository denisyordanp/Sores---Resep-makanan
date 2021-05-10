package com.socialite.sores.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.socialite.sores.R
import com.socialite.sores.models.Result
import com.socialite.sores.ui.DetailsActivity
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val result = bundle
        setWebView(view, result)

        return view
    }

    private val bundle: Result?
        get() {
            val args = arguments
            return args?.getParcelable(DetailsActivity.RESULT_BUNDLE_KEY)
        }

    private fun setWebView(view: View, result: Result?) {
        view.instructions_webView.webViewClient = object : WebViewClient() {}
        val websiteUrl = result?.sourceUrl
        if (!websiteUrl.isNullOrEmpty()) {
            view.instructions_webView.loadUrl(websiteUrl)
        }
    }
}