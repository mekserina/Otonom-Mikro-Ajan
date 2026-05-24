package com.otonom.mikroajan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "SYSTEM STATUS: MONITORING...",
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}
