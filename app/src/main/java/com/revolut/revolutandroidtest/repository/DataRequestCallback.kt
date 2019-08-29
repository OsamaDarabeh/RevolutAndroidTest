package com.revolut.revolutandroidtest.repository

interface DataRequestCallback {
    fun onSuccess()

    fun onError(errorMessage: String)

    fun onLoading()

    fun onNoData()

    fun onNoInternet()

    fun onFailure(throwable: Throwable)
}