package dev210202.goingtohakwon.view

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev210202.goingtohakwon.AttendanceList
import dev210202.goingtohakwon.MutableListLiveData
import dev210202.goingtohakwon.base.BaseViewModel
import dev210202.goingtohakwon.model.Attendance
import dev210202.goingtohakwon.model.Hakwon
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.model.Student
import dev210202.goingtohakwon.utils.Message
import dev210202.goingtohakwon.utils.getToday

class DataViewModel : BaseViewModel() {

	private val store = Firebase.firestore
	private val storage = Firebase.storage
	private val database = Firebase.database.reference

	//
	private val document: DocumentReference
		get() {
			return store.collection("hakwon").document(getHakwonName())
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
	private var phone = ""
	private var hakwonPassWord = ""

	fun getHakwonName() = hakwonName
	fun setHakwonName(name: String) {
		hakwonName = name
	}

	fun getChildName() = childName
	fun setChildName(name: String) {
		childName = name
	}

	fun getPhone() = phone
	fun setPhone(phoneNumber: String) {
		this.phone = phoneNumber
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
		isFail: (String) -> Unit
	) {
		setHakwonName(name)

		document.get().addOnSuccessListener { result ->
			if (result.exists()) {
				isSuccess()
			} else {
				//isFail("일치하는 학원이 없습니다. 다시 시도해주세요.")
			}
		}.addOnFailureListener {
			Log.e("exception", it.message.toString())
			//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")

		}

	}

	fun checkPassword(
		inputPassword: String,
		isSuccess: (String) -> Unit,
		isFail: (String) -> Unit
	) {
		setHakwonPassWord(inputPassword)
		document.get().addOnSuccessListener { result ->
			if (result.exists()) {
				val parentsPassWord = result.data?.get("password").toString()
				if (inputPassword == parentsPassWord) {
					//	isSuccess("일반")
				} else {
					val adminPassword = result.data?.get("adminpassword").toString()
					if (inputPassword == adminPassword) {
						//isSuccess("관리자")
					} else {
						//	isFail("비밀번호가 다릅니다.")
					}
				}
			}
		}.addOnFailureListener {
			//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}

	fun checkName(name: String, isSuccess: () -> Unit, isFail: (String) -> Unit) {
		setChildName(name)
		document.collection("학원생").document(name).get().addOnSuccessListener { result ->
			if (result.exists()) {
				isSuccess()
			} else {
				//isFail("일치하는 이름이 없습니다. 다시 시도해주세요.")
			}
		}.addOnFailureListener {
			//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}


	fun confirmAttendance(
		inputName: String,
		isSuccess: (String) -> Unit,
		isFail: (String) -> Unit
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
					//isSuccess("출석 되었습니다.")
				} else {
					document.collection("학원생").document(inputName).set(
						AttendanceList(
							attendance = listOf(getToday())
						)
					)
					//isSuccess("출석 되었습니다.")
				}
			}.addOnFailureListener {
				//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
			}
	}

	fun getAttendance(name: String, isFail: (String) -> Unit) {
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
					//isFail("일치하는 이름이 없습니다. 다시 시도해주세요.")
				}
			}.addOnFailureListener {
				//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
			}
	}


	fun getAttendanceStudents(
		today: String,
		isFail: (String) -> Unit
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
					//	isFail("출석한 학생이 없습니다.")
				}
			}.addOnFailureListener {
				//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
			}
	}

	fun addAttachments(isSuccess: () -> Unit, isFail: (Message) -> Unit) {

		_attachmentList.value!!.forEach { uri ->
			storage.reference.child("${getHakwonName()}/${uri.lastPathSegment}").putFile(uri)
				.addOnSuccessListener {
					isSuccess()
				}.addOnFailureListener {
					isFail(Message.NETWORK_ERROR)
				}
		}
	}

	fun downloadAttachment(
		uri: String,
		isSuccess: (uri: Uri) -> Unit,
		isFail: (String) -> Unit
	) {
		val pathString = "${getHakwonName()}/${uri}"
		Log.e("PATH ", pathString)
		storage.reference.child(pathString).downloadUrl.addOnCompleteListener { task ->
			if (task.isSuccessful) {
				isSuccess(task.result)
			}
		}.addOnFailureListener {
			//isFail("통신중에 오류가 발생했습니다. 다시 시도해주세요.")
		}
	}

	//
	fun login(
		hakwonName: String,
		childName: String,
		phone: String,
		isSuccess: () -> Unit,
		isFail: (Message) -> Unit
	) {
		checkExistStudent(
			hakwonName = hakwonName,
			studentName = childName,
			phone = phone,
			result = { message ->
				when (message) {
					Message.NOT_REGIST_STUDENT -> {
						isFail(message)
					}
					Message.REGIST_STUDENT -> {
						isSuccess()
					}
					Message.NETWORK_ERROR -> {
						isFail(message)
					}
					else -> {

					}
				}
			}
		)
	}

	fun registChild(
		hakwonName: String,
		childName: String,
		phone: String,
		isSuccess: () -> Unit,
		isFail: (Message) -> Unit
	) {
		// TODO: 콜백으로 인한 가독성 문제 해결하기
		checkExistStudent(
			hakwonName = hakwonName,
			studentName = childName,
			phone = phone,
			result = { message ->
				when (message) {
					Message.NOT_REGIST_STUDENT -> {
						database.child(hakwonName).child("students").child(childName).setValue(
							Student(
								phone = phone
							)
						).addOnSuccessListener {
							isSuccess()
						}.addOnFailureListener {
							isFail(Message.NETWORK_ERROR)
						}
					}
					Message.REGIST_STUDENT -> {
						isFail(message)
					}
					Message.NETWORK_ERROR -> {
						isFail(message)
					}
					else -> {

					}
				}
			}
		)
	}

	fun createHakwon(hakwon: Hakwon, isSuccess: () -> Unit, isFail: (Message) -> Unit) {

		checkExistHakwon(hakwon, isSuccess = { message ->
			when (message) {
				Message.CAN_REGIST_HAKWON -> {
					database.child(hakwon.name).setValue(hakwon).addOnSuccessListener {
						isSuccess()
					}.addOnFailureListener {
						isFail(Message.NETWORK_ERROR)
					}
				}
				Message.REGIST_HAKWON -> {
					isFail(Message.REGIST_HAKWON)
				}
				else -> {}
			}
		}, isFail = {
			isFail(it)
		})

	}

	private fun checkExistHakwon(
		hakwon: Hakwon,
		isSuccess: (Message) -> Unit,
		isFail: (Message) -> Unit
	) {
		database.child(hakwon.name).get().addOnSuccessListener { dataSnapshot ->
			if (dataSnapshot.exists()) {
				isSuccess(Message.REGIST_HAKWON)
			} else {
				isSuccess(Message.CAN_REGIST_HAKWON)
			}
		}.addOnFailureListener {
			isFail(Message.NETWORK_ERROR)
		}
	}

	fun adminLogin(hakwon: Hakwon, isSuccess: () -> Unit, isFail: (Message) -> Unit) {
		database.child(hakwon.name).get().addOnSuccessListener { dataSnapshot ->
			if (dataSnapshot.exists()) {
				val password = dataSnapshot.child("password").value
				if (password == hakwon.password) {
					isSuccess()
				} else {
					isFail(Message.NOT_CORRECT_PW)
				}
			} else {
				isFail(Message.NOT_REGIST_HAKWON)
			}
		}.addOnFailureListener {
			isFail(Message.NETWORK_ERROR)
		}
	}

	fun checkAttendance(
		hakwonName: String,
		studentName: String,
		phone: String,
		date: String,
		time: String,
		state: String,
		isSuccess: () -> Unit,
		isFail: (Message) -> Unit
	) {
		checkExistStudent(
			hakwonName = hakwonName,
			studentName = studentName,
			phone = phone,
			result = { message ->
				when (message) {
					Message.REGIST_STUDENT, Message.NOT_REGIST_STUDENT -> {
						database.child(hakwonName).child("students").child(studentName)
							.child("attendance")
							.child(date).setValue(Attendance(time, state))
							.addOnSuccessListener {
								isSuccess()
							}.addOnFailureListener {
								isFail(Message.NETWORK_ERROR)
							}
					}
					Message.NETWORK_ERROR -> {
						isFail(Message.NETWORK_ERROR)
					}
					else -> {}
				}

			})

	}

	private fun checkExistStudent(
		hakwonName: String,
		studentName: String,
		phone: String,
		result: (Message) -> Unit
	) {
		database.child(hakwonName).child("students").child(studentName).child("phone").get()
			.addOnSuccessListener { dataSnapshot ->
				if (dataSnapshot.exists()) {
					val phoneNumber = dataSnapshot.value
					if (phone == phoneNumber) {
						result(Message.REGIST_STUDENT)
					} else {
						result(Message.NOT_REGIST_STUDENT)
					}
				} else {
					result(Message.NOT_REGIST_STUDENT)
				}
			}.addOnFailureListener {
				result(Message.NETWORK_ERROR)
			}
	}

	fun getAttendanceStudents(
		hakwonName: String
	) {
		database.child(hakwonName).child("students").get().addOnSuccessListener { dataSnapshot ->
			//Log.e("DS Value", dataSnapshot.value.toString())

			val value = dataSnapshot.value as ArrayList<*>
			value.forEach { student ->
				Log.e("student", student.toString())
			}
		}
	}

	fun registNotice(
		hakwonName: String,
		notice: Notice,
		isSuccess: (Message) -> Unit,
		isFail: (Message) -> Unit
	) {
		database.child(hakwonName).child("notices").push().setValue(notice).addOnSuccessListener {
			isSuccess(Message.REGIST_NOTICE)
		}.addOnFailureListener {
			isFail(Message.NETWORK_ERROR)
		}
	}

	fun getNotice(hakwonName: String, isFail: (Message) -> Unit) {
		database.child(hakwonName).child("notices").get().addOnSuccessListener { dataSnapshot ->
			val noticeList = mutableListOf<Notice>()
			dataSnapshot.children.forEach { data ->
				val notice = data.getValue(Notice::class.java)
				notice?.copy(uuid = data.key.toString())?.let { noticeList.add(it) }
			}
			_noticeList.value = noticeList
		}.addOnFailureListener {
			isFail(Message.NETWORK_ERROR)
		}
	}

	fun editNotice(
		hakwonName: String,
		notice: Notice,
		isSuccess: (Message) -> Unit,
		isFail: (Message) -> Unit
	) {
		database.child(hakwonName).child("notices").child(notice.uuid)
			.updateChildren(notice.toMap())
			.addOnSuccessListener {
				isSuccess(Message.EDIT_NOTICE)
			}.addOnFailureListener {
				isFail(Message.NETWORK_ERROR)
			}

	}

	fun deleteNotice(
		hakwonName: String,
		notice: Notice,
		isSuccess: (Message) -> Unit,
		isFail: (Message) -> Unit
	) {
		database.child(hakwonName).child("notices").child(notice.uuid).removeValue()
			.addOnSuccessListener {
				_noticeList.remove(notice)
				isSuccess(Message.REMOVE_NOTICE)
			}.addOnFailureListener {
				isFail(Message.NETWORK_ERROR)
			}
	}
}