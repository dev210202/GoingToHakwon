package com.dutch2019.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding>(
    layoutRes: Int
) : AppCompatActivity(layoutRes) {

    val binding by lazy {
        DataBindingUtil.bind<B>(
            (window.decorView.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
        )!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}