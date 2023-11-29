package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.Notice
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeEditBinding
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel


class AdminNoticeEditFragment : BaseFragment<FragmentAdminNoticeEditBinding>(
	R.layout.fragment_admin_notice_edit
) {
	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentAdapter: AttachmentAdapter by lazy {
		AttachmentAdapter(
			onAttachmentClicked = { uri ->
				viewModel.downloadAttachment(uri,
					isSuccess = { uriResult ->
						val intent = Intent(Intent.ACTION_VIEW)
						intent.data = uriResult
						startActivity(intent)
					}, isFail = {
						showToast(it)
					})
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		val notice = AdminNoticeEditFragmentArgs.fromBundle(requireArguments()).notice
		binding.notice = notice
		val position = AdminNoticeEditFragmentArgs.fromBundle(requireArguments()).position
		attachmentAdapter.setAttachList(notice.attachment)
		binding.rvAttachment.adapter = attachmentAdapter

		binding.btnAddAttachment.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "*/*"
			startActivityForResult(intent, 10)
		}

		binding.btnConfirm.setOnClickListener {
			if (viewModel.getAttachmentList().isNotEmpty()) {
				viewModel.addAttachments(isSuccess = {
					viewModel.editNotice(
						Notice(
							date = getToday(),
							title = binding.etTitle.text.toString(),
							content = binding.etContent.text.toString(),
							attachment = viewModel.getAttachmentList()
						), position, isSuccess = { showToast(it) }, isFail = { showToast(it) })
				}, isFail = {
					showToast(it)
				})

			} else {
				viewModel.editNotice(Notice(
					date = getToday(),
					title = binding.etTitle.text.toString(),
					content = binding.etContent.text.toString(),
					attachment = viewModel.getAttachmentList()
				), position, isSuccess = {
					showToast("성공")
				}, isFail = {
					showToast(it)
				})
			}
		}

		viewModel.attachmentList.observe(this) { list ->
			if (list.isNotEmpty()) {
				attachmentAdapter.setAttachList(viewModel.getAttachmentList())
			}

		}

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
			data?.data?.let {
				viewModel.addAttachmentList(it)
			}
		}
	}
}