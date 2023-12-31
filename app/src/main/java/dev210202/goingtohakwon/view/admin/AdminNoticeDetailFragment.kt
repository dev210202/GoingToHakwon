package dev210202.goingtohakwon.view.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeDetailBinding
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel


class AdminNoticeDetailFragment : BaseFragment<FragmentAdminNoticeDetailBinding>(
	R.layout.fragment_admin_notice_detail
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
					}, isFail = { showSnackBar(it.message) })
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val notice = AdminNoticeDetailFragmentArgs.fromBundle(requireArguments()).notice
		binding.notice = notice
		attachmentAdapter.setAttachList(notice.attachment)
		binding.rvAttachment.adapter = attachmentAdapter
		View.OnClickListener {
			findNavController().popBackStack()
		}.run {
			binding.layoutBack.setOnClickListener(this)
			binding.btnBack.setOnClickListener(this)
		}
	}
}