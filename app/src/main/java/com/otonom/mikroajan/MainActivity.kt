package com.otonom.mikroajan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otonom.mikroajan.data.EventNode
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuraDashboard()
        }
    }
}

@Composable
fun AuraDashboard() {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val events by db.eventDao().getAllEvents().collectAsState(initial = emptyList())
    
    var isPermissionGranted by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isPermissionGranted = isNotificationServiceEnabled(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0208))
            .padding(16.dp)
    ) {
        Text(
            text = "--- OTONOM MİKRO-AJAN: CORE ONLINE ---",
            color = Color(0xFF00FF41),
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (!isPermissionGranted) {
            PermissionWarning {
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "NEURAL LOGS:",
            color = Color.Gray,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(events) { event ->
                LogEntry(event)
            }
        }
    }
}

@Composable
fun PermissionWarning(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF220000))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "UYARI: Bildirim erişimi kapalı. Ajanın çevreyi görebilmesi için izin verin.",
                color = Color.Red,
                fontSize = 12.sp
            )
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("İzin Ver", color = Color.White)
            }
        }
    }
}

@Composable
fun LogEntry(event: EventNode) {
    val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(event.timestamp))
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row {
            Text("[$time] ", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text("INGRESS: ${event.packageName}", color = Color(0xFF00FF41), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
        Text("DATA: ${event.title} - ${event.content}", color = if (event.priority >= 40) Color.Yellow else Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        event.recommendation?.let {
            Text("AI SUGGESTION: $it", color = Color(0xFF00FF41), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 2.dp))
        }
        Divider(color = Color(0xFF111111))
    }
}

fun isNotificationServiceEnabled(context: Context): Boolean {
    val pkgName = context.packageName
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat?.contains(pkgName) == true
}
