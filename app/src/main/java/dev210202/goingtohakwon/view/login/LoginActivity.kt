package dev210202.goingtohakwon.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dev210202.goingtohakwon.base.BaseActivity
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.ActivityLoginBinding
import dev210202.goingtohakwon.view.admin.AdminMainActivity
import dev210202.goingtohakwon.view.parents.ParentsMainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(
	R.layout.activity_login
) {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		checkAdminPreferences()
		checkParentsPreferences()
	}

	private fun checkAdminPreferences() {
		val sharedPref = getSharedPreferences("admin", Context.MODE_PRIVATE) ?: return

		val hakwonName = sharedPref.getString("hakwonName", "")!!
		val password = sharedPref.getString("password", "")!!
		if (hakwonName.isNotEmpty() && password.isNotEmpty()) {
			Intent(this, AdminMainActivity::class.java).apply {
				putExtra("hakwonName", hakwonName)
			}.run(::startActivity)
		}
	}

	private fun checkParentsPreferences() {
		val sharedPref = getSharedPreferences("parents", Context.MODE_PRIVATE) ?: return

		val hakwonName = sharedPref.getString("hakwonName", "")!!
		val studentName = sharedPref.getString("studentName", "")!!
		val phone = sharedPref.getString("phone", "")!!

		if (hakwonName.isNotEmpty() && studentName.isNotEmpty() && phone.isNotEmpty()) {
			Intent(this, ParentsMainActivity::class.java).apply {
				putExtra("hakwonName", hakwonName)
				putExtra("studentName", studentName)
				putExtra("phone", phone)
			}.run(::startActivity)
		}
	}


}