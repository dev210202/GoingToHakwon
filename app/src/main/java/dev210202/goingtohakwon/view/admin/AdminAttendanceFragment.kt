package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.TodayDecorator
import dev210202.goingtohakwon.adpater.AdminAttendanceAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminAttendanceBinding
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel

class AdminAttendanceFragment : BaseFragment<FragmentAdminAttendanceBinding>(
	R.layout.fragment_admin_attendance
) {

	private val viewModel: DataViewModel by activityViewModels()
	private val adminAttendanceAdapter: AdminAttendanceAdapter by lazy {
		AdminAttendanceAdapter().apply {
			setAttendanceList(listOf())
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


		binding.calendarView.addDecorator(TodayDecorator(requireContext()))
		binding.rvAttendance.adapter = adminAttendanceAdapter
		binding.calendarView.setOnDateChangedListener { widget, date, selected ->

			viewModel.getAttendanceStudents(date.getToday(), isFail = {
				//showToast(it)
			})
		}
		binding.fabPerson.setOnClickListener {
			findNavController().navigate(
				AdminAttendanceFragmentDirections.actionAdminAttendanceFragmentToAdminAttendancePersonFragment()
			)
		}

		viewModel.attendanceStudentList.observe(this) { list ->
			adminAttendanceAdapter.setAttendanceList(list)
		}
	}


}