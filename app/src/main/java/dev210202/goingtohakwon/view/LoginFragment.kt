package dev210202.goingtohakwon.view

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentLoginBinding
import dev210202.goingtohakwon.utils.showToast

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
		readPreferences()
		changeViewToStep(step = 1)
		initButtonNext()

	}

	private fun readPreferences() {
		val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

		sharedPref.run {

			getString("hakwonName", null)?.let {
				viewModel.setHakwonName(it)
			}
			getString("hakwonPassWord", null)?.let {
				viewModel.setHakwonPassWord(it)
			}
			getString("childName", null)?.let {
				viewModel.setChildName(it)
			}
			if (isExistPreferences()) {
				findNavController().navigate(
					LoginFragmentDirections.actionLoginFragmentToParentsMainFragment()
				)
			}
		}
	}

	private fun isExistPreferences(): Boolean = viewModel.getHakwonName().isNotEmpty() && viewModel.getHakwonPassWord().isNotEmpty() && viewModel.getChildName().isNotEmpty()


	private fun initButtonNext() {
		binding.btnNext.setOnClickListener {
			when (infoText()) {
				is1stStep() -> {
					if (binding.edittext.text.toString().isEmpty()) {
						showToast("학원 이름을 입력하세요.")
					} else {
						viewModel.checkHakwon(
							name = binding.edittext.text.toString(),
							isSuccess = {
								changeViewToStep(step = 2)
								binding.edittext.inputType =
									InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
							},
							isFail = { message ->
								showToast(message)
							}
						)
					}
				}
				is2ndStep() -> {
					if (binding.edittext.text.toString().isEmpty()) {
						showToast("비밀번호를 입력하세요.")
					} else {
						viewModel.checkPassword(
							inputPassword = binding.edittext.text.toString(),
							isSuccess = { message ->
								when (message) {
									"일반" -> {
										changeViewToStep(step = 3)
										binding.edittext.inputType = InputType.TYPE_CLASS_TEXT
									}
									"관리자" -> {
										findNavController().navigate(
											LoginFragmentDirections.actionLoginFragmentToAdminMainFragment()
										)
									}
								}
							},
							isFail = { message ->
								showToast(message)
							}
						)
					}
				}
				isEndStep() -> {
					if (binding.edittext.text.toString().isEmpty()) {
						showToast("이름를 입력하세요.")
					} else {
						viewModel.checkName(
							name = binding.edittext.text.toString(),
							isSuccess = {

								val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
								val editor = sharedPref?.edit()
								editor?.putString("hakwonName", viewModel.getHakwonName())
								editor?.putString("hakwonPassWord", viewModel.getHakwonPassWord())
								editor?.putString("childName", viewModel.getChildName())
								editor?.apply()
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