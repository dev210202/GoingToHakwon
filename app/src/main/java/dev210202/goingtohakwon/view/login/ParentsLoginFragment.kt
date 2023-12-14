package dev210202.goingtohakwon.view.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentParentsLoginBinding
import dev210202.goingtohakwon.utils.ResponseMessage
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.parents.ParentsMainActivity

class ParentsLoginFragment : BaseFragment<FragmentParentsLoginBinding>(
	R.layout.fragment_parents_login
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.tvRegist.setOnClickListener {
			findNavController().navigate(
				ParentsLoginFragmentDirections.actionParentsLoginFragmentToParentsRegistFragment()
			)
		}

		binding.btnLogin.setOnClickListener {
			viewModel.checkExistHakwon(
				hakwonName = binding.etHakwonName.text.toString(),
				result = { message ->
					when (message) {
						ResponseMessage.REGIST_HAKWON -> {
							getFirebaseToken { token ->
								checkExistStudent {
									updateStudentToken(token){
										if(binding.checkbox.isChecked){
											setParentsPreferences()
										}
										if (isAllPermissionGranted()) {
											startParentsMainActivity()
										} else {
											startPermissionActivity()
										}
									}
								}
							}
						}
						else -> {
							showToast(message = message.message)
						}
					}
				}
			)
		}
	}

	private fun isAllPermissionGranted() : Boolean {
		return PermissionActivity.requestPermissionList.all { permission ->
			ContextCompat.checkSelfPermission(
				requireContext(),
				permission
			) == PackageManager.PERMISSION_GRANTED
		}
	}

	private fun setParentsPreferences() {
		val sharedPref = activity?.getSharedPreferences("parents",Context.MODE_PRIVATE) ?: return
		with (sharedPref.edit()) {
			putString("hakwonName", binding.etHakwonName.text.toString())
			putString("studentName", binding.etChild.text.toString())
			putString("phone", binding.etPhone.text.toString())
			apply()
		}
	}

	private fun updateStudentToken(token: String, isSuccess: () -> Unit) {
		viewModel.updateStudentToken(
			hakwonName = binding.etHakwonName.text.toString(),
			studentName = binding.etChild.text.toString(),
			phone = binding.etPhone.text.toString(),
			token = token,
			isSuccess = {
				isSuccess()
			},
			isFail = { showToast(it.message) }
		)
	}

	private fun getFirebaseToken(isSuccess: (String) -> Unit) {
		viewModel.getToken(
			hakwonName = binding.etHakwonName.text.toString(),
			isSuccess = { token ->
				isSuccess(token)
			},
			isFail = {
				showToast(it.message)
			}
		)
	}

	private fun checkExistStudent(isSuccess: () -> Unit) {
		viewModel.checkExistStudent(
			hakwonName = binding.etHakwonName.text.toString(),
			studentName = binding.etChild.text.toString(),
			phone = binding.etPhone.text.toString(),
			result = { message ->
				when (message) {
					ResponseMessage.REGIST_STUDENT -> {
						isSuccess()
					}
					else -> {
						showToast(message.message)
					}
				}

			}
		)
	}


	private fun startParentsMainActivity() {
		val intent = Intent(requireContext(), ParentsMainActivity::class.java)
		intent.putExtra("hakwonName", viewModel.getHakwonName())
		intent.putExtra("childName", viewModel.getChildName())
		intent.putExtra("phone", viewModel.getPhone())
		startActivity(intent)
	}

	private fun startPermissionActivity() {
		val intent = Intent(requireContext(), PermissionActivity::class.java)
		intent.putExtra("hakwonName", viewModel.getHakwonName())
		intent.putExtra("childName", viewModel.getChildName())
		intent.putExtra("phone", viewModel.getPhone())
		startActivity(intent)
	}



}