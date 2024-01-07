package dev210202.goingtohakwon.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AttachmentEditAdapter
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeAddBinding
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.utils.getFileName
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel

class AdminNoticeAddFragment : BaseFragment<FragmentAdminNoticeAddBinding>(
    R.layout.fragment_admin_notice_add,
) {
    private val viewModel: DataViewModel by activityViewModels()
    private val attachmentEditAdapter: AttachmentEditAdapter by lazy {
        AttachmentEditAdapter()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
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
            if (attachmentEditAdapter.getAddAttachList().isNotEmpty()) {
                viewModel.addAttachments(
                    context = requireContext(),
                    attachmentList = attachmentEditAdapter.getAddAttachList().toList(),
                    isSuccess = {
                        registNotice {
                            getAccessToken { accessToken ->
                                sendNotification(accessToken)
                            }
                        }
                    },
                    isFail = {
                        showSnackBar(it.message)
                    },
                )
            } else {
                registNotice {
                    getAccessToken { accessToken ->
                        sendNotification(accessToken)
                    }
                }
            }
        }
    }

    private fun registNotice(isSuccess: () -> Unit) {
        viewModel.registNotice(
            hakwonName = viewModel.getHakwonName(),
            notice =
                Notice(
                    date = getToday(),
                    title = binding.etTitle.text.toString(),
                    content = binding.etContent.text.toString(),
                    attachment = attachmentEditAdapter.getAttachList(),
                ),
            isSuccess = {
                isSuccess()
            },
            isFail = {
                showSnackBar(it.message)
            },
        )
    }

    private fun getAccessToken(isSuccess: (String) -> Unit) {
        val asset = resources.assets.open("goingtohakwon-firebase-adminsdk.json")
        viewModel.getAccessToken(asset, isSuccess = { accessToken ->
            isSuccess(accessToken)
        })
    }

    private fun sendNotification(accessToken: String) {
        viewModel.sendNoticeNotification(
            accessToken = accessToken,
            hakwonName = viewModel.getHakwonName(),
            title = "${viewModel.getHakwonName()} 안내문 알림",
            content = binding.etTitle.text.toString(),
            isSuccess = {
                requireActivity().runOnUiThread {
                    findNavController().navigate(
                        AdminNoticeAddFragmentDirections.actionAdminNoticeAddFragmentToAdminNoticeFragment(),
                    )
                }
            },
            isFail = {
                showSnackBar(it.message)
            },
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
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
