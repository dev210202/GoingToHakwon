package dev210202.goingtohakwon.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentParentsLoginBinding
import dev210202.goingtohakwon.utils.Message
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.parents.ParentsMainActivity

class ParentsLoginFragment : BaseFragment<FragmentParentsLoginBinding>(
	R.layout.fragment_parents_login
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.tvRegist.setOnClickListener {
			binding.tvRegistInfo.visibility = View.INVISIBLE
			binding.tvRegist.visibility = View.INVISIBLE
			binding.btnLogin.text = "등록"
		}

		binding.btnLogin.setOnClickListener {
			if (binding.btnLogin.text == "등록") {
				viewModel.checkExistHakwon(
					hakwonName = binding.etHakwonName.text.toString(),
					result = { message ->
						when (message) {
							Message.REGIST_HAKWON -> {
								registChild()
							}
							Message.NOT_REGIST_HAKWON -> {
								showToast(message = message.message)
							}

							Message.NETWORK_ERROR -> {
								showToast(message = message.message)
							}

							else -> {}
						}
					}
				)
			} else {
				viewModel.checkExistHakwon(
					hakwonName = binding.etHakwonName.text.toString(),
					result = { message ->
						when (message) {
							Message.REGIST_HAKWON -> {
								loginHakwon()
							}
							Message.NOT_REGIST_HAKWON -> {
								showToast(message = message.message)
							}
							Message.NETWORK_ERROR -> {
								showToast(message = message.message)
							}

							else -> {}
						}
					}
				)
			}
		}

	}

	private fun loginHakwon() {
		viewModel.login(
			hakwonName = binding.etHakwonName.text.toString(),
			childName = binding.etChild.text.toString(),
			phone = binding.etPhone.text.toString(),
			isSuccess = {
				viewModel.run {
					setChildName(binding.etChild.text.toString())
					setHakwonName(binding.etHakwonName.text.toString())
					setPhone(binding.etPhone.text.toString())
				}
				startParentsMainActivity()
			},
			isFail = showMessage
		)
	}

	private fun registChild() {
		viewModel.registChild(
			hakwonName = binding.etHakwonName.text.toString(),
			childName = binding.etChild.text.toString(),
			phone = binding.etPhone.text.toString(),
			isSuccess = {
				viewModel.run {
					setChildName(binding.etChild.text.toString())
					setHakwonName(binding.etHakwonName.text.toString())
					setPhone(binding.etPhone.text.toString())
				}
				startParentsMainActivity()
			},
			isFail = showMessage
		)
	}

	private fun startParentsMainActivity() {
		val intent = Intent(requireContext(), ParentsMainActivity::class.java)
		intent.putExtra("hakwonName", viewModel.getHakwonName())
		intent.putExtra("childName", viewModel.getChildName())
		intent.putExtra("phone", viewModel.getPhone())
		startActivity(intent)
	}
}