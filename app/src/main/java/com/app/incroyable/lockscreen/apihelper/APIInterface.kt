package com.app.incroyable.lockscreen.apihelper

import com.app.incroyable.lockscreen.model.MultipleResource
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface APIInterface {

    @GET
    fun doGetListResources(@Url url: String): Call<MultipleResource>
}