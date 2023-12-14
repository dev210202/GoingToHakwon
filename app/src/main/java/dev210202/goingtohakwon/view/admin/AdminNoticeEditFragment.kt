package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentEditAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeEditBinding
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel


class AdminNoticeEditFragment : BaseFragment<FragmentAdminNoticeEditBinding>(
	R.layout.fragment_admin_notice_edit
) {

	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentEditAdapter: AttachmentEditAdapter by lazy {
		AttachmentEditAdapter(
			onAttachmentClicked = { uri ->
				viewModel.downloadAttachment(
					hakwonName = viewModel.getHakwonName(),
					uri = uri,
					isSuccess = { uriResult ->
						val intent = Intent(Intent.ACTION_VIEW)
						intent.data = uriResult
						startActivity(intent)
					}, isFail = showMessage
				)
			},
			onRemoveAttachmentClicked = { attachment ->
				viewModel.removeAttach(attachment)
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		val notice = AdminNoticeEditFragmentArgs.fromBundle(requireArguments()).notice
		binding.notice = notice
		attachmentEditAdapter.setAttachList(notice.attachment)
		binding.rvAttachment.adapter = attachmentEditAdapter

		binding.btnAddAttachment.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "*/*"
			startActivityForResult(intent, 10)
		}

		binding.btnConfirm.setOnClickListener {
			if (viewModel.getAttachmentList().isNotEmpty()) {
				viewModel.addAttachments(isSuccess = {
					viewModel.editNotice(
						hakwonName = viewModel.getHakwonName(),
						notice = (binding.notice as Notice).copy(
							date = getToday(),
							title = binding.etTitle.text.toString(),
							content = binding.etContent.text.toString(),
							attachment = viewModel.getAttachmentList()
						),
						isSuccess = showMessage,
						isFail = showMessage
					)
				}, isFail = {
					showToast(it.message)
				})

			} else {
				viewModel.editNotice(
					hakwonName = viewModel.getHakwonName(),
					notice = (binding.notice as Notice).copy(
						date = getToday(),
						title = binding.etTitle.text.toString(),
						content = binding.etContent.text.toString(),
						attachment = viewModel.getAttachmentList()
					), isSuccess = showMessage, isFail = showMessage
				)
			}
		}

		viewModel.attachmentList.observe(this) { list ->
			attachmentEditAdapter.setAttachList(viewModel.getAttachmentList())
		}

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
			data?.data?.let {
				viewModel.checkExistAttachment(it, isSuccess = {
					viewModel.addAttachmentList(it)
				}, isFail = { message ->
					showToast(message)
				})
			}
		}
	}
}