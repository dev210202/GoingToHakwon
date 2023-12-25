package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentAdminMainBinding
import dev210202.goingtohakwon.utils.getTime
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel

class AdminMainFragment : BaseFragment<FragmentAdminMainBinding>(
	R.layout.fragment_admin_main
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.hakwonName = viewModel.getHakwonName()

		OnClickListener{ view ->
			lateinit var state : String
			when(view){
				binding.btnYes -> state = "출석"
				binding.btnLate -> state = "지각"
				binding.btnNo -> state = "결석"
			}
			if (binding.etChild.text.toString().isNotEmpty()) {
				checkAttendance(state, isSuccess = {
					getStudentToken { studentToken ->
						getAccessToken { accessToken ->
							sendAttendanceNotification(
								studentToken = studentToken,
								accessToken = accessToken,
								state = state
							)
						}

					}
				})
			} else {
				showSnackBar("이름을 입력하고 출석해주세요.")
			}
		}.run {
			binding.btnYes.setOnClickListener(this)
			binding.btnNo.setOnClickListener(this)
			binding.btnLate.setOnClickListener(this)
		}

	}

	private fun checkAttendance(state: String, isSuccess: () -> Unit) {
		viewModel.checkAttendance(
			hakwonName = viewModel.getHakwonName(),
			studentName = binding.etChild.text.toString(),
			date = getToday(),
			time = getTime(),
			phone = binding.etPhone.text.toString(),
			state = state,
			isSuccess = {
				showSnackBar("$state 처리되었습니다.")
				isSuccess()
			},
			isFail = {
				showSnackBar(it.message)
			}
		)
	}

	private fun getStudentToken(isSuccess: (String) -> Unit) {
		viewModel.getStudentToken(
			hakwonName = viewModel.getHakwonName(),
			studentName = binding.etChild.text.toString(),
			phone = binding.etPhone.text.toString(),
			isSuccess = { studentToken ->
				isSuccess(studentToken)
			},
			isFail = {
				showSnackBar(it.message)
			}
		)
	}

	private fun getAccessToken(isSuccess: (String) -> Unit) {
		val asset = resources.assets.open("goingtohakwon-firebase-adminsdk.json")
		viewModel.getAccessToken(asset, isSuccess = { accessToken ->
			isSuccess(accessToken)
		})
	}

	private fun sendAttendanceNotification(
		studentToken: String,
		accessToken: String,
		state: String
	) {
		viewModel.sendAttendanceNotification(
			token = studentToken,
			accessToken = accessToken,
			title = "${viewModel.getHakwonName()} 출석 알림",
			content = "${binding.etChild.text.toString()} 학생이 $state 했습니다.",
			isSuccess = {
				requireActivity().runOnUiThread {
					showSnackBar(it.message)
				}
			}, isFail = {
				requireActivity().runOnUiThread {
					showSnackBar(it.message)
				}
			})
	}
}