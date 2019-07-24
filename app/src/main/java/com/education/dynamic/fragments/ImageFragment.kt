package com.education.dynamic.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.education.dynamic.R
import com.education.dynamic.interfaces.OnBaseFragmentListener
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.fragment_image.*
import java.lang.Exception
import java.util.Collections.singletonList
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.util.*
import com.squareup.picasso.OkHttp3Downloader




class ImageFragment : Fragment() {
    private val TAG = ImageFragment::class.java.simpleName
    private var url: String? = null
    private var listener: OnImageFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .build()

        val picasso = Picasso.Builder(context!!)
            .downloader(OkHttp3Downloader(client))
            .build()

        picasso.load(url)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(object : Target{
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                listener?.onLoad()
            }

            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                listener?.onFail(e)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                listener?.onLoaded()
                Log.i(TAG, "From : "+from.toString())
                ivImage.setImageBitmap(bitmap)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnImageFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnImageFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnImageFragmentListener : OnBaseFragmentListener {
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
    }
}
