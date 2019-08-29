package com.revolut.revolutandroidtest.repository.api

import androidx.collection.ArrayMap
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.revolut.revolutandroidtest.BuildConfig.BASE_URL
import com.revolut.revolutandroidtest.repository.RetrofitApis
import com.revolut.revolutandroidtest.repository.model.CurrencyRate
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitModel {

    // main variables
    private var api: RetrofitApis? = null
    private var subscribeOn: Scheduler? = null
    private var observeOn: Scheduler? = null


    init {
        retrofitModel()
    }

    private fun retrofitModel() {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        subscribeOn = Schedulers.io()
        observeOn = AndroidSchedulers.mainThread()

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
            .readTimeout(60, TimeUnit.SECONDS)    // socket timeout
            .build()

        this.api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitApis::class.java)
    }


    fun getRatesList(params: ArrayMap<String, String>): Observable<CurrencyRate> {
        return api!!.getRatesList(params)
            .subscribeOn(subscribeOn!!)
            .observeOn(observeOn!!)
    }


}
