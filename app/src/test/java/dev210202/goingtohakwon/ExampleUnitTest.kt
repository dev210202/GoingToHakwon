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
	fun string(){
		val title = "예바미술 교습소 안내문 알림안녕하세요 예바미술"

		println(title.split("알림")[0])
		println(title.split("알림")[1])
	}
}