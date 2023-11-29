package dev210202.goingtohakwon.view.parents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dutch2019.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentSettingBinding
import dev210202.goingtohakwon.view.LoginActivity

class SettingFragment : BaseFragment<FragmentSettingBinding>(
	R.layout.fragment_setting
) {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.layoutLogout.setOnClickListener {
			removePreferences()
			val intent = Intent(requireContext(), LoginActivity::class.java)
			startActivity(intent)
			activity?.finish()
		}
	}

	private fun removePreferences() {
		val sharedPref = activity?.getSharedPreferences("hakwon", Context.MODE_PRIVATE)
		val editor = sharedPref?.edit()
		editor?.remove("hakwonName")
		editor?.remove("hakwonPassWord")
		editor?.remove("childName")
		editor?.apply()
	}

}