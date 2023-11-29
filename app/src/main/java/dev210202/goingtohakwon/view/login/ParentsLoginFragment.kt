package dev210202.goingtohakwon.view.login

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentParentsLoginBinding
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel

class ParentsLoginFragment : BaseFragment<FragmentParentsLoginBinding>(
    R.layout.fragment_parents_login
){
	private val viewModel : DataViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.tvRegist.setOnClickListener {
			binding.tvRegistInfo.visibility = View.INVISIBLE
			binding.tvRegist.visibility = View.INVISIBLE
			binding.btnLogin.text = "등록"
		}

	    binding.btnLogin.setOnClickListener {
		    if(binding.btnLogin.text == "등록"){
				viewModel.registChild(
					hakwonName = binding.etHakwonName.text.toString(),
					childName = binding.etChild.text.toString(),
					phone = binding.etPhone.text.toString(),
					isSuccess = {
						// move parentsMain
					},
					isFail = {
						showToast(it.name)
					}
				)
		    }
		    else {
			    viewModel.login(
				    hakwonName = binding.etHakwonName.text.toString(),
				    childName = binding.etChild.text.toString(),
				    phone = binding.etPhone.text.toString(),
				    isSuccess = {
					    // move parentsMain
				    },
				    isFail = {
					    showToast(it)
				    }
			    )
		    }
	    }

	}
}