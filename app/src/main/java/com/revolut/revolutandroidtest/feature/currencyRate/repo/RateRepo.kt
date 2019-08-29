package com.revolut.revolutandroidtest.feature.currencyRate.repo

import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.revolut.revolutandroidtest.R
import com.revolut.revolutandroidtest.repository.model.CurrencyRate
import com.revolut.revolutandroidtest.utils.App
import com.revolut.revolutandroidtest.utils.App.Companion.TAG_ERROR
import com.revolut.revolutandroidtest.utils.App.Companion.TAG_PARAM
import com.revolut.revolutandroidtest.utils.App.Companion.TAG_SUCCESS
import com.revolut.revolutandroidtest.utils.ResourceProvider
import io.reactivex.disposables.CompositeDisposable
import com.revolut.revolutandroidtest.repository.DataRequestCallback
import com.revolut.revolutandroidtest.utils.isConnectingToInternet
import timber.log.Timber


class RateRepo {


    /**
     * init the variable
     */
    private val mGson = Gson()
    private val compositeDisposable = CompositeDisposable()


    /**
     * Dispose all RX Retrofit Api requests
     */
    fun clearAllRequest() {
        compositeDisposable.dispose()
    }


    /**
     * Get the list of currency list
     * @param mCallBack
     * @param mCurrencyListLiveData
     */
    fun getListOfCurrencies(
        based: String, mCallBack: DataRequestCallback,
        mCurrencyListLiveData: MutableLiveData<CurrencyRate>
    ) {
        // check internet
        if (!isConnectingToInternet()) {
            mCallBack.onError(ResourceProvider(App.getInstance()).getString(R.string.msg_no_internet))
            mCallBack.onNoInternet()
            return
        }

        // show loading dialog
        mCallBack.onLoading()
        // set the parameter
        val params = ArrayMap<String, String>()
        params["base"] = based
        Timber.tag(TAG_PARAM).i(params.toString())
        //send the request and return the data from server side
        val subscribe = App.getInstance().getRetrofitModel().getRatesList(params)
            .subscribe({ currencyRate ->
                Timber.tag(TAG_SUCCESS).d(currencyRate.toString())
                try {
                    mCurrencyListLiveData.postValue(currencyRate)
                    mCallBack.onSuccess()
                } catch (e: Exception) {
                    mCallBack.onError(e.message.toString())
                }

            }, { throwable ->
                Timber.tag(TAG_ERROR).e(throwable.message.toString())
                mCallBack.onError(throwable.message ?: "")
            })

        compositeDisposable.add(subscribe)
    }


}