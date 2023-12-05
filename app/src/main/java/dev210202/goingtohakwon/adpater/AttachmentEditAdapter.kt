package dev210202.goingtohakwon.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev210202.goingtohakwon.databinding.ItemAttachmentBinding
import dev210202.goingtohakwon.databinding.ItemAttachmentEditBinding

class AttachmentEditAdapter(
	private val onAttachmentClicked: (uri: String) -> Unit,
	private val onRemoveAttachmentClicked: (String) -> Unit
) : RecyclerView.Adapter<AttachmentEditAdapter.AttachmentViewHolder>() {

	private var attachmentList = mutableListOf<String>()

	fun setAttachList(list: List<String>) {
		val attachList = list.toMutableList()
		attachList.removeAll { it.isEmpty() }
		attachmentList = attachList
		notifyDataSetChanged()
	}


	inner class AttachmentViewHolder(
		private val binding: ItemAttachmentEditBinding
	) : ViewHolder(binding.root) {
		val attachmentLayout = binding.layoutAttachment
		val removeLayout = binding.layoutRemove
		val removeButton = binding.btnRemove
		fun bind(title: String) {
			binding.title = title
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
		return AttachmentViewHolder(
			ItemAttachmentEditBinding.inflate(
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
		holder.removeLayout.setOnClickListener {
			onRemoveAttachmentClicked(attachmentList[position])
		}
		holder.removeButton.setOnClickListener {
			onRemoveAttachmentClicked(attachmentList[position])
		}
	}
}