package dev210202.goingtohakwon.model

data class Student(
	val attendance: List<Attendance> = listOf(),
	val phone: String = ""
)
