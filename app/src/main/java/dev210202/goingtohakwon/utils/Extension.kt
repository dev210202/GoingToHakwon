package dev210202.goingtohakwon.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun Activity.showSnackBar(message: String) =
	Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT)
		.show()

fun Fragment.showSnackBar(message: String) =
	this.view?.let {
		Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
			.show()
		val imm: InputMethodManager =
			activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(view!!.windowToken, 0)
	}

fun Fragment.showSnackBarWithAction(message: String, actionMessage: String, action: () -> Unit) =
	this.view?.let {
		Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
			.setAction(actionMessage) {
				action
			}
			.show()
		val imm: InputMethodManager =
			activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(view!!.windowToken, 0)
	}

fun Date.convertToFormat(): String = SimpleDateFormat("yyyy-MM-dd").format(this)

fun Date.convertToTime(): String = SimpleDateFormat("HH:mm").format(this)

fun String.getState(): String = this.split("-")[3]
fun String.getDay(): Int = this.split("-")[2].toInt()
fun String.getMonth(): Int = this.split("-")[1].toInt()
fun String.getYear(): Int = this.split("-")[0].toInt()

fun String?.isNotNull() = this != null

fun String?.isNull() = this == null

fun Uri.getFileName(context : Context): String{
	
	val cursor = context.contentResolver.query(this, null, null, null, null)
	val nameIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
	cursor.moveToFirst()
	return cursor.getString(nameIndex)
}