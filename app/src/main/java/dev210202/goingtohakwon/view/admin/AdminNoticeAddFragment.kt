package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeAddBinding
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel


class AdminNoticeAddFragment : BaseFragment<FragmentAdminNoticeAddBinding>(
	R.layout.fragment_admin_notice_add
) {
	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentAdapter: AttachmentAdapter by lazy {
		AttachmentAdapter(
			onAttachmentClicked = {

			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.rvAttachment.adapter = attachmentAdapter
		binding.btnAddAttachment.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "*/*"
			startActivityForResult(intent, 10)
		}

		binding.btnConfirm.setOnClickListener {
			if (viewModel.getAttachmentList().isNotEmpty()) {
				viewModel.addAttachments(
					isSuccess = {
						viewModel.registNotice(
							hakwonName = viewModel.getHakwonName(),
							notice = Notice(
								date = getToday(),
								title = binding.etTitle.text.toString(),
								content = binding.etContent.text.toString(),
								attachment = viewModel.getAttachmentList()
							),
							isSuccess = {
								showMessage
								findNavController().popBackStack()
							},
							isFail = showMessage
						)
					},
					isFail = showMessage
				)
			} else {
				viewModel.registNotice(
					hakwonName = viewModel.getHakwonName(),
					notice = Notice(
						date = getToday(),
						title = binding.etTitle.text.toString(),
						content = binding.etContent.text.toString(),
						attachment = viewModel.getAttachmentList()
					),
					isSuccess = {
						showMessage
						findNavController().popBackStack()
					},
					isFail = showMessage
				)
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