package dev210202.goingtohakwon.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev210202.goingtohakwon.Notice
import dev210202.goingtohakwon.databinding.ItemNoticeBinding

class NoticeAdapter(
	private val onLayoutClicked : (Notice) -> Unit
) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

	var hakwonNoticeList = mutableListOf<Notice>()

	@JvmName("setNoticeList")
	fun setHakwonNoticeList(list: List<Notice>) {
		hakwonNoticeList = list.toMutableList()
		notifyDataSetChanged()
	}

	inner class NoticeViewHolder(
		private val binding: ItemNoticeBinding
	) : RecyclerView.ViewHolder(binding.root) {

		val layout = binding.layoutNotice
		fun bind(position: Int){
			binding.notice = hakwonNoticeList[position]
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
		return NoticeViewHolder(ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun getItemCount(): Int = hakwonNoticeList.size


	override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
		holder.bind(position)
		holder.layout.setOnClickListener {
			onLayoutClicked(hakwonNoticeList[position])
		}
	}
}