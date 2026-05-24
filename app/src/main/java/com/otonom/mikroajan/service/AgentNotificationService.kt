package com.otonom.mikroajan.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.otonom.mikroajan.DatabaseProvider
import com.otonom.mikroajan.data.EventNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgentNotificationService : NotificationListenerService() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val bundle = sbn.notification.extras
        val title = bundle.getString("android.title") ?: "No Title"
        val text = bundle.getCharSequence("android.text")?.toString() ?: "No Text"

        Log.d("OtonomAjan", "Bildirim Yakalandı: [$packageName] $title: $text")

        scope.launch {
            val db = DatabaseProvider.getDatabase(applicationContext)
            val event = EventNode(
                packageName = packageName,
                title = title,
                content = text,
                timestamp = System.currentTimeMillis(),
                priority = if (text.contains("acil", ignoreCase = true) || text.contains("kritik", ignoreCase = true)) 50 else 0
            )
            db.eventDao().insert(event)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("OtonomAjan", "Bildirim kaldırıldı: ${sbn.packageName}")
    }
}
