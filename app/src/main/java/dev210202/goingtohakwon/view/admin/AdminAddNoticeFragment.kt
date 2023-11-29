package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.Notice
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminAddNoticeBinding
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel


class AdminAddNoticeFragment : BaseFragment<FragmentAdminAddNoticeBinding>(
	R.layout.fragment_admin_add_notice
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
						viewModel.addNotice(Notice(
							date = getToday(),
							title = binding.etTitle.text.toString(),
							content = binding.etContent.text.toString(),
							attachment = viewModel.getAttachmentList()
						), isSuccess = {
							showToast("등록 되었습니다.")
						}, isFail = {
							showToast(it)
						})
					},
					isFail = {
						showToast(it)
					})
			} else {
				viewModel.addNotice(Notice(
					date = getToday(),
					title = binding.etTitle.text.toString(),
					content = binding.etContent.text.toString(),
				),
					isSuccess = {
						showToast("등록되었습니다.")
					},
					isFail = {
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