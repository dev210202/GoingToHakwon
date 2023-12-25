package dev210202.goingtohakwon.view.parents

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.FragmentSettingBinding
import dev210202.goingtohakwon.view.login.LoginActivity

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

		binding.layoutNotification.setOnClickListener {
			presentNotificationSetting(requireContext())
		}
	}

	private fun removePreferences() {
		val sharedPref = activity?.getSharedPreferences("parents", Context.MODE_PRIVATE)
		val editor = sharedPref?.edit()
		editor?.remove("hakwonName")
		editor?.remove("studentName")
		editor?.remove("phone")
		editor?.apply()
	}

	fun presentNotificationSetting(context: Context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			notificationSettingOreo(context)
		} else {
			notificationSettingOreoLess(context)
		}

	}

	@RequiresApi(Build.VERSION_CODES.O)
	fun notificationSettingOreo(context: Context) {
		Intent().apply {
			action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
			putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
			flags = Intent.FLAG_ACTIVITY_NEW_TASK
		}.run(::startActivity)
	}

	fun notificationSettingOreoLess(context: Context) {
		Intent().apply {
			action = "android.settings.APP_NOTIFICATION_SETTINGS"
			putExtra("app_package", context.packageName)
			putExtra("app_uid", context.applicationInfo?.uid)
			flags = Intent.FLAG_ACTIVITY_NEW_TASK
		}.run(::startActivity)
	}
}