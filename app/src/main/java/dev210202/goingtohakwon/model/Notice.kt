package dev210202.goingtohakwon.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Notice(
	val uuid : String = "",
	val date: String = "",
	val title : String = "",
	val content: String = "",
	val attachment: List<String> = listOf("")
) : Serializable {

	@Exclude
	fun toMap(): Map<String, Any?>{
		return mapOf(
			"uuid" to uuid,
			"date" to date,
			"title" to title,
			"content" to content,
			"attachment" to attachment
		)
	}
}
