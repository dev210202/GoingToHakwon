package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentAdminAttendancePersonBinding
import dev210202.goingtohakwon.decorators.*
import dev210202.goingtohakwon.utils.*
import dev210202.goingtohakwon.view.DataViewModel

class AdminAttendancePersonFragment : BaseFragment<FragmentAdminAttendancePersonBinding>(
	R.layout.fragment_admin_attendance_person
) {

	private val viewModel: DataViewModel by activityViewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.calendarView.addDecorators(
			TodayDecorator(requireContext())
		)
		binding.btnSearch.setOnClickListener {
			viewModel.getAttendancesOnName(
				hakwonName = viewModel.getHakwonName(),
				studentName = binding.etName.text.toString(),
				phone = binding.etPhone.text.toString()
			)
			showSnackBar("검색이 완료되었습니다.")
		}

		binding.fabCalendar.setOnClickListener {
			findNavController().navigate(
				AdminAttendancePersonFragmentDirections.actionAdminAttendancePersonFragmentToAdminAttendanceFragment()
			)
		}

		viewModel.attendanceList.observe(this) { attendanceList ->
			val attendList = attendanceList.filter { it.state == "출석" }
			val lateList = attendanceList.filter { it.state == "지각" }
			val absentList = attendanceList.filter { it.state == "결석" }
			attendList.find { it.date == getToday() }?.let {
				binding.calendarView.addDecorators(
					AttendDecorator(attendList.convertCalendarDayList(), requireContext()),
					LateDecorator(lateList.convertCalendarDayList(), requireContext()),
					AbsentDecorator(absentList.convertCalendarDayList(), requireContext()),
					TodayAttendDecorator(requireContext())
				)
			}
			lateList.find { it.date == getToday() }?.let {
				binding.calendarView.addDecorators(
					AttendDecorator(attendList.convertCalendarDayList(), requireContext()),
					LateDecorator(lateList.convertCalendarDayList(), requireContext()),
					AbsentDecorator(absentList.convertCalendarDayList(), requireContext()),
					TodayLateDecorator(requireContext())
				)
			}
			absentList.find { it.date == getToday() }?.let {
				binding.calendarView.addDecorators(
					AttendDecorator(attendList.convertCalendarDayList(), requireContext()),
					LateDecorator(lateList.convertCalendarDayList(), requireContext()),
					AbsentDecorator(absentList.convertCalendarDayList(), requireContext()),
					TodayAbsentDecorator(requireContext())
				)
			}

		}
	}
}