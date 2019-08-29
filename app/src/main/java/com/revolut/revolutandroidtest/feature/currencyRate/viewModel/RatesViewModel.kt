package com.revolut.revolutandroidtest.feature.currencyRate.viewModel

import android.app.Application
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.revolut.revolutandroidtest.feature.currencyRate.repo.RateRepo
import com.revolut.revolutandroidtest.repository.model.CurrencyRate
import com.revolut.revolutandroidtest.repository.DataRequestCallback
import timber.log.Timber
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.revolut.revolutandroidtest.BuildConfig
import com.revolut.revolutandroidtest.feature.currencyRate.model.Currency
import com.revolut.revolutandroidtest.utils.DEFAULT_UPDATE_TIME_SEC
import com.revolut.revolutandroidtest.utils.liveDataUtil.Event
import com.revolut.revolutandroidtest.utils.liveDataUtil.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class RatesViewModel(application: Application) : AndroidViewModel(application) {

    private val rateRepo = RateRepo()
    private var firstTime = true
    val currencyFirstItem = ObservableField<Currency>()
    val showProgressBar = ObservableField(true)
    val showNoInternet = ObservableField(false)
    val showListOfCurrencies = ObservableField(false)
    val mCurrencyListLiveData: MutableLiveData<CurrencyRate> = MutableLiveData()

    private val _showMessage = MutableLiveData<Event<String>>()

    val showMessage: LiveData<Event<String>>
        get() = _showMessage

    fun getDataFromServer() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                rateRepo.getListOfCurrencies(
                    currencyFirstItem.get()?.name!!,
                    mCallBack,
                    mCurrencyListLiveData
                )
            }
        }, 0, DEFAULT_UPDATE_TIME_SEC)
    }

    /**
     * Mange fetching data from server side
     */
    private val mCallBack: DataRequestCallback
        get() = object : DataRequestCallback {
            override fun onSuccess() {
                Timber.d("onSuccess: ")
                if (firstTime) {
                    firstTime = false
                    showProgressBar.set(false)
                    showListOfCurrencies.set(true)
                    showNoInternet.set(false)
                }
            }

            override fun onError(errorMessage: String) {
                Timber.d("onError: $errorMessage")
                firstTime = true
                showProgressBar.set(false)
                showListOfCurrencies.set(false)
                showNoInternet.set(false)
                _showMessage.postValue(Event(errorMessage))

            }

            override fun onLoading() {
                Timber.d("onLoading: ")
                if (firstTime) {
                    showProgressBar.set(true)
                    showListOfCurrencies.set(false)
                    showNoInternet.set(false)
                }
            }

            override fun onNoData() {
                Timber.d("onNoData: ")
                firstTime = true
                _showMessage.value = Event("No Data")
                showProgressBar.set(false)
                showListOfCurrencies.set(false)
                showNoInternet.set(false)
            }

            override fun onNoInternet() {
                Timber.d("onNoInternet: ")
                firstTime = true
                _showMessage.postValue(Event("onNoInternet"))
                showProgressBar.set(false)
                showListOfCurrencies.set(false)
                showNoInternet.set(true)
            }

            override fun onFailure(throwable: Throwable) {
                Timber.d("onFailure: ")
                firstTime = true
                _showMessage.postValue(Event("onFailure"))
                showProgressBar.set(false)
                showListOfCurrencies.set(false)
                showNoInternet.set(false)
            }
        }

    /**
     * clear the requests when the activity finish.
     */
    fun clearAllRequest() {
        rateRepo.clearAllRequest()
    }


}