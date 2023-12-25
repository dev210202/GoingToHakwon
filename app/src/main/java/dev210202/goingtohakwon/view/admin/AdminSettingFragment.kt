package dev210202.goingtohakwon.view.admin

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentAdminSettingBinding
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.login.LoginActivity

class AdminSettingFragment : BaseFragment<FragmentAdminSettingBinding>(
	R.layout.fragment_admin_setting
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.layoutLogout.setOnClickListener {
			removeAdminPreferences()
			Intent(requireContext(), LoginActivity::class.java).run(::startActivity)
			activity?.finish()
		}

		binding.layoutAttendance.setOnClickListener {
			Intent(requireContext(), AdminAttendanceCheckActivity::class.java).apply {
				putExtra("hakwonName", viewModel.getHakwonName())
			}.run(::startActivity)
		}
	}

	private fun removeAdminPreferences() {
		val sharedPref = activity?.getSharedPreferences("admin", Context.MODE_PRIVATE)
		val editor = sharedPref?.edit()
		editor?.remove("hakwonName")
		editor?.remove("password")
		editor?.apply()
	}
}