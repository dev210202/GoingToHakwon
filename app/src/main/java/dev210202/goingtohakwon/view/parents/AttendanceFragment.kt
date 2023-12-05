package dev210202.goingtohakwon.view.parents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.decorators.TodayDecorator
import dev210202.goingtohakwon.databinding.FragmentAttendanceBinding
import dev210202.goingtohakwon.utils.*
import dev210202.goingtohakwon.view.DataViewModel


class AttendanceFragment : BaseFragment<FragmentAttendanceBinding>(
	R.layout.fragment_attendance
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val calendarDayList = viewModel.getAttendanceList().convertCalendarDayList()
//		if (viewModel.getAttendanceList().contains(CalendarDay.today().getToday())) {
//			binding.calendarView.addDecorators(
//				DayDecorator(calendarDayList, requireContext()),
//				CheckTodayDecorator(requireContext())
//			)
//		} else {
//			binding.calendarView.addDecorators(
//				DayDecorator(calendarDayList, requireContext()),
//				TodayDecorator(requireContext())
//			)
//		}
	}
}