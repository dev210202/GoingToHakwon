package dev210202.goingtohakwon.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dev210202.goingtohakwon.Notice
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseFragment
import dev210202.goingtohakwon.databinding.FragmentAdminLoginBinding
import dev210202.goingtohakwon.model.Hakwon
import dev210202.goingtohakwon.model.Student
import dev210202.goingtohakwon.utils.showToast
import dev210202.goingtohakwon.view.DataViewModel
import dev210202.goingtohakwon.view.admin.AdminMainActivity

class AdminLoginFragment : BaseFragment<FragmentAdminLoginBinding>(
	R.layout.fragment_admin_login
) {
	private val viewModel: DataViewModel by activityViewModels()
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		binding.tvRegist.setOnClickListener {
			binding.tvRegistInfo.visibility = View.INVISIBLE
			binding.tvRegist.visibility = View.INVISIBLE
			binding.btnLogin.text = "등록"
		}

		binding.btnLogin.setOnClickListener {
			if (binding.btnLogin.text == "등록") {
				viewModel.createHakwon(
					Hakwon(
						name = binding.etHakwonName.text.toString(),
						password = binding.etPassword.text.toString(),
						notices = listOf(Notice()),
						students = listOf(Student())
					),
					isSuccess = {
						// TODO: Toast 표시될 멘트 변경
						showToast("성공")
					},
					isFail = {
						// TODO: Toast 표시될 멘트 변경
						showToast(it.message)
					}
				)
			} else {
				viewModel.adminLogin(
					Hakwon(
						name = binding.etHakwonName.text.toString(),
						password = binding.etPassword.text.toString()
					),
					isSuccess = {
						startAdminMainActivity()
					},
					isFail = {
						// TODO: Toast 표시될 멘트 변경
						showToast(it.message)
					}
				)
			}
		}
	}

	private fun startAdminMainActivity() {
		val intent = Intent(requireContext(), AdminMainActivity::class.java)
		intent.putExtra("hakwonName", binding.etHakwonName.text.toString())
		startActivity(intent)
	}
}