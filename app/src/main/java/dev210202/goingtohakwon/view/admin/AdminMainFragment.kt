package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentAdminMainBinding
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel

class AdminMainFragment : BaseFragment<FragmentAdminMainBinding>(
	R.layout.fragment_admin_main
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.hakwonName = viewModel.getHakwonName()

		binding.btnNext.setOnClickListener {
			viewModel.confirmAttendance(binding.edittext.text.toString(),
				isSuccess = { message ->
					showToast(message)
				},
				isFail = { message ->
					showToast(message)
				})
		}
//		binding.layoutNotice.setOnClickListener {
//			findNavController().navigate(
//				AdminMainFragmentDirections.actionAdminMainFragmentToAdminNoticeFragment()
//			)
//		}
//		binding.layoutAttendance.setOnClickListener {
//			findNavController().navigate(
//				AdminMainFragmentDirections.actionAdminMainFragmentToAdminAttendanceFragment()
//			)
//		}
	}
}