package com.revolut.revolutandroidtest.utils

import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}


@BindingAdapter("src")
fun bindImageResource(imageView: ImageView, idRes: Int) {
    imageView.setImageResource(idRes)
}


