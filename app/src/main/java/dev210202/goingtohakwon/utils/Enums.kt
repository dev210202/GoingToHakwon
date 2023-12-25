package dev210202.goingtohakwon.utils

enum class ResponseMessage(val message : String) {
	NETWORK_ERROR("통신중에 오류가 발생했습니다."),
	TOKEN_ERROR("통신중에 오류가 발생했습니다. 앱을 다시 실행해주세요."),

	NOT_CORRECT_PW("비밀번호가 일치하지 않습니다."),

	NOT_REGIST_STUDENT("등록되지 않은 학생입니다."),
	NOT_REGIST_HAKWON("등록되지 않은 학원입니다."),

	REGIST_HAKWON("이미 등록된 학원입니다."),
	REGIST_STUDENT("등록된 학생입니다."),
	REGIST_NOTICE("안내문이 등록되었습니다."),

	EDIT_NOTICE("안내문이 수정되었습니다."),

	REMOVE_NOTICE("안내문이 삭제되었습니다."),

	SEND_NOTIFICATIONS("알림이 발송되었습니다."),


}