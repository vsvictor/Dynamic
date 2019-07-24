package com.education.dynamic

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.education.dynamic.Constants.Companion.BASE_URL
import com.education.dynamic.api.API
import com.education.dynamic.exceptions.HTTPCode
import com.education.dynamic.exceptions.HTTPException
import com.education.dynamic.interfaces.OnFail
import com.education.dynamic.interfaces.OnLoad
import com.education.dynamic.model.DynamicItem
import com.education.dynamic.model.DynamicList
import com.education.dynamic.model.ListParser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP
import java.lang.Exception

object DataLoader : LifecycleObserver{
    private val TAG = DataLoader::class.java.simpleName
    private val thread: HandlerThread
    private val handler: Handler

    private val gson: Gson;
    private val client: OkHttpClient
    private val api: API

    private var onFail : OnFail? = null
    private var onLoad : OnLoad? = null
    var items = MutableLiveData<List<DynamicItem>>()

    init {
        gson = GsonBuilder()
            .registerTypeAdapter(
                DynamicList::class.java,
                ListParser()
            )
            .create();

        client = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .cache(null)
            .build();

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        thread = HandlerThread("loader");
        thread.start()
        handler = Handler(thread.looper);
        api = retrofit.create(API::class.java)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getItems(){
        onLoad?.onLoad()
        handler?.post {
            api.getItems(1).enqueue(object : Callback<DynamicList> {
                override fun onResponse(call: Call<DynamicList>, response: Response<DynamicList>) {
                    val resCode = response.code()

                    if (resCode >= 200 && resCode < 300) {
                        items.postValue(response.body())
                        onLoad?.onLoaded()
                    }else{
                        onFail?.onFail(HTTPException(HTTPCode.findByCode(resCode)))
                    }
                }
                override fun onFailure(call: Call<DynamicList>, t: Throwable) {
                    onLoad?.onLoaded()
                    onFail?.onFail(Exception(t))
                }
            })
        }
    }

    fun setOnErrorListener(onFail: OnFail){
        this.onFail = onFail
    }
    fun setOnLoadListener(onLoad: OnLoad){
        this.onLoad = onLoad
    }

}