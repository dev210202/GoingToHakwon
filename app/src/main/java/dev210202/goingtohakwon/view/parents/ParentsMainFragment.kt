package dev210202.goingtohakwon.view.parents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentParentsMainBinding
import dev210202.goingtohakwon.utils.getDaysOfWeek
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
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

		binding.layoutAttendance.setOnClickListener {

		}
		binding.layoutNotice.setOnClickListener {
			findNavController().navigate(
				ParentsMainFragmentDirections.actionParentsMainFragmentToNoticeFragment()
			)
		}
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