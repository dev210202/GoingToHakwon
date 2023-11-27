package dev210202.goingtohakwon.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(message: String) =
	Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
		.show()

fun Date.convertToFormat(): String = SimpleDateFormat("yyyy-MM-dd").format(this)
fun String.getDay(): Int = this.split("-")[2].toInt()
fun String.getMonth(): Int = this.split("-")[1].toInt()
fun String.getYear(): Int = this.split("-")[0].toInt()
inline fun <reified T> DocumentSnapshot.toObjectNonNull(): T = toObject(T::class.java)!!

val QuerySnapshot.isNotEmpty get() = !this.isEmpty


