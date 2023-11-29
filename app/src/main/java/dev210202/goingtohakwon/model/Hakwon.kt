package dev210202.goingtohakwon.model

import dev210202.goingtohakwon.Notice

data class Hakwon(
	val name: String,
	val students: List<Student> = listOf(),
	val notices: List<Notice> = listOf(),
	val password: String,
)