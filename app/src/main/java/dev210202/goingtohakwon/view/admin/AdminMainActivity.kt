package dev210202.goingtohakwon.view.admin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dev210202.goingtohakwon.base.BaseActivity
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.databinding.ActivityAdminMainBinding
import dev210202.goingtohakwon.view.DataViewModel

class AdminMainActivity : BaseActivity<ActivityAdminMainBinding>(
	R.layout.activity_admin_main
) {
	private val viewModel: DataViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
		val navController = navHostFragment.navController

		binding.bottomNavigation.apply {
			setupWithNavController(navController)
			setOnItemSelectedListener {item ->
				NavigationUI.onNavDestinationSelected(item, navController)
				navController.popBackStack(item.itemId, inclusive = false)
				true
			}
		}

		intent.getStringExtra("hakwonName")?.let { viewModel.setHakwonName(it) }

	}

}