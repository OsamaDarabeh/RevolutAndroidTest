package com.revolut.revolutandroidtest.feature.currencyRate.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.revolut.revolutandroidtest.R
import com.revolut.revolutandroidtest.databinding.ActivityMainBinding
import com.revolut.revolutandroidtest.feature.currencyRate.model.Currency
import com.revolut.revolutandroidtest.feature.currencyRate.viewModel.RatesViewModel
import com.revolut.revolutandroidtest.repository.model.CurrencyRate
import com.revolut.revolutandroidtest.utils.*
import timber.log.Timber
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList


class CurrencyRateActivity : AppCompatActivity(), ListOfCurrencyAdapter.OperationCurrencyInterface,
    TextWatcher {

    private var currentValue: Double = 1.00
    private lateinit var binding: ActivityMainBinding
    private lateinit var mListOfCurrencyAdapter: ListOfCurrencyAdapter
    private lateinit var viewModel: RatesViewModel
    private var fromFocus: Boolean = false
    private var firstTime: Boolean = true
    private lateinit var currency: Currency
    /**
     * on Create method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(RatesViewModel::class.java)
        binding.viewModel = viewModel
        initView()
        initData()
        observeViewModel()
    }

    /**
     * View initialization
     */
    private fun initView() {
        setSupportActionBar(binding.mainTbMain)
        this.supportActionBar?.setTitle(R.string.rates)
        this.supportActionBar?.elevation = 0.0f
        this.supportActionBar?.setDisplayShowTitleEnabled(true)
        this.supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.mainRvCurrencies.setHasFixedSize(true)
        mListOfCurrencyAdapter = ListOfCurrencyAdapter()
        binding.mainRvCurrencies.adapter = mListOfCurrencyAdapter
        mListOfCurrencyAdapter.setTheOperationsListener(this)
    }

    /**
     * init the data by create the first currency and call the API of currencies.
     */
    private fun initData() {
        initFirstCurrency()
        // set the currency to view model.
        viewModel.currencyFirstItem.set(currency)
        // set data to edit text in first item.
        binding.mainInFirstItem.itemEtCurrency?.setText(currency.showValue.toString())
        binding.mainInFirstItem.itemEtCurrency?.addTextChangedListener(this)
        viewModel.getDataFromServer() // call the server
    }

    /**
     * init the first Currency based a Default currency.
     */
    private fun initFirstCurrency() {
        currency = Currency(
            DEFAULT_CURRENCY,
            getString(getCurrencyFullName(this, DEFAULT_CURRENCY.toLowerCase(Locale.ENGLISH))),
            1.00,
            1.00,
            getCurrencyFlag(this, DEFAULT_CURRENCY.toLowerCase(Locale.ENGLISH))
        )
    }

    /**
     * observer view model
     */
    private fun observeViewModel() {
        viewModel.mCurrencyListLiveData.observe(this, Observer { addDataToList(it) })
        viewModel.showMessage.observe(this, Observer { showMessage(it.getContentIfNotHandled()) })
    }

    private fun showMessage(it: String?) {
        Toast.makeText(this, it!!, Toast.LENGTH_LONG).show()
    }

    /**
     * add the data to list from the API.
     * @param currencyRate the list of rate and the base of currency rate return from server.
     */
    private fun addDataToList(currencyRate: CurrencyRate) {
        if (firstTime) {
            firstTime = false
            val listOfCurrency = ArrayList<Currency>()
            for (rate in currencyRate.rates.iterator()) {
                val currency = Currency()
                currency.name = rate.key
                currency.value = rate.value.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                currency.showValue =
                    rate.value.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                currency.desc =
                    getString(getCurrencyFullName(this, rate.key.toLowerCase(Locale.ENGLISH)))
                currency.image = getCurrencyFlag(this, rate.key.toLowerCase(Locale.ENGLISH))
                listOfCurrency.add(currency)
            }
            mListOfCurrencyAdapter.insertData(listOfCurrency)
        } else {
            mListOfCurrencyAdapter.updateRealValues(currencyRate.rates, currentValue)
        }
    }


    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    /**
     *  When the entered currency changing the other values changed based on it.
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!fromFocus) {
            if (!s.isNullOrBlank()) {
                Log.d(App.TAG, "Not Null Or Blank ,setValue($s)")
                // update the value for each currency.
                currentValue = get2Digit(s.toString().toDouble())
                mListOfCurrencyAdapter.updateShownValues(currentValue)
            } else {
                Log.d(App.TAG, "clearAll")
                currentValue = 0.0
                // clear all values that shown to user.
                mListOfCurrencyAdapter.clearAll()
            }
        }
    }

    /**
     * update the first filed when chooses by the clicking on it.
     * @param position
     * @param value
     */
    override fun updateTheFirstFiledValue(position: Int, value: Currency) {
        Log.d(App.TAG, "updateTheFirstFiledValue/valueBefore:$value")
        // from focus to stop the text watcher listener to listen of changing.
        fromFocus = true
        // edit the first item view
        if (value.showValue != 0.0) {
            // set the current value to value of selected currency.
            currentValue = value.showValue
            binding.mainInFirstItem.itemEtCurrency?.setText(getFormat2Digit(value.showValue))
        } else
            binding.mainInFirstItem.itemEtCurrency?.setText("")

        viewModel.currencyFirstItem.set(value)

        // from focus to return the text watcher listener to listen of changing.
        fromFocus = false
        // update the adapter
        mListOfCurrencyAdapter.mCurrencyList.removeAt(position)
        mListOfCurrencyAdapter.notifyItemRemoved(position)

        mListOfCurrencyAdapter.mCurrencyList.add(0, currency)
        mListOfCurrencyAdapter.notifyItemInserted(0)
        // set the new value
        currency = value

        // update the value of based on the selected currency.
        mListOfCurrencyAdapter.mCurrencyList.forEach {
            Log.d(App.TAG, "ListValueBefore:$it")
            it.value = it.value / currency.value
            it.showValue = get2Digit(it.value * currency.showValue)
            Log.d(App.TAG, "ListValueAfter:$it")
        }
        mListOfCurrencyAdapter.notifyDataSetChanged()

        // reset the selected currency to 1.0
        currency.value = 1.00
        Log.d(App.TAG, "updateTheFirstFiledValue/valueAfter:$currency")


    }

    /**
     * Activity destroy
     */
    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearAllRequest()
    }
}
