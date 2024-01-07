package dev210202.goingtohakwon.model

import java.io.Serializable

data class Attachment(
	val fileName : String= "",
	val lastPathSegment : String= "",
) : Serializable