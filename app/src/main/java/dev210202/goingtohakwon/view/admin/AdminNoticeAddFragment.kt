package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentEditAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeAddBinding
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel


class AdminNoticeAddFragment : BaseFragment<FragmentAdminNoticeAddBinding>(
	R.layout.fragment_admin_notice_add
) {
	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentEditAdapter: AttachmentEditAdapter by lazy {
		AttachmentEditAdapter(
			onAttachmentClicked = {

			},
			onRemoveAttachmentClicked = { attachment ->
				viewModel.removeAttach(attachment)
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.rvAttachment.adapter = attachmentEditAdapter
		binding.btnAddAttachment.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "*/*"
			startActivityForResult(intent, 10)
		}

		binding.btnConfirm.setOnClickListener {
			if (viewModel.getAttachmentList().isNotEmpty()) {
				viewModel.addAttachments(
					isSuccess = {
						registNotice {
							getAccessToken { accessToken ->
								sendNotification(accessToken)
							}
						}
					},
					isFail = { showToast(it.message) }
				)
			} else {
				registNotice {
					getAccessToken { accessToken ->
						sendNotification(accessToken)
					}
				}
			}
		}
		viewModel.attachmentList.observe(this) { list ->
			attachmentEditAdapter.setAttachList(viewModel.getAttachmentList())
		}
	}

	private fun registNotice(isSuccess: () -> Unit) {
		viewModel.registNotice(
			hakwonName = viewModel.getHakwonName(),
			notice = Notice(
				date = getToday(),
				title = binding.etTitle.text.toString(),
				content = binding.etContent.text.toString(),
				attachment = viewModel.getAttachmentList()
			),
			isSuccess = {
				isSuccess()
			}, isFail = {
				showToast(it.message)
			})

	}

	private fun getAccessToken(isSuccess: (String) -> Unit) {
		val asset = resources.assets.open("goingtohakwon-firebase-adminsdk.json")
		viewModel.getAccessToken(asset, isSuccess = { accessToken ->
			isSuccess(accessToken)
		})
	}

	private fun sendNotification(accessToken : String) {
		viewModel.sendNoticeNotification(
			accessToken = accessToken,
			hakwonName = viewModel.getHakwonName(),
			title = binding.etTitle.text.toString(),
			content = binding.etContent.text.toString(),
			isSuccess = {
				viewModel.resetAttachmentList()
				requireActivity().runOnUiThread {
					showToast(it.message)
					findNavController().popBackStack()
				}
			}, isFail = {
				showToast(it.message)
			})
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