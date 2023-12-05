package dev210202.goingtohakwon.view

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev210202.goingtohakwon.model.MutableListLiveData
import dev210202.goingtohakwon.base.BaseViewModel
import dev210202.goingtohakwon.model.Attendance
import dev210202.goingtohakwon.model.Hakwon
import dev210202.goingtohakwon.model.Notice
import dev210202.goingtohakwon.model.Student
import dev210202.goingtohakwon.utils.Message

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

	private var _attendanceList = MutableListLiveData<Attendance>()
	val attendanceList: LiveData<List<Attendance>> get() = _attendanceList

	private var _attachmentList = MutableListLiveData<Uri>()
	val attachmentList: LiveData<List<Uri>> get() = _attachmentList

	private var _attendanceStudentList = MutableListLiveData<Student>()
	val attendanceStudentList: LiveData<List<Student>> get() = _attendanceStudentList

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

	fun checkExistAttachment(uri : Uri, isSuccess: () -> Unit, isFail: (String) -> Unit){
		val attachmentUri =_attachmentList.value?.find { it == uri }
		if(attachmentUri == null){
			isSuccess()
		}else{
			isFail("같은 이름의 첨부파일이 존재합니다. 다른 이름으로 추가해주세요.")
		}
	}
	fun resetAttachmentList() {
		_attachmentList.value = listOf()
	}


	fun removeAttach(attachment: String) {
		val attachmentUri = _attachmentList.value?.find { uri -> uri.lastPathSegment == attachment }
		attachmentUri?.let {
			_attachmentList.remove(it)
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
		hakwonName: String,
		uri: String,
		isSuccess: (uri: Uri) -> Unit,
		isFail: (Message) -> Unit
	) {
		val pathString = "${hakwonName}/${uri}"
		storage.reference.child(pathString).downloadUrl.addOnCompleteListener { task ->
			if (task.isSuccessful) {
				isSuccess(task.result)
			}
		}.addOnFailureListener {
			isFail(Message.NETWORK_ERROR)
		}
	}


	fun deleteAttachments(
		hakwonName: String,
		uriList: List<String>,
		isSuccess: () -> Unit,
		isFail: (Message) -> Unit
	) {
		uriList.forEachIndexed { index, uri ->
			deleteAttachment(hakwonName, uri, isFail = {
				isFail(it)
				return@deleteAttachment
			})
			if (index == uriList.lastIndex) {
				isSuccess()
			}
		}

	}

	fun deleteAttachment(
		hakwonName: String,
		uri: String,
		isFail: (Message) -> Unit
	) {
		val pathString = "${hakwonName}/${uri}"
		storage.reference.child(pathString).delete().addOnSuccessListener {
		}.addOnFailureListener {
			isFail(Message.NETWORK_ERROR)
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
						database.child(hakwonName).child("students").child(childName + phone)
							.setValue(
								//						database.child(hakwonName).child("students").push().setValue(
								Student(
									name = childName,
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

		checkExistHakwon(hakwon.name, result = { message ->
			when (message) {
				Message.NOT_REGIST_HAKWON -> {
					database.child(hakwon.name).setValue(hakwon).addOnSuccessListener {
						isSuccess()
					}.addOnFailureListener {
						isFail(Message.NETWORK_ERROR)
					}
				}
				Message.REGIST_HAKWON -> {
					isFail(Message.REGIST_HAKWON)
				}
				Message.NETWORK_ERROR -> {
					isFail(Message.NETWORK_ERROR)
				}
				else -> {}
			}
		})

	}

	fun checkExistHakwon(
		hakwonName: String,
		result: (Message) -> Unit,
	) {
		database.child(hakwonName).get().addOnSuccessListener { dataSnapshot ->
			if (dataSnapshot.exists()) {
				result(Message.REGIST_HAKWON)
			} else {
				result(Message.NOT_REGIST_HAKWON)
			}
		}.addOnFailureListener {
			result(Message.NETWORK_ERROR)
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
					Message.REGIST_STUDENT -> {
						database.child(hakwonName).child("students").child(studentName + phone)
							.child("attendance")
							.child(date).setValue(Attendance(date, time, state))
							.addOnSuccessListener {
								isSuccess()
							}.addOnFailureListener {
								isFail(Message.NETWORK_ERROR)
							}
					}
					Message.NETWORK_ERROR -> {
						isFail(Message.NETWORK_ERROR)
					}
					Message.NOT_REGIST_STUDENT -> {
						isFail(Message.NOT_REGIST_STUDENT)
					}
					else -> {}
				}

			})

	}

	/*
	 *
	 *
			
	-- Student --

	 *
	 *
	 */

	private fun checkExistStudent(
		hakwonName: String,
		studentName: String,
		phone: String,
		result: (Message) -> Unit
	) {
		database.child(hakwonName).child("students").child(studentName + phone).child("phone").get()
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

	fun getAttendancesOnName(
		studentName: String,
		hakwonName: String,
	) {
		database.child(hakwonName).child("students").child(studentName).child("attendance").get()
			.addOnSuccessListener { dataSnapshot ->
				Log.e("datasnapshot", dataSnapshot.toString())
				if (dataSnapshot.exists()) {
					dataSnapshot.children.forEach { data ->
						var attendance = Attendance()
						data.children.forEach { children ->
							when (children.key) {
								"date" -> {
									attendance = attendance.copy(date = children.value.toString())
								}
								"state" -> {
									attendance = attendance.copy(state = children.value.toString())
								}
							}
						}
						Log.e("data", attendance.toString())

						_attendanceList.add(attendance)
					}

				}
				//val student = dataSnapshot.getValue(Student::class.java)
				//Log.e("student", student.toString())

			}.addOnFailureListener {

			}
	}

	fun getAttendanceStudentsOnDate(
		date: String,
		hakwonName: String
	) {
		database.child(hakwonName).child("students").get().addOnSuccessListener { dataSnapshot ->
			val studentList = getStudentListFromDataSnapshot(dataSnapshot)
			val filtList = mutableListOf<Student>()
			studentList.forEach { student ->
				student.attendance.forEach { attendance ->
					if (attendance.date == date) {
						filtList.add(student)
					}
				}
			}
			_attendanceStudentList.value = filtList
		}
	}

	private fun getStudentListFromDataSnapshot(dataSnapshot: DataSnapshot): List<Student> {
		val studentList = mutableListOf<Student>()
		dataSnapshot.children.forEach { data ->
			//Log.e("data", data.toString())

			var student = Student()
			val attendanceList = mutableListOf<Attendance>()
			data.children.forEach { children ->
				when (children.key) {
					"name" -> {
						student = student.copy(name = children.value!!.toString())
					}
					"phone" -> {
						student = student.copy(phone = children.value!!.toString())
					}
					"attendance" -> {
						children.children.forEach { attendanceData ->
							attendanceData.getValue(Attendance::class.java)
								?.let { attendanceList.add(it) }
						}
						student = student.copy(attendance = attendanceList)
					}
				}
				//Log.e("key", children.key.toString())
				//Log.e("CHILD", children.toString())
			}
			//Log.e("STU", student.toString())
			studentList.add(student)
		}
		return studentList
	}

	/*
	
	-- Notice --
	
	 */
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