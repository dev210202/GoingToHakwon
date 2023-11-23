package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.NoticeAdapter
import dev210202.goingtohakwon.databinding.FragmentAdminNoticeBinding
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.parents.NoticeFragmentDirections

class AdminNoticeFragment : BaseFragment<FragmentAdminNoticeBinding>(
	R.layout.fragment_admin_notice
) {
	private val viewModel: DataViewModel by activityViewModels()

	private val noticeAdapter: NoticeAdapter by lazy {
		NoticeAdapter(
			onLayoutClicked = { notice ->
				findNavController().navigate(
					AdminNoticeFragmentDirections.actionAdminNoticeFragmentToAdminNoticeDetailFragment(
						notice
					)
				)
			}
		)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.rvNotice.adapter = noticeAdapter
		viewModel.getNotice(isFail = { message ->
			showToast(message)
		})

		binding.btnAddNotice.setOnClickListener{
			findNavController().navigate(
				AdminNoticeFragmentDirections.actionAdminNoticeFragmentToAdminAddNoticeFragment()
			)
		}

		viewModel.noticeList.observe(this) { list ->
			if (list.isNotEmpty()) {
				noticeAdapter.setHakwonNoticeList(list)
			}
		}
	}
}