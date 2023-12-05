package dev210202.goingtohakwon.view.parents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentAttendanceBinding
import dev210202.goingtohakwon.decorators.*
import dev210202.goingtohakwon.utils.*
import dev210202.goingtohakwon.view.DataViewModel


class AttendanceFragment : BaseFragment<FragmentAttendanceBinding>(
	R.layout.fragment_attendance
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val attendanceList = viewModel.getAttendanceList()

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