package com.education.dynamic.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.fragment_web.*
import com.education.dynamic.interfaces.OnBaseFragmentListener
import com.education.dynamic.R
import com.education.dynamic.exceptions.HTTPCode
import com.education.dynamic.exceptions.HTTPException
import retrofit2.http.HTTP
import android.webkit.WebView




class WebFragment : Fragment() {
    private var url: String? = null
    private var listener: OnWebFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.education.dynamic.R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                listener?.onLoaded()
            }
        })
        listener?.onLoad()
        wvMain.loadUrl(url)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWebFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnWebFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnWebFragmentListener : OnBaseFragmentListener {
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            WebFragment().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
    }
}
