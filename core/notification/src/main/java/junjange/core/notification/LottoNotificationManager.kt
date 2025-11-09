package junjange.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import junjange.feature.main.MainActivity

class LottoNotificationManager(
    private val context: Context,
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    LOTTO_CHANNEL_ID,
                    context.getString(R.string.lotto_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = context.getString(R.string.lotto_notification_channel_description)
                    lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendLottoNotification() {
        val intent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("notification_type", "lotto")
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                LOTTO_NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(context.resources, junjange.feature.main.R.drawable.app_icon)

        val notification =
            NotificationCompat
                .Builder(context, LOTTO_CHANNEL_ID)
                .setSmallIcon(junjange.feature.main.R.drawable.app_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(context.getString(R.string.lotto_notification_content_title))
                .setContentText(context.getString(R.string.lotto_notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

        notificationManager.notify(LOTTO_NOTIFICATION_ID, notification)
    }

    fun sendPensionLottoNotification() {
        val intent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("notification_type", "pension_lotto")
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                PENSION_LOTTO_NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(context.resources, junjange.feature.main.R.drawable.app_icon)

        val notification =
            NotificationCompat
                .Builder(context, LOTTO_CHANNEL_ID)
                .setSmallIcon(junjange.feature.main.R.drawable.app_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(context.getString(R.string.pension_lotto_notification_content_title))
                .setContentText(context.getString(R.string.pension_lotto_notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

        notificationManager.notify(PENSION_LOTTO_NOTIFICATION_ID, notification)
    }

    companion object {
        private const val LOTTO_CHANNEL_ID = "lotto_channel"
        private const val LOTTO_NOTIFICATION_ID = 1001
        private const val PENSION_LOTTO_NOTIFICATION_ID = 1002
    }
}
