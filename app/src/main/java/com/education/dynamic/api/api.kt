package com.education.dynamic.api

import com.education.dynamic.Constants.Companion.PATH
import com.education.dynamic.model.DynamicList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API{
    @GET(PATH)
    fun getItems(@Query("dl") arg: Int) : Call<DynamicList>
}