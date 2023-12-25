package dev210202.goingtohakwon.adpater

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev210202.goingtohakwon.databinding.ItemAttachmentEditBinding

class AttachmentEditAdapter(
) : RecyclerView.Adapter<AttachmentEditAdapter.AttachmentViewHolder>() {

	private var attachList = mutableListOf<String>()
	private var addAttachList = mutableListOf<Uri>()
	private var removeAttachList = mutableListOf<String>()

	fun addAttachment(attachment: String) {
		attachList.add(attachment)
		notifyDataSetChanged()
	}

	fun addAttachmentDataList(attachment: Uri) {
		addAttachList.add(attachment)
	}

	private fun addRemoveAttachList(attachment: String) {
		removeAttachList.add(attachment)
	}

	private fun removeAttach(attach: String) {
		attachList.remove(attach)
		notifyDataSetChanged()
	}

	private fun removeAddAttach(attachment: String) {
		addAttachList.find { it.lastPathSegment.toString() == attachment }?.let { attach ->
			addAttachList.remove(attach)
		}
	}


	fun getRemoveAttachList() = removeAttachList
	fun getAttachList() = attachList

	fun getAddAttachList() = addAttachList

	fun setAttachList(list: List<String>) {
		attachList = list.toMutableList()

		notifyDataSetChanged()
	}


	inner class AttachmentViewHolder(
		private val binding: ItemAttachmentEditBinding
	) : ViewHolder(binding.root) {
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

	override fun getItemCount(): Int = attachList.size

	override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {

		val title = attachList[position]
		holder.bind(title)

		OnClickListener {
			if (!addAttachList.map { it.lastPathSegment.toString() }
					.contains(attachList[position])) {
				addRemoveAttachList(attachList[position])
			}
			removeAddAttach(attachList[position])
			removeAttach(attachList[position])
		}.run {
			holder.removeLayout.setOnClickListener(this)
			holder.removeButton.setOnClickListener(this)
		}
//
	}
}