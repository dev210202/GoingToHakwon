package dev210202.goingtohakwon.model

data class Student(
	val token : String="",
	val name: String = "",
	val attendance: List<Attendance> = listOf(),
	val phone: String = ""
)
