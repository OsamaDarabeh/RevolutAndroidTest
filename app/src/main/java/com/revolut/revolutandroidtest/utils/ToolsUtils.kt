package com.revolut.revolutandroidtest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Is connecting to internet boolean.
 *
 * @return the boolean
 */
fun isConnectingToInternet(): Boolean {
    val cm = App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    var activeNetwork: NetworkInfo? = null
    if (cm != null) {
        activeNetwork = cm.activeNetworkInfo
    }
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}


/**
 * Returns the currency full name.
 */
fun getCurrencyFullName(context: Context, currencyKey: String): Int {
    return context.resources.getIdentifier(
        "currency_$currencyKey", "string",
        context.packageName
    )
}

/**
 * Returns the currency flag.
 */
fun getCurrencyFlag(context: Context, currencyKey: String): Int {
    return context.resources.getIdentifier(
        "ic_$currencyKey", "mipmap", context.packageName
    )
}


fun get2Digit(myDouble: Double): Double {
    return BigDecimal(myDouble).setScale(2, RoundingMode.HALF_UP).toDouble()
}

fun getFormat2Digit(myNum: Double): String {
    val df = DecimalFormat("0.00")
    return df.format(myNum)
}