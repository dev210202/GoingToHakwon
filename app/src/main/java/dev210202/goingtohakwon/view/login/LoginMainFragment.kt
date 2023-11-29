package dev210202.goingtohakwon.view.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentLoginMainBinding


class LoginMainFragment : BaseFragment<FragmentLoginMainBinding>(
	R.layout.fragment_login_main
) {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.btnAdmin.setOnClickListener {
			findNavController().navigate(
				LoginMainFragmentDirections.actionLoginMainFragmentToAdminLoginFragment()
			)
		}
		binding.btnParents.setOnClickListener {
			findNavController().navigate(
				LoginMainFragmentDirections.actionLoginMainFragmentToParentsLoginFragment()
			)
		}
	}
}