package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import dev210202.goingtohakwon.CheckTodayDecorator
import dev210202.goingtohakwon.DayDecorator
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.TodayDecorator
import dev210202.goingtohakwon.databinding.FragmentAdminAttendancePersonBinding
import dev210202.goingtohakwon.utils.*
import dev210202.goingtohakwon.view.DataViewModel

class AdminAttendancePersonFragment : BaseFragment<FragmentAdminAttendancePersonBinding>(
	R.layout.fragment_admin_attendance_person
) {

	private val viewModel: DataViewModel by activityViewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


		binding.btnNext.setOnClickListener {
			viewModel.getAttendance(
				name = binding.edittext.text.toString(),
				isFail = {
					showToast(it)
				}
			)
		}

		binding.fabCalendar.setOnClickListener {
			findNavController().navigate(
				AdminAttendancePersonFragmentDirections.actionAdminAttendancePersonFragmentToAdminAttendanceFragment()
			)
		}

		viewModel.attendanceList.observe(this) { attendanceList ->
			val calendarDayList = attendanceList.map {
				CalendarDay.from(
					it.getYear(),
					it.getMonth() - 1,
					it.getDay()
				)
			}
			if (attendanceList.contains(CalendarDay.today().getToday())) {
				binding.calendarView.addDecorators(
					DayDecorator(calendarDayList, requireContext()),
					CheckTodayDecorator(requireContext())
				)
			} else {
				binding.calendarView.addDecorators(
					DayDecorator(calendarDayList, requireContext()),
					TodayDecorator(requireContext())
				)
			}
		}
	}
}