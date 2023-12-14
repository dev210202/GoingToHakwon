package dev210202.goingtohakwon.view.login

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.base.BaseActivity
import dev210202.goingtohakwon.databinding.ActivityPermissionBinding
import dev210202.goingtohakwon.view.parents.ParentsMainActivity

class PermissionActivity : BaseActivity<ActivityPermissionBinding>(
	R.layout.activity_permission
) {
	private lateinit var hakwonName: String
	private lateinit var childName: String
	private lateinit var phone: String
	private var url =
		"https://github.com/dev210202/imageReosurces/assets/32587845/c25a0bc7-782a-485b-87b1-56fc7f4276e5"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		intent.getStringExtra("hakwonName")?.let { hakwonName = it }
		intent.getStringExtra("childName")?.let { childName = it }
		intent.getStringExtra("phone")?.let { phone = it }

		Glide.with(this)
			.load(url).into(binding.imageView)

		binding.btnPermission.setOnClickListener {
			permissionCheck()
		}

	}

	private fun permissionCheck() {

		val isAllPermissionGranted = requestPermissionList.all { permission ->
			ContextCompat.checkSelfPermission(
				applicationContext,
				permission
			) == PackageManager.PERMISSION_GRANTED
		}
		if (!isAllPermissionGranted) {
			requestOpen()
		}
		else{
			startParentsActivity()
		}
	}

	private fun requestOpen() {
		ActivityCompat.requestPermissions(
			this,
			requestPermissionList,
			REQUEST_CODE_PERMISSIONS
		)
	}

	companion object {
		private const val REQUEST_CODE_PERMISSIONS = 210
		val requestPermissionList = arrayOf(
			android.Manifest.permission.POST_NOTIFICATIONS
		)
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)

		if (requestCode == REQUEST_CODE_PERMISSIONS) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				startParentsActivity()
			} else {
				if (shouldShowRequestPermissionRationale(requestPermissionList[0])) {
					requestOpen()
				} else {
					openSettings()
				}
			}
		}
	}

	private fun openSettings() {
		Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
			addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			data = Uri.fromParts("package", packageName, null)
		}.run(::startActivity)
	}

	private fun startParentsActivity() {
		val intent = Intent(this, ParentsMainActivity::class.java)
		intent.putExtra("hakwonName", hakwonName)
		intent.putExtra("childName", childName)
		intent.putExtra("phone", phone)
		startActivity(intent)
	}
}