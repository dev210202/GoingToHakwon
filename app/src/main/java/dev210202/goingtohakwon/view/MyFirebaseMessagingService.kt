package dev210202.goingtohakwon.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev210202.goingtohakwon.R
import dev210202.goingtohakwon.view.login.LoginActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
	@RequiresApi(Build.VERSION_CODES.S)
	override fun onMessageReceived(p0: RemoteMessage) {
		super.onMessageReceived(p0)

		//  포그라운드 실행
		showNotification(p0)
		Log.e("onMessageReceived", p0.toString())
	}

	override fun onMessageSent(p0: String) {
		super.onMessageSent(p0)
		Log.e("onMessageSent", p0)
	}

	override fun onNewToken(p0: String) {
		super.onNewToken(p0)
		Log.e("onNewToken", p0)
	}

	@RequiresApi(Build.VERSION_CODES.S)
	private fun showNotification(remoteMessage: RemoteMessage) {

		val intent = Intent(this, LoginActivity::class.java)
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		val pendingIntent = PendingIntent.getActivity(
			this, 0 /* Request code */, intent,
			PendingIntent.FLAG_IMMUTABLE
		)
		var title = remoteMessage.notification?.title!!
		var content = remoteMessage.notification?.body!!
		var expandContent = ""

		if (title.contains("안내문 알림")) {
			expandContent = title.split("알림")[1] + "\n" + content
			content = title.split("알림")[1]
			title = title.split("알림")[0] + "알림"
		}


		val channelId = "channel"
		val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val notificationBuilder = NotificationCompat.Builder(this, channelId)
			.setSmallIcon(R.drawable.ic_noti_icon)
			.setContentTitle(title)
			.setContentText(content)
			.setAutoCancel(true)
			.setSound(defaultSoundUri)
			.setContentIntent(pendingIntent)
			.setStyle(NotificationCompat.BigTextStyle().bigText(expandContent))


		val notificationManager =
			getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId,
				"Channel human readable title",
				NotificationManager.IMPORTANCE_DEFAULT
			)
			notificationManager.createNotificationChannel(channel)
		}

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

	}

}