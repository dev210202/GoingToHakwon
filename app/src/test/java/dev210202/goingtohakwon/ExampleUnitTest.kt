package dev210202.goingtohakwon

import com.prolificinteractive.materialcalendarview.CalendarDay
import dev210202.goingtohakwon.utils.convertToFormat
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
	@Test
	fun calendar_test() {
		val cal = Calendar.getInstance()
		cal.time = Date()

		val week = mutableListOf<String>().apply {
			repeat(7) {
				add(SimpleDateFormat("yyyy-MM-dd").format(cal.time))
				cal.add(Calendar.DATE, -1)
			}
		}

		println(week.toString())


		assertEquals(4, 2 + 2)
	}

	@Test
	fun calender_test2(){
		val cal = Calendar.getInstance(Locale.KOREA)
		cal.firstDayOfWeek = Calendar.MONDAY
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
		//println(convertFormat(cal.time))
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
		//println(convertFormat(cal.time))
	}

	@Test
	fun dow(){
		val daysOfWeek = mutableListOf<String>()
		val cal = Calendar.getInstance(Locale.KOREA)
		cal.run {
			firstDayOfWeek = Calendar.MONDAY
			var dayOfWeek = Calendar.MONDAY
			repeat(7){
				set(Calendar.DAY_OF_WEEK, dayOfWeek)
				daysOfWeek.add(cal.time.convertToFormat())
				dayOfWeek++
			}
		}
		println(daysOfWeek.toString())
	}

	@Test
	fun d(){
		println(CalendarDay.today())
	}
}


fun convertFormat(date : Date): String {
	return SimpleDateFormat("yyyy-MM-dd").format(date)
}