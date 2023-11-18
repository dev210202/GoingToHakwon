package dev210202.goingtohakwon

import android.util.Log
import com.dutch2019.base.BaseViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev210202.goingtohakwon.util.isNull

class DataViewModel : BaseViewModel() {

	val db = Firebase.firestore
	val storage = Firebase.storage
	lateinit var document: DocumentReference
	var hakwonName = ""
	var childName = ""
	fun checkHakwon(
		name: String,
		isSuccess: () -> Unit,
		isFail: (String) -> Unit
	) {
		document = db.collection("hakwon").document(name)
		document.get().addOnSuccessListener { result ->
			if (result.exists()) {
				hakwonName = name
				isSuccess()
			} else {
				isFail("일치하는 학원이 없습니다. 다시 시도해주세요.")
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}

	}

	fun checkPassword(password: String, isSuccess: () -> Unit, isFail: (String) -> Unit) {
		document.get().addOnSuccessListener { result ->
			if (result.exists()) {
				val hakwonPassword = result.data?.get("password").toString()
				if (hakwonPassword == password) {
					isSuccess()
				} else {
					isFail("비밀번호가 다릅니다.")
				}
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}

	fun checkName(name: String, isSuccess: () -> Unit, isFail: (String) -> Unit) {
		document.collection("학원생").document(name).get().addOnSuccessListener { result ->
			if (result.exists()) {
				childName = name
				isSuccess()
			} else {
				isFail("일치하는 이름이 없습니다. 다시 시도해주세요.")
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}
}