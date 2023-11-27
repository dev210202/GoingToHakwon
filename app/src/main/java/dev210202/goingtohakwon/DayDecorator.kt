package dev210202.goingtohakwon

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


class DayDecorator(private var attendanceList: List<CalendarDay>, context: Context) :
	DayViewDecorator {
	private val drawable = ContextCompat.getDrawable(context,R.drawable.background_calendar)

	override fun shouldDecorate(day: CalendarDay?): Boolean {
		return attendanceList.contains(day)
	}

	override fun decorate(view: DayViewFacade?) {
		view?.setSelectionDrawable(drawable!!)
	}

}
