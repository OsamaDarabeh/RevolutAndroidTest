package com.revolut.revolutandroidtest.utils

import android.app.Application
import com.revolut.revolutandroidtest.repository.api.RetrofitModel

class App : Application() {

     private var retrofitModel: RetrofitModel? = null

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    companion object {

        const val TAG = "App"
        const val TAG_PARAM = "Param"
        const val TAG_SUCCESS = "Success"
        const val TAG_ERROR = "Error"

        private lateinit var mInstance: App

        /**
         * Gets instance.
         *
         * @return the instance
         */
        @Synchronized
        fun getInstance(): App {
            return mInstance
        }


    }

    ///////////////////////////////// Retrofit Library ////////////////////////////////////////////
    fun getRetrofitModel(): RetrofitModel {

        if (retrofitModel == null) {
            retrofitModel = RetrofitModel()
        }

        return retrofitModel as RetrofitModel
    }


}