package dev210202.goingtohakwon.utils

enum class Message(val message : String) {
	NETWORK_ERROR("통신중에 오류가 발생했습니다."),
	NOT_REGIST_STUDENT("등록되지 않은 학생입니다."),
	NOT_REGIST_HAKWON("등록되지 않은 학원입니다."),
	NOT_CORRECT_PW("비밀번호가 일치하지 않습니다."),
	REGIST_HAKWON("이미 등록된 학원입니다."),
	REGIST_STUDENT("등록된 학생입니다."),

	CAN_REGIST_HAKWON("등록할 수 있는 학원입니다.")
}