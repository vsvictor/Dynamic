package com.education.dynamic

import android.content.Context
import com.education.dynamic.fragments.ImageFragment
import com.education.dynamic.fragments.MessageFragment
import com.education.dynamic.fragments.WebFragment
import com.education.dynamic.interfaces.OnFail
import com.education.dynamic.model.DynamicItem
import java.lang.Exception

class Agent(val activity: MainActivity) {
    private var onFail : OnFail? = null
    val context : Context = activity.applicationContext
    fun showContent(item : DynamicItem){
        try {
            activity.supportFragmentManager.beginTransaction().replace(
                R.id.llContainer, when (item.function) {
                    "text" -> {MessageFragment.newInstance(item.param)}
                    "image" -> {ImageFragment.newInstance(item.param)}
                    "url" -> {WebFragment.newInstance(item.param)}
                    else -> {throw Exception(context.getString(R.string.undefined_content_type))}
                }).commit()
        }catch (ex : Exception){
            onFail?.onFail(ex)
        }
    }
    fun setOnErrorListener(onFail: OnFail){
        this.onFail = onFail
    }
}