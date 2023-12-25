package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentEditAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeEditBinding
import dev210202.goingtohakwon.utils.*
import dev210202.goingtohakwon.view.DataViewModel


class AdminNoticeEditFragment : BaseFragment<FragmentAdminNoticeEditBinding>(
	R.layout.fragment_admin_notice_edit
) {

	private val viewModel: DataViewModel by activityViewModels()
	private val attachmentEditAdapter: AttachmentEditAdapter by lazy {
		AttachmentEditAdapter()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		val notice = AdminNoticeEditFragmentArgs.fromBundle(requireArguments()).notice
		binding.notice = notice

		attachmentEditAdapter.setAttachList(notice.attachment)
		binding.rvAttachment.adapter = attachmentEditAdapter

		View.OnClickListener {
			findNavController().popBackStack()
		}.run {
			binding.layoutBack.setOnClickListener(this)
			binding.btnBack.setOnClickListener(this)
		}

		binding.btnAddAttachment.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "*/*"
			startActivityForResult(intent, 10)
		}
		binding.layoutAddAttachment.setOnClickListener {
			val intent = Intent(Intent.ACTION_GET_CONTENT)
			intent.type = "*/*"
			startActivityForResult(intent, 10)
		}

		binding.btnConfirm.setOnClickListener {
			viewModel.deleteAttachments(
				hakwonName = viewModel.getHakwonName(),
				uriList = attachmentEditAdapter.getRemoveAttachList(),
				isSuccess = {

				},
				isFail = {
					showSnackBar(it.message)
				}
			)
			viewModel.addAttachments(
				context = requireContext(),
				attachmentList = attachmentEditAdapter.getAddAttachList(),
				isSuccess = {

				},
				isFail = {
					showSnackBar(it.message)
				}
			)
			viewModel.editNotice(
				hakwonName = viewModel.getHakwonName(),
				notice = (binding.notice as Notice).copy(
					date = getToday(),
					title = binding.etTitle.text.toString(),
					content = binding.etContent.text.toString(),
					attachment = attachmentEditAdapter.getAttachList()
				),
				isSuccess = { showSnackBar(it.message) },
				isFail = { showSnackBar(it.message) }
			)
			/*viewModel.deleteAttachments(
				hakwonName = viewModel.getHakwonName(),
				uriList = attachmentEditAdapter.getRemoveAttachList(),
				isSuccess = {
					viewModel.addAttachments(
						attachmentList = attachmentEditAdapter.getAddAttachList(),
						isSuccess = {
							viewModel.editNotice(
								hakwonName = viewModel.getHakwonName(),
								notice = (binding.notice as Notice).copy(
									date = getToday(),
									title = binding.etTitle.text.toString(),
									content = binding.etContent.text.toString(),
									attachment = attachmentEditAdapter.getAttachList()
								),
								isSuccess = { showToast(it.message) },
								isFail = { showToast(it.message) }
							)
						},
						isFail = {
							showToast(it.message)
						}
					)
				},
				isFail = {
					showToast(it.message)
				}
			)

			 */
			/*
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
							isSuccess = { showToast(it.message) },
							isFail = { showToast(it.message) }
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
								.map { it.lastPathSegment.toString() }
						), isSuccess = { showToast(it.message) }, isFail = { showToast(it.message) }
					)
				}
			 */
			//			if (attachmentEditAdapter.getRemoveAttachList().isNotEmpty()) {
			//				viewModel.deleteAttachments(
			//					hakwonName = viewModel.getHakwonName(),
			//					uriList = attachmentEditAdapter.getRemoveAttachList(),
			//					isSuccess = {
			//
			//					},
			//					isFail = {
			//						showToast(it.message)
			//					}
			//				)
			//			}
			//			if (attachmentEditAdapter.getAttachList().isNotEmpty()) {
			//				viewModel.addAttachments(
			//					attachmentList = viewModel.getAttachmentList(),
			//					isSuccess = {
			//
			//					},
			//					isFail = {
			//						showToast(it.message)
			//					}
			//
			//				)
			//			}
			//			viewModel.editNotice(
			//				hakwonName = viewModel.getHakwonName(),
			//				notice = (binding.notice as Notice).copy(
			//					date = getToday(),
			//					title = binding.etTitle.text.toString(),
			//					content = binding.etContent.text.toString(),
			//					attachment = attachmentEditAdapter.getAttachList()
			//				),
			//				isSuccess = {
			//					showToast(it.message)
			//				},
			//				isFail = {
			//					showToast(it.message)
			//				}
			//			)
		}

	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onActivityResult(
		requestCode: Int,
		resultCode: Int,
		data: Intent?
	) {
		if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
			data?.data?.let { uri ->
				if (!attachmentEditAdapter.getAttachList()
						.contains(uri.getFileName(requireContext()))
				) {
					attachmentEditAdapter.addAttachment(uri.getFileName(requireContext()))
					attachmentEditAdapter.addAttachmentDataList(uri)
				} else {
					showSnackBar("같은 이름의 첨부파일이 존재합니다. 다른 이름으로 추가해주세요.")
				}
			}
		}
	}
}