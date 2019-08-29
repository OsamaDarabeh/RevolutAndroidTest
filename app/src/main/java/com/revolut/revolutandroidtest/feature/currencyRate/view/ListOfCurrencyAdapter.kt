package com.revolut.revolutandroidtest.feature.currencyRate.view

import android.content.Context
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.revolutandroidtest.databinding.ItemCurrencyBinding
import com.revolut.revolutandroidtest.feature.currencyRate.model.Currency
import com.revolut.revolutandroidtest.utils.App.Companion.TAG
import com.revolut.revolutandroidtest.utils.get2Digit
import com.revolut.revolutandroidtest.utils.getFormat2Digit


class ListOfCurrencyAdapter :
    RecyclerView.Adapter<ListOfCurrencyAdapter.CurrencyRateViewHolder>() {

    lateinit var listener: OperationCurrencyInterface

    val mCurrencyList: ArrayList<Currency> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemCurrencyBinding.inflate(inflater, parent, false)
        return CurrencyRateViewHolder(binding)
    }

    fun insertData(currency: List<Currency>) {
        mCurrencyList.clear()
        mCurrencyList.addAll(currency)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mCurrencyList.size
    }

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        holder.bind(mCurrencyList[position])
    }

    override fun onBindViewHolder(
        holder: CurrencyRateViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(mCurrencyList[position])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    /**
     * currency rate view holder
     */
    inner class CurrencyRateViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {


        /**
         * binding data with view
         */
        fun bind(currency: Currency) {

            binding.currency = currency

            // set text view currency
            if (currency.showValue != 0.0) {
                binding.itemEtCurrency?.setText(getFormat2Digit(currency.showValue))
            } else {
                binding.itemEtCurrency?.setText("")
            }
            // listeners
            binding.root.setOnClickListener {
                listener.updateTheFirstFiledValue(adapterPosition, currency)
            }
            binding.itemEtCurrency?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    listener.updateTheFirstFiledValue(adapterPosition, currency)
                }
            }

            binding.executePendingBindings()
        }

    }


    fun clearAll() {
        for (it in 0 until mCurrencyList.size) {
            Log.e(TAG, "clearAll:$it")
            mCurrencyList[it].showValue = 0.0
        }
        notifyDataSetChanged()
    }

    fun updateShownValues(value: Double) {
        mCurrencyList.forEach {
            Log.e(TAG, "BeforeUpdateShownValues:$it")
            it.showValue = get2Digit(it.value * value)
            Log.e(TAG, "AfterUpdateShownValues:$it")
        }
        notifyDataSetChanged()
    }

    fun updateRealValues(
        value: ArrayMap<String, Double>,
        currentValue: Double
    ) {
        mCurrencyList.forEach {
            Log.e(TAG, "BeforeUpdateValues:$it")
            Log.e(TAG, "BeforeUpdateValues:${value[it.name]}")
            if (value[it.name] != null) {
                it.value = get2Digit(value[it.name]!!)
                it.showValue = get2Digit(it.value * currentValue)
                Log.e(TAG, "AfterUpdateValues:$it")
            }
        }
        notifyDataSetChanged()
    }


    /**
     * get the item of currency
     */
    fun getItem(pos: Int): Currency? {
        return mCurrencyList[pos]
    }


    /**
     * set the Operations Listener.
     */
    fun setTheOperationsListener(listener: OperationCurrencyInterface) {
        this.listener = listener
    }

    /**
     * operation interface with adapter operations.
     */
    interface OperationCurrencyInterface {

        fun updateTheFirstFiledValue(position: Int, value: Currency)

    }

}