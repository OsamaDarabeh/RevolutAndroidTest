package com.revolut.revolutandroidtest.repository


import androidx.collection.ArrayMap
import com.revolut.revolutandroidtest.repository.model.CurrencyRate
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface RetrofitApis {


    @GET("/latest")
    fun getRatesList(@QueryMap params: ArrayMap<String, String>): Observable<CurrencyRate>


}