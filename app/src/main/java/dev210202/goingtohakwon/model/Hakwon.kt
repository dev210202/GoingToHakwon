package dev210202.goingtohakwon.model

data class Hakwon(
	val name: String,
	val students: List<Student> = listOf(),
	val notices: List<Notice> = listOf(),
	val password: String,
)