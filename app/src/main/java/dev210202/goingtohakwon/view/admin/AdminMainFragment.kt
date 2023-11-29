package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentAdminMainBinding
import dev210202.goingtohakwon.model.Attendance
import dev210202.goingtohakwon.utils.getTime
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel
/*
	fin - 11.29 17:00
 */
class AdminMainFragment : BaseFragment<FragmentAdminMainBinding>(
	R.layout.fragment_admin_main
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.hakwonName = viewModel.getHakwonName()

		binding.btnYes.setOnClickListener {
			if (binding.etChild.text.toString().isNotEmpty()) {
				checkAttendance("출석")
			} else {
				showToast("이름을 입력하고 출석해주세요.")
			}
		}
		binding.btnLate.setOnClickListener {
			if (binding.etChild.text.toString().isNotEmpty()) {
				checkAttendance("지각")
			} else {
				showToast("이름을 입력하고 출석해주세요.")
			}
		}

		binding.btnNo.setOnClickListener {
			if (binding.etChild.text.toString().isNotEmpty()) {
				checkAttendance("결석")
			} else {
				showToast("이름을 입력하고 출석해주세요.")
			}
		}
	}

	private fun checkAttendance(state: String) {
		viewModel.checkAttendance(
			hakwonName = viewModel.getHakwonName(),
			studentName = binding.etChild.text.toString(),
			date = getToday(),
			time = getTime(),
			state = state,
			isSuccess = {
				showToast("$state 처리되었습니다.")
			},
			isFail = {
				showToast(it.message)
			}
		)
	}
}