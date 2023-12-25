package dev210202.goingtohakwon.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentAdminRegistBinding
import dev210202.goingtohakwon.model.Hakwon
import dev210202.goingtohakwon.utils.ResponseMessage
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.admin.AdminMainActivity

class AdminRegistFragment : BaseFragment<FragmentAdminRegistBinding>(
	R.layout.fragment_admin_regist
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.btnRegist.setOnClickListener {
			viewModel.checkExistHakwon(
				hakwonName = binding.etHakwonName.text.toString(),
				result = { message ->
					when (message) {
						ResponseMessage.NOT_REGIST_HAKWON -> {
							registHakwon{
								if(binding.checkbox.isChecked){
									setAdminPreferences()
								}
								startAdminMainActivity()
							}
						}
						else -> {
							showSnackBar(message.message)
						}
					}
				}
			)
		}
	}

	private fun registHakwon(isSuccess: () -> Unit) {
		viewModel.registHakwon(
			Hakwon(
				name = binding.etHakwonName.text.toString(),
				password = binding.etPassword.text.toString()
			),
			isSuccess = {
				isSuccess()
			},
			isFail = {
				showSnackBar(it.message)
			}
		)
	}
	private fun setAdminPreferences() {
		val sharedPref = activity?.getSharedPreferences("admin", Context.MODE_PRIVATE) ?: return
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