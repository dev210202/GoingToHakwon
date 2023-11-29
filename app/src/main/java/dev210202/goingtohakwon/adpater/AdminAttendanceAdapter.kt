package dev210202.goingtohakwon.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev210202.goingtohakwon.databinding.ItemAdminAttendanceBinding

class AdminAttendanceAdapter :
	RecyclerView.Adapter<AdminAttendanceAdapter.AdminAttendanceViewHolder>() {

	private var attendanceList = mutableListOf<String>()

	fun setAttendanceList(list:  List<String>){
		attendanceList = list.toMutableList()
		notifyDataSetChanged()
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
			binding.name = attendanceList[position]
		}
	}
}