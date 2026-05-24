package com.otonom.mikroajan.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class AgentNotificationService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val bundle = sbn.notification.extras
        val title = bundle.getString("android.title") ?: "No Title"
        val text = bundle.getCharSequence("android.text")?.toString() ?: "No Text"

        Log.d("OtonomAjan", "Bildirim: [$packageName] $title: $text")
        // TODO: Room DB integration will be added here
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("OtonomAjan", "Bildirim kaldırıldı: ${sbn.packageName}")
    }
}
