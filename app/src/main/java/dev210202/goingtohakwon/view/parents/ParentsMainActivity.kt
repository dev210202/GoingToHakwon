package dev210202.goingtohakwon.view.parents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dutch2019.base.BaseActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dev210202.goingtohakwon.DayDecorator
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.TodayDecorator
import dev210202.goingtohakwon.databinding.ActivityParentsMainBinding
import dev210202.goingtohakwon.utils.getDay
import dev210202.goingtohakwon.utils.getMonth
import dev210202.goingtohakwon.utils.getYear
import dev210202.goingtohakwon.view.DataViewModel

class ParentsMainActivity : BaseActivity<ActivityParentsMainBinding>(
	R.layout.activity_parents_main
) {
	private val viewModel: DataViewModel by viewModels()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
		val navController = navHostFragment.navController

		NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

		intent.getStringExtra("hakwonName")?.let { viewModel.setHakwonName(it) }
		intent.getStringExtra("childName")?.let { viewModel.setChildName(it) }

		Log.e("ASdASD", viewModel.getChildName())
	}
}