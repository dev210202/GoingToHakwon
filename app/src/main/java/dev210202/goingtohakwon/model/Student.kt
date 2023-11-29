package dev210202.goingtohakwon.model

data class Student(
	val name: String = "",
	val attendance: List<Attendance> = listOf(Attendance()),
	val phone: Int? = null
)
