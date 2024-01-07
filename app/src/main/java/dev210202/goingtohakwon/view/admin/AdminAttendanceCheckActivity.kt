package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import androidx.activity.viewModels
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.adpater.AdminAttendanceAdapter
import dev210202.goingtohakwon.base.BaseActivity
import dev210202.goingtohakwon.databinding.ActivityAdminAttendanceCheckBinding
import dev210202.goingtohakwon.utils.getTime
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.showSnackBar
import dev210202.goingtohakwon.view.DataViewModel

class AdminAttendanceCheckActivity : BaseActivity<ActivityAdminAttendanceCheckBinding>(
    R.layout.activity_admin_attendance_check,
) {
    private val viewModel: DataViewModel by viewModels()
    private val adminAttendanceAdapter: AdminAttendanceAdapter by lazy {
        AdminAttendanceAdapter().apply {
            setSelectedDate(getToday())
            setAttendanceList(listOf())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra("hakwonName")?.let { viewModel.setHakwonName(it) }

        binding.rvAttendance.adapter = adminAttendanceAdapter

        binding.btnAttendance.setOnClickListener {
            checkAttendance("출석", isSuccess = {
                getStudentToken { studentToken ->
                    getAccessToken { accessToken ->
                        sendAttendanceNotification(
                            studentToken = studentToken,
                            accessToken = accessToken,
                            state = "출석",
                        )
                    }
                }
            })
        }

        binding.btnExit.setOnClickListener {
            checkAttendance("귀가", isSuccess = {
                getStudentToken { studentToken ->
                    getAccessToken { accessToken ->
                        sendAttendanceNotification(
                            studentToken = studentToken,
                            accessToken = accessToken,
                            state = "귀가",
                        )
                    }
                }
            })
        }

        viewModel.attendanceStudentList.observe(this) { list ->
            viewModel.getAttendanceStudentsOnDate(
                date = getToday(),
                hakwonName = viewModel.getHakwonName(),
            )
            adminAttendanceAdapter.setAttendanceList(list)
        }
    }

    private fun checkAttendance(
        state: String,
        isSuccess: () -> Unit,
    ) {
        viewModel.checkAttendance(
            hakwonName = viewModel.getHakwonName(),
            studentName = binding.etStudentName.text.toString(),
            date = getToday(),
            time = getTime(),
            phone = binding.etPhone.text.toString(),
            state = state,
            isSuccess = {
                showSnackBar("$state 처리되었습니다.")
                isSuccess()
            },
            isFail = {
                showSnackBar(it.message)
            },
        )
    }

    private fun getStudentToken(isSuccess: (String) -> Unit) {
        viewModel.getStudentToken(
            hakwonName = viewModel.getHakwonName(),
            studentName = binding.etStudentName.text.toString(),
            phone = binding.etPhone.text.toString(),
            isSuccess = { studentToken ->
                isSuccess(studentToken)
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

    private fun sendAttendanceNotification(
        studentToken: String,
        accessToken: String,
        state: String,
    ) {
        viewModel.sendAttendanceNotification(
            token = studentToken,
            accessToken = accessToken,
            title = "${viewModel.getHakwonName()} 출석 알림",
            content = "${binding.etStudentName.text} 학생이 $state 했습니다.",
            isSuccess = {
                runOnUiThread {
                    showSnackBar(it.message)
                }
            },
            isFail = {
                runOnUiThread {
                    showSnackBar(it.message)
                }
            },
        )
    }
}
