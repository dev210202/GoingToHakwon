package dev210202.goingtohakwon.view.parents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.NoticeAdapter
import dev210202.goingtohakwon.databinding.FragmentNoticeBinding
import dev210202.goingtohakwon.view.DataViewModel

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(
	R.layout.fragment_notice
) {
	private val viewModel: DataViewModel by activityViewModels()
	private val noticeAdapter: NoticeAdapter by lazy {
		NoticeAdapter(
			onLayoutClicked = { notice ->
				findNavController().navigate(
					NoticeFragmentDirections.actionNoticeFragmentToNoticeDetailFragment(notice)
				)
			}
		).apply {
			setHakwonNoticeList(listOf())
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.hakwonName = viewModel.getHakwonName()
		binding.rvNotice.adapter = noticeAdapter

		viewModel.noticeList.observe(this){
			noticeAdapter.setHakwonNoticeList(viewModel.getNoticeList())
		}
	}
}