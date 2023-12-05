package dev210202.goingtohakwon.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import dev210202.goingtohakwon.utils.Message
import dev210202.goingtohakwon.utils.showToast

abstract class BaseFragment<B : ViewDataBinding>(
    layoutRes: Int
) : Fragment(layoutRes) {

    protected lateinit var binding: B
        private set
    val showMessage = { message : Message -> showToast(message.message)}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.bind(view!!)!!
        binding.lifecycleOwner = viewLifecycleOwner
        return view
    }

}