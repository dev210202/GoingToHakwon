package dev210202.goingtohakwon.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev210202.goingtohakwon.databinding.ItemAttachmentBinding

class AttachmentAdapter(
	private val onAttachmentClicked: (uri: String) -> Unit
) : RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>() {

	private var attachmentList = mutableListOf<String>()

	fun setAttachList(list: List<String>) {
		val attachList=  list.toMutableList()
		attachList.removeAll{it.isEmpty()}
		if (attachList.isNotEmpty()) {
			attachmentList = attachList
			notifyDataSetChanged()
		}
	}

	inner class AttachmentViewHolder(
		private val binding: ItemAttachmentBinding
	) : ViewHolder(binding.root) {
		val attachmentLayout = binding.layoutAttachment
		fun bind(title: String) {
			binding.title = title
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
		return AttachmentViewHolder(
			ItemAttachmentBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			),
		)
	}

	override fun getItemCount(): Int = attachmentList.size

	override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {

		val title = attachmentList[position]
		holder.bind(title)
		holder.attachmentLayout.setOnClickListener {
			onAttachmentClicked(title)
		}
	}
}