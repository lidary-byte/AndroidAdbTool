package widget

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RunningIndicator() {
    CircularProgressIndicator(
        color = MaterialTheme.colors.onPrimary,
        strokeWidth = 2.dp,
        modifier = Modifier.size(20.dp)
    )
}