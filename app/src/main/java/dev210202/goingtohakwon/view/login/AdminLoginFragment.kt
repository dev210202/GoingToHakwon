package dev210202.goingtohakwon.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentAdminLoginBinding
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.admin.AdminMainActivity

class AdminLoginFragment : BaseFragment<FragmentAdminLoginBinding>(
	R.layout.fragment_admin_login
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.tvRegist.setOnClickListener {
			findNavController().navigate(
				AdminLoginFragmentDirections.actionAdminLoginFragmentToAdminRegistFragment()
			)
		}

		binding.btnLogin.setOnClickListener {
			viewModel.adminLogin(
				hakwonName = binding.etHakwonName.text.toString(),
				inputPassword = binding.etPassword.text.toString(),
				isSuccess = {
					if (binding.checkbox.isChecked) {
						setAdminPreferences()
					}
					startAdminMainActivity()
				},
				isFail = {
					showSnackBar(it.message)
				}
			)
		}
	}

	private fun setAdminPreferences() {
		val sharedPref = activity?.getSharedPreferences("admin",Context.MODE_PRIVATE) ?: return
		with(sharedPref.edit()) {
			putString("hakwonName", binding.etHakwonName.text.toString())
			putString("password", binding.etPassword.text.toString())
			apply()
		}
	}

	private fun startAdminMainActivity() {
		val intent = Intent(requireContext(), AdminMainActivity::class.java)
		intent.putExtra("hakwonName", binding.etHakwonName.text.toString())
		startActivity(intent)
	}
}