package com.education.dynamic.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.education.dynamic.R
import kotlinx.android.synthetic.main.fragment_message.*


class MessageFragment : Fragment() {
    private var text: String? = null
    private var listener: OnMessageFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            text = it.getString("text")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvText.setText(text)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessageFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnMessageFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnMessageFragmentListener {
    }

    companion object {
        @JvmStatic
        fun newInstance(text: String) =
            MessageFragment().apply {
                arguments = Bundle().apply {
                    putString("text", text)
                }
            }
    }
}
