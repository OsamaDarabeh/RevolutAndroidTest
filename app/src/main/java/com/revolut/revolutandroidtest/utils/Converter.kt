package com.revolut.revolutandroidtest.utils

import android.widget.EditText
import androidx.databinding.InverseMethod

object Converter {

    @InverseMethod("doubleToString")
    fun doubleToString(
        view: EditText, oldValue: Double,
        value: Long
    ): String {
        return ""
    }
}