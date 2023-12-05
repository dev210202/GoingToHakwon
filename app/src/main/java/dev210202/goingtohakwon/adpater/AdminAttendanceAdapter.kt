package dev210202.goingtohakwon.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev210202.goingtohakwon.databinding.ItemAdminAttendanceBinding
import dev210202.goingtohakwon.model.Student

class AdminAttendanceAdapter :
	RecyclerView.Adapter<AdminAttendanceAdapter.AdminAttendanceViewHolder>() {

	private var selectedDate = ""
	private var attendanceList = mutableListOf<Student>()

	fun setAttendanceList(list:  List<Student>){
		attendanceList = list.toMutableList()
		notifyDataSetChanged()
	}

	fun setSelectedDate(date : String){
		selectedDate = date
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAttendanceViewHolder {
		return AdminAttendanceViewHolder(
			ItemAdminAttendanceBinding.inflate(
				LayoutInflater.from(
					parent.context
				), parent, false
			)
		)
	}

	override fun getItemCount(): Int = attendanceList.size

	override fun onBindViewHolder(holder: AdminAttendanceViewHolder, position: Int) {
		holder.bind(position)
	}

	inner class AdminAttendanceViewHolder(
		private val binding: ItemAdminAttendanceBinding
	) :
		RecyclerView.ViewHolder(binding.root) {


		fun bind(position: Int) {
			val student = attendanceList[position]
			binding.student = student
			student.attendance.find { it.date == selectedDate }?.let {
				binding.time = it.time
				binding.state = it.state
			}
		}
	}
}