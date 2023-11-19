package dev210202.goingtohakwon

data class Attendance(
	val date : String,
	val validate: String
){
	fun getState(): String {
		when(validate){
			"O" ->{
				return "○"
			}
			"X" -> {
				return "✕"
			}
			"V" -> {
				return "△"
			}

		}
		return " "
	}
}
