package dev210202.goingtohakwon.view

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.dutch2019.base.BaseViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev210202.goingtohakwon.AttendanceList
import dev210202.goingtohakwon.MutableListLiveData
import dev210202.goingtohakwon.Notice
import dev210202.goingtohakwon.utils.getToday
import dev210202.goingtohakwon.utils.toObjectNonNull

class DataViewModel : BaseViewModel() {

	private val db = Firebase.firestore
	private val storage = Firebase.storage
	//	private lateinit var document: DocumentReference
	//
	//	private val doc :DocumentReference get() {
	//		return db.collection("hakwon").document(getHakwonName())
	//	}

	private val document: DocumentReference
		get() {
			return db.collection("hakwon").document(getHakwonName())
		}

	private var _noticeList = MutableListLiveData<Notice>()
	val noticeList: LiveData<List<Notice>> get() = _noticeList

	private var _attendanceList = MutableListLiveData<String>()
	val attendanceList: LiveData<List<String>> get() = _attendanceList

	private var _attachmentList = MutableListLiveData<Uri>()
	val attachmentList: LiveData<List<Uri>> get() = _attachmentList

	private var _attendanceStudentList = MutableListLiveData<String>()
	val attendanceStudentList: LiveData<List<String>> get() = _attendanceStudentList

	private lateinit var documentList: QuerySnapshot

	private var hakwonName = ""
	private var childName = ""
	private var hakwonPassWord = ""

	fun getHakwonName() = hakwonName
	fun setHakwonName(name: String) {
		hakwonName = name
	}

	fun getChildName() = childName
	fun setChildName(name: String) {
		childName = name
	}

	fun getHakwonPassWord() = hakwonPassWord
	fun setHakwonPassWord(password: String) {
		hakwonPassWord = password
	}

	fun getNoticeList() = _noticeList.value!!

	fun addAttachmentList(uri: Uri) {
		_attachmentList.add(uri)
	}

	fun getAttendanceList() = _attendanceList.value!!
	fun getAttachmentList(): List<String> {
		return _attachmentList.value!!.map { it.lastPathSegment.toString() }
	}

	fun checkHakwon(
		name: String,
		isSuccess: () -> Unit,
		isFail: (message: String) -> Unit
	) {
		setHakwonName(name)

		document.get().addOnSuccessListener { result ->
			if (result.exists()) {
				isSuccess()
			} else {
				isFail("일치하는 학원이 없습니다. 다시 시도해주세요.")
			}
		}.addOnFailureListener {
			Log.e("exception", it.message.toString())
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")

		}

	}

	fun checkPassword(
		inputPassword: String,
		isSuccess: (message: String) -> Unit,
		isFail: (message: String) -> Unit
	) {
		setHakwonPassWord(inputPassword)
		document.get().addOnSuccessListener { result ->
			if (result.exists()) {
				val parentsPassWord = result.data?.get("password").toString()
				if (inputPassword == parentsPassWord) {
					isSuccess("일반")
				} else {
					val adminPassword = result.data?.get("adminpassword").toString()
					if (inputPassword == adminPassword) {
						isSuccess("관리자")
					} else {
						isFail("비밀번호가 다릅니다.")
					}
				}
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}

	fun checkName(name: String, isSuccess: () -> Unit, isFail: (message: String) -> Unit) {
		setChildName(name)
		document.collection("학원생").document(name).get().addOnSuccessListener { result ->
			if (result.exists()) {
				isSuccess()
			} else {
				isFail("일치하는 이름이 없습니다. 다시 시도해주세요.")
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}


	fun getNotice(isFail: (message: String) -> Unit) {

		document.collection("안내문").get().addOnSuccessListener { documents ->
			if (!documents.isEmpty) {
				val noticeList = mutableListOf<Notice>()
				documentList = documents
				for (document in documents) {
					noticeList.add(document.toObjectNonNull())
				}
				_noticeList.value = noticeList
			} else {
				isFail("불러올 수 있는 공지사항이 없습니다.")
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}

	fun addNotice(notice: Notice, isSuccess: () -> Unit, isFail: (message: String) -> Unit) {
		document.collection("안내문").add(notice).addOnSuccessListener {
			isSuccess()
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}.addOnCompleteListener {
			if (it.isSuccessful) {
				isSuccess()
			}
		}
	}

	fun deleteNotice(
		position: Int,
		isSuccess: (message: String) -> Unit,
		isFail: (message: String) -> Unit
	) {
		val doc = documentList.documents[position].reference
		doc.delete().addOnSuccessListener {
			_noticeList.remove(_noticeList.get(position))
			isSuccess("삭제 되었습니다.")
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}


	fun editNotice(
		notice: Notice,
		position: Int,
		isSuccess: (message: String) -> Unit,
		isFail: (message: String) -> Unit
	) {
		val doc = documentList.documents[position].reference
		doc.run {
			update("title", notice.title)
			update("content", notice.content)
			update("date", notice.date)
			update("attachment", notice.attachment)
		}.addOnSuccessListener {
			isSuccess("수정 되었습니다.")
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}

	fun confirmAttendance(
		inputName: String,
		isSuccess: (message: String) -> Unit,
		isFail: (message: String) -> Unit
	) {
		// 출석 리스트를 가져와야함
		document.collection("학원생").document(inputName).get()
			.addOnSuccessListener { documentSnapshot ->
				if (documentSnapshot.exists()) {
					document.collection("학원생").document(inputName).update(
						"attendance", FieldValue.arrayUnion(
							getToday()
						)
					)
					isSuccess("출석 되었습니다.")
				} else {
					document.collection("학원생").document(inputName).set(
						AttendanceList(
							attendance = listOf(getToday())
						)
					)
					isSuccess("출석 되었습니다.")
				}
			}.addOnFailureListener {
				isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
			}
	}

	fun getAttendance(name: String, isFail: (message: String) -> Unit) {
		document.collection("학원생").document(name).get()
			.addOnSuccessListener { documentSnapshot ->
				if (documentSnapshot.exists()) {
					documentSnapshot.data?.get("attendance")?.let {
						_attendanceList.value = mutableListOf<String>().apply {
							(it as List<String>).forEach { date ->
								add(date)
							}
						}
					}
				} else {
					_attendanceList.value = listOf()
					isFail("일치하는 이름이 없습니다. 다시 시도해주세요.")
				}
			}.addOnFailureListener {
				isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
			}
	}


	fun getAttendanceStudents(
		today: String,
		isFail: (message: String) -> Unit
	) {
		document.collection("학원생").whereArrayContains("attendance", today).get()
			.addOnSuccessListener { documents ->
				if (!documents.isEmpty) {
					mutableListOf<String>().apply {
						for (document in documents) {
							add(document.id)
						}
					}.run {
						_attendanceStudentList.value = this
					}
				} else {
					_attendanceStudentList.value = listOf()
					isFail("출석한 학생이 없습니다.")
				}
			}.addOnFailureListener {
				isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
			}
	}

	fun addAttachments(isSuccess: () -> Unit, isFail: (message: String) -> Unit) {

		_attachmentList.value!!.forEach { uri ->
			storage.reference.child("${getHakwonName()}/${uri.lastPathSegment}").putFile(uri)
				.addOnSuccessListener {
					isSuccess()
				}.addOnFailureListener {
					isFail(it.message.toString())
				}
		}
	}

	fun downloadAttachment(
		uri: String,
		isSuccess: (uri: Uri) -> Unit,
		isFail: (message: String) -> Unit
	) {
		val pathString = "${getHakwonName()}/${uri}"
		Log.e("PATH ", pathString)
		storage.reference.child(pathString).downloadUrl.addOnCompleteListener { task ->
			if (task.isSuccessful) {
				isSuccess(task.result)
			}
		}.addOnFailureListener {
			isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}


}