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
            val priority = if (text.contains("acil", ignoreCase = true) || text.contains("kritik", ignoreCase = true)) 50 else 0
            val recommendation = when {
                priority >= 50 -> "ACİL MÜDAHALE GEREKLİ: Sistemi kontrol edin."
                text.contains("pil", ignoreCase = true) || text.contains("batarya", ignoreCase = true) -> "Enerji tasarrufu modunu değerlendirin."
                text.contains("güvenlik", ignoreCase = true) || text.contains("şifre", ignoreCase = true) -> "Güvenlik protokollerini gözden geçirin."
                else -> "Otonom analiz: Normal aktivite."
            }
            val event = EventNode(
                packageName = packageName,
                title = title,
                content = text,
                timestamp = System.currentTimeMillis(),
                priority = priority,
                recommendation = recommendation
            )
            db.eventDao().insert(event)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("OtonomAjan", "Bildirim kaldırıldı: ${sbn.packageName}")
    }
}
