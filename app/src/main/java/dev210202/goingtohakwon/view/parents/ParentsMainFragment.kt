package dev210202.goingtohakwon.view.parents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentParentsMainBinding
import dev210202.goingtohakwon.model.Attendance
import dev210202.goingtohakwon.utils.getDaysOfWeek
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel

class ParentsMainFragment : BaseFragment<FragmentParentsMainBinding>(
    R.layout.fragment_parents_main,
) {
    private val viewModel: DataViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        binding.hakwonName = viewModel.getHakwonName()

        binding.tvNoticeAll.setOnClickListener {
            findNavController().navigate(
                ParentsMainFragmentDirections.actionParentsMainFragmentToNoticeFragment(),
            )
        }
        binding.tvAttendanceAll.setOnClickListener {
            findNavController().navigate(
                ParentsMainFragmentDirections.actionParentsMainFragmentToAttendanceFragment(),
            )
        }

        viewModel.getNotice(viewModel.getHakwonName(), isFail = {
            showSnackBar(it.message)
        })
        viewModel.getAttendancesOnName(
            studentName = viewModel.getStudentName(),
            hakwonName = viewModel.getHakwonName(),
            phone = viewModel.getPhone(),
        )

        viewModel.noticeList.observe(viewLifecycleOwner) { noticeList ->
            if (noticeList.isNotEmpty()) {
                val sortedList = noticeList.sortedByDescending { it.date }
                binding.notice = sortedList.first()
            }
        }
        viewModel.attendanceList.observe(viewLifecycleOwner) { attendanceList ->
            if (attendanceList.isNotEmpty()) {
                val thisWeekAttendanceList = mutableListOf<Attendance>()
                val thisWeek = getDaysOfWeek()
                val tvStates = getTvStateMap(thisWeek)

                thisWeek.forEach { day ->
                    attendanceList.find { it.date == day }
                        ?.let { thisWeekAttendanceList.add(it) }
                }
                thisWeekAttendanceList.forEach { attendance ->
                    when (attendance.state) {
                        "출석" -> {
                            tvStates[attendance.date]?.let { it.text = "○" }
                        }
                        "지각" -> {
                            tvStates[attendance.date]?.let { it.text = "△" }
                        }
                        "결석" -> {
                            tvStates[attendance.date]?.let { it.text = "✕" }
                        }
                    }
                }
                tvStates[getToday()]?.let {
                    it.background = resources.getDrawable(R.drawable.layout_stroke_bottom)
                }
            }
        }
    }

    private fun getTvStateMap(thisWeek: List<String>) =
        mapOf(
            thisWeek[0] to binding.tvMon,
            thisWeek[1] to binding.tvTue,
            thisWeek[2] to binding.tvWed,
            thisWeek[3] to binding.tvThu,
            thisWeek[4] to binding.tvFri,
            thisWeek[5] to binding.tvSat,
            thisWeek[6] to binding.tvSun,
        )
}
