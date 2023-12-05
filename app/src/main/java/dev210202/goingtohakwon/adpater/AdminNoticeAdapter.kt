package dev210202.goingtohakwon.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.databinding.ItemAdminNoticeBinding

class AdminNoticeAdapter(
	private val onLayoutClicked: (Notice) -> Unit,
	private val onMoreClicked: (Notice, Int, LinearLayout) -> Unit
) : RecyclerView.Adapter<AdminNoticeAdapter.AdminNoticeViewHolder>() {

	var hakwonNoticeList = mutableListOf<Notice>()

	@JvmName("setNoticeList")
	fun setHakwonNoticeList(list: List<Notice>) {
		hakwonNoticeList = list.toMutableList()
		Log.e("Notice List", hakwonNoticeList.toString())
		notifyDataSetChanged()
	}

	inner class AdminNoticeViewHolder(
		private val binding: ItemAdminNoticeBinding
	) : RecyclerView.ViewHolder(binding.root) {

		val layout = binding.layoutNotice
		val layoutMore = binding.layoutMore
		val btnMore = binding.btnMore
		fun bind(position: Int) {
			binding.notice = hakwonNoticeList[position]
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminNoticeViewHolder {
		return AdminNoticeViewHolder(
			ItemAdminNoticeBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun getItemCount(): Int = hakwonNoticeList.size


	override fun onBindViewHolder(holder: AdminNoticeViewHolder, position: Int) {
		holder.bind(position)
		holder.layout.setOnClickListener {
			onLayoutClicked(hakwonNoticeList[position])
		}
		holder.btnMore.setOnClickListener {
			onMoreClicked(hakwonNoticeList[position], position, holder.layoutMore)
		}
		holder.layoutMore.setOnClickListener {
			onMoreClicked(hakwonNoticeList[position], position, holder.layoutMore)
		}
	}
}