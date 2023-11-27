package dev210202.goingtohakwon

import java.io.Serializable

data class Notice(
	val date: String = "",
	val title : String = "",
	val content: String = "",
	val attachment: List<String> = listOf("")
) : Serializable
