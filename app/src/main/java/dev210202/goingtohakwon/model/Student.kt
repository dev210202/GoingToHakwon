package dev210202.goingtohakwon.model

data class Student(
	val attendance: List<Attendance> = listOf(Attendance()),
	val phone: Int = 0
)
