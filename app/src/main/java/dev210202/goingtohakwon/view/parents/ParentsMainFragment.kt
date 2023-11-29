package dev210202.goingtohakwon.view.parents

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dev210202.goingtohakwon.DayDecorator
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.TodayDecorator
import dev210202.goingtohakwon.databinding.FragmentParentsMainBinding
import dev210202.goingtohakwon.utils.*
import dev210202.goingtohakwon.view.DataViewModel

class ParentsMainFragment : BaseFragment<FragmentParentsMainBinding>(
	R.layout.fragment_parents_main
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.hakwonName = viewModel.getHakwonName()

		viewModel.getNotice(
			isFail = { message ->
				showToast(message)
			}
		)

		viewModel.getAttendance(
			name = viewModel.getChildName(),
			isFail = { message ->
				showToast(message)
			}
		)
		viewModel.noticeList.observe(this) { noticeList ->
			if (noticeList.isNotEmpty()) {
				val sortedList = noticeList.sortedBy { it.date }
				binding.notice = sortedList.first()
			}
		}
		viewModel.attendanceList.observe(this) { attendanceList ->
			if (attendanceList.isNotEmpty()) {
				val thisWeekAttendanceList = mutableListOf<String>()
				val thisWeek = getDaysOfWeek()
				val tvStates = getTvStateMap(thisWeek)

				thisWeek.forEach { day ->
					attendanceList.find { it == day }?.let { thisWeekAttendanceList.add(it) }
				}
				thisWeekAttendanceList.forEach { day ->
					tvStates[day]?.let { it.text = "○" }
				}
				tvStates[getToday()]?.let {
					it.background = resources.getDrawable(R.drawable.layout_stroke_bottom_white)
				}
			}

		}

//		binding.bottomNavigation.setOnItemSelectedListener {item ->
//			when(item.itemId){
//				R.id.home -> {
//
//				}
//			}
//
//		}

//		binding.layoutAttendance.setOnClickListener {
//
//			val view = layoutInflater.inflate(R.layout.layout_attendance, null)
//			val calendarDayList = viewModel.getAttendanceList()
//				.map { CalendarDay.from(it.getYear(), it.getMonth() - 1, it.getDay()) }
//			val calendarView = view.findViewById<MaterialCalendarView>(R.id.calendarView)
//			calendarView.addDecorators(DayDecorator(calendarDayList, requireContext()), TodayDecorator(requireContext()) )
//
//			val builder = androidx.appcompat.app.AlertDialog.Builder(binding.layoutAttendance.context)
//			builder.setView(view)
//			val dialog = builder.create().apply { show() }
//
//			val closeButton = view.findViewById<ImageButton>(R.id.btn_close)
//			closeButton.setOnClickListener {
//				dialog.dismiss()
//			}
//
//		}
//		binding.layoutNotice.setOnClickListener {
//			findNavController().navigate(
//				ParentsMainFragmentDirections.actionParentsMainFragmentToNoticeFragment()
//			)
//		}
	}

	private fun getTvStateMap(thisWeek: List<String>) = mapOf(
		thisWeek[0] to binding.tvStateMon,
		thisWeek[1] to binding.tvStateTue,
		thisWeek[2] to binding.tvStateWed,
		thisWeek[3] to binding.tvStateThu,
		thisWeek[4] to binding.tvStateFri,
		thisWeek[5] to binding.tvStateSat,
		thisWeek[6] to binding.tvStateSun,
	)
}