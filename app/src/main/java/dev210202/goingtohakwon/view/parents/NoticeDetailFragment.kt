package dev210202.goingtohakwon.view.parents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentAdapter
import dev210202.goingtohakwon.databinding.FragmentNoticeDetailBinding
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel


class NoticeDetailFragment : BaseFragment<FragmentNoticeDetailBinding>(
	R.layout.fragment_notice_detail
) {
	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentAdapter: AttachmentAdapter by lazy {
		AttachmentAdapter(
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
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val notice = NoticeDetailFragmentArgs.fromBundle(requireArguments()).notice
		binding.notice = notice
		Log.e("NOTICe", notice.toString())
		attachmentAdapter.setAttachList(notice.attachment)
		binding.rvAttachment.adapter = attachmentAdapter
	}
}