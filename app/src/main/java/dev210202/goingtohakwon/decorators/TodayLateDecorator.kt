package dev210202.goingtohakwon.decorators

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import dev210202.goingtohakwon.R


class TodayLateDecorator(context: Context) :
	DayViewDecorator {
	private val drawable = ContextCompat.getDrawable(context, R.drawable.background_calendar_today_late)

	override fun shouldDecorate(day: CalendarDay?): Boolean {
		return CalendarDay.today() == day
	}

	override fun decorate(view: DayViewFacade?) {
		view?.setSelectionDrawable(drawable!!)
	}

}
