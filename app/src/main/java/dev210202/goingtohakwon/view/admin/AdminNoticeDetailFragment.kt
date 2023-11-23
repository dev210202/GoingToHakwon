package dev210202.goingtohakwon.view.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeDetailBinding
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.parents.NoticeDetailFragmentArgs


class AdminNoticeDetailFragment : BaseFragment<FragmentAdminNoticeDetailBinding>(
	R.layout.fragment_admin_notice_detail
) {
	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentAdapter: AttachmentAdapter by lazy {
		AttachmentAdapter(
			onAttachmentClicked = { uri ->
				viewModel.downloadAttachment(uri,
					isSuccess = {uriResult ->
						val intent = Intent(Intent.ACTION_VIEW)
						intent.data= uriResult
						startActivity(intent)
					}, isFail = {
						showToast(it)
					})
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val notice = AdminNoticeDetailFragmentArgs.fromBundle(requireArguments()).notice
		binding.notice = notice
		attachmentAdapter.setAttachList(notice.attachment)
		binding.rvAttachment.adapter = attachmentAdapter
	}
}