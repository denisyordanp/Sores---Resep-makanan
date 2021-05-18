package com.socialite.sores.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.socialite.sores.databinding.FragmentInstructionsBinding
import com.socialite.sores.models.Result
import com.socialite.sores.ui.DetailsActivity

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val result = bundle
        setWebView(result)

        return binding.root
    }

    private val bundle: Result?
        get() {
            val args = arguments
            return args?.getParcelable(DetailsActivity.RESULT_BUNDLE_KEY)
        }

    private fun setWebView(result: Result?) {
        binding.instructionsWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl = result?.sourceUrl
        if (!websiteUrl.isNullOrEmpty()) {
            binding.instructionsWebView.loadUrl(websiteUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}