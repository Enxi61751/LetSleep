package com.letsleep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.letsleep.app.ui.LetSleepApp
import com.letsleep.app.ui.theme.LetSleepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LetSleepTheme {
                LetSleepApp()
            }
        }
    }
}
