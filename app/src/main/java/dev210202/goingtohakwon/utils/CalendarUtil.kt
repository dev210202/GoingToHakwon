package dev210202.goingtohakwon.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

fun getDaysOfWeek(): MutableList<String> {
	val daysOfWeek = mutableListOf<String>()
	val cal = Calendar.getInstance(Locale.KOREA)
	cal.run {
		firstDayOfWeek = Calendar.MONDAY
		var dayOfWeek = Calendar.MONDAY
		repeat(7) {
			set(Calendar.DAY_OF_WEEK, dayOfWeek)
			daysOfWeek.add(cal.time.convertToFormat())
			dayOfWeek++
		}
	}
	return daysOfWeek
}

fun getToday(): String {
	val cal = Calendar.getInstance(Locale.KOREA)
	return cal.time.convertToFormat()
}

fun CalendarDay.getToday() : String{
	return "${this.year}-${this.month + 1}-${this.day}"
}