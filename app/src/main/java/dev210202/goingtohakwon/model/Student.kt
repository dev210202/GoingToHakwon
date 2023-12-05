package dev210202.goingtohakwon.model

data class Student(
	val uuid : String="",
	val name: String = "",
	val attendance: List<Attendance> = listOf(),
	val phone: String = ""
)
