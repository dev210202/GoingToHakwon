package dev210202.goingtohakwon

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentLoginBinding
import dev210202.goingtohakwon.util.showToast

class LoginFragment : BaseFragment<FragmentLoginBinding>(
	R.layout.fragment_login
) {

	private val viewModel: DataViewModel by activityViewModels()

	private val infoTextViewArray by lazy {
		resources.getStringArray(R.array.info_textview)
	}

	private val hintEditTextArray by lazy {
		resources.getStringArray(R.array.hint_edittext)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		changeViewToStep(step = 1)
		initButtonNext()

	}

	private fun initButtonNext() {
		binding.btnNext.setOnClickListener {
			when (infoText()) {
				is1stStep() -> {
					if(binding.edittext.text.toString().isEmpty()){
						showToast("학원 이름을 입력하세요.")
					}
					else {
						viewModel.checkHakwon(
							name = binding.edittext.text.toString(),
							isSuccess = {
								changeViewToStep(step = 2)
								binding.edittext.inputType =
									InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
							},
							isFail = { message ->
								// showToast
								showToast(message)
							}
						)
					}
				}
				is2ndStep() -> {
					if(binding.edittext.text.toString().isEmpty()){
						showToast("비밀번호를 입력하세요.")
					}
					else{
						viewModel.checkPassword(
							password = binding.edittext.text.toString(),
							isSuccess = {
								changeViewToStep(step = 3)
								binding.edittext.inputType = InputType.TYPE_CLASS_TEXT
							},
							isFail = {message ->
								showToast(message)
							}
						)
					}
				}
				isEndStep() -> {
					if(binding.edittext.text.toString().isEmpty()){
						showToast("이름를 입력하세요.")
					}
					else{
						viewModel.checkName(
							name = binding.edittext.text.toString(),
							isSuccess = {
								findNavController().navigate(
									LoginFragmentDirections.actionLoginFragmentToParentsMainFragment()
								)
							},
							isFail = { message ->
								showToast(message)
							}
						)
					}
				}
			}
			clearEditTextInput()
		}
	}

	private fun infoText() = binding.tvInfo.text.toString()

	private fun clearEditTextInput() {
		binding.edittext.setText("")
	}

	private fun changeViewToStep(step: Int) {
		binding.tvInfo.text = infoTextViewArray[step - 1]
		binding.edittext.hint = hintEditTextArray[step - 1]
	}

	private fun is1stStep() = infoTextViewArray[0]

	private fun is2ndStep() = infoTextViewArray[1]

	private fun isEndStep() = infoTextViewArray.last()

}