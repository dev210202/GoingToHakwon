package dev210202.goingtohakwon.view.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AdminNoticeAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeBinding
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel

@SuppressLint("RestrictedApi")
class AdminNoticeFragment : BaseFragment<FragmentAdminNoticeBinding>(
	R.layout.fragment_admin_notice
) {
	private val viewModel: DataViewModel by activityViewModels()

	private val adminNoticeAdapter: AdminNoticeAdapter by lazy {
		AdminNoticeAdapter(
			onLayoutClicked = { notice ->
				findNavController().navigate(
					AdminNoticeFragmentDirections.actionAdminNoticeFragmentToAdminNoticeDetailFragment(
						notice
					)
				)
			},
			onMoreClicked = { notice, position, buttonView ->
				val popup = PopupMenu(requireContext(), buttonView)
				popup.menuInflater.inflate(R.menu.menu_more, popup.menu)
				popup.show()
				popup.setOnMenuItemClickListener { menuItem ->
					when (menuItem.itemId) {
						R.id.dropdown_menu_edit -> {
							findNavController().navigate(
								AdminNoticeFragmentDirections.actionAdminNoticeFragmentToAdminNoticeEditFragment(
									notice
								)
							)
						}
						R.id.dropdown_menu_delete -> {
							viewModel.deleteAttachments(
								hakwonName = viewModel.getHakwonName(),
								uriList = notice.attachment,
								isSuccess = {
									viewModel.deleteNotice(
										viewModel.getHakwonName(),
										notice,
										isSuccess = { showSnackBar(it.message) },
										isFail = { showSnackBar(it.message) }
									)
								},
								isFail = { showSnackBar(it.message) }
							)
						}
					}
					false
				}
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.rvNotice.adapter = adminNoticeAdapter
		viewModel.getNotice(viewModel.getHakwonName(), isFail = {
			showSnackBar(it.message)
		})

		binding.fabAdd.setOnClickListener {
			findNavController().navigate(
				AdminNoticeFragmentDirections.actionAdminNoticeFragmentToAdminNoticeAddFragment()
			)
		}


		viewModel.noticeList.observe(viewLifecycleOwner) { list ->
			adminNoticeAdapter.setHakwonNoticeList(list.sortedByDescending { it.date })
		}
	}
}