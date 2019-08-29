package com.revolut.revolutandroidtest.repository.model

import android.util.ArrayMap
import java.io.Serializable

data class CurrencyRate(
    val base: String,
    val date: String,
    val rates: ArrayMap<String, Double>
) : Serializable {
    override fun toString(): String {
        return "CurrencyRate(base='$base', date='$date', rates=$rates)"
    }
}