package com.revolut.revolutandroidtest.repository

import com.google.gson.Gson


data class AppResponse(

    var base: String? = "",
    var date: String? = "",
    var rates: Any? = null

) {

    fun getResult(): String {
        return Gson().toJson(rates)
    }


}