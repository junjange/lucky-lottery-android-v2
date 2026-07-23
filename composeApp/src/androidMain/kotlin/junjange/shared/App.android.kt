package junjange.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
actual fun SharedAppContent() {
    // Android uses its own Activity-based navigation
    // This is only used when shared module is run directly
    Text("Android uses native navigation")
}
