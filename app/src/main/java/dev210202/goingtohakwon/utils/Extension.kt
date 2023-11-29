package dev210202.goingtohakwon.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*

fun Activity.showToast(message: String) = Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT)
	.show()
fun Fragment.showToast(message: String) =
	Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
		.show()

fun Date.convertToFormat(): String = SimpleDateFormat("yyyy-MM-dd").format(this)

fun Date.convertToTime() : String = SimpleDateFormat("HH:mm").format(this)
fun String.getDay(): Int = this.split("-")[2].toInt()
fun String.getMonth(): Int = this.split("-")[1].toInt()
fun String.getYear(): Int = this.split("-")[0].toInt()
inline fun <reified T> DocumentSnapshot.toObjectNonNull(): T = toObject(T::class.java)!!

val QuerySnapshot.isNotEmpty get() = !this.isEmpty
