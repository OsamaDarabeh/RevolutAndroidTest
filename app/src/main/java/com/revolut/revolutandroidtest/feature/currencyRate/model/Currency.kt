package com.revolut.revolutandroidtest.feature.currencyRate.model

class Currency(
    var name: String = "",
    var desc: String = "",
    var value: Double = 0.0,
    var showValue: Double = 0.0,
    var image: Int = 0
){
    override fun toString(): String {
        return "Currency(name='$name', desc='$desc', value=$value, showValue=$showValue, image=$image)"
    }
}
