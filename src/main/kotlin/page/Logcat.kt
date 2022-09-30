package page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import res.whiteColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Logcat() {
    var filter by remember { mutableStateOf("") }
    LazyColumn() {
        stickyHeader {
            Row {
                Text("筛选:")
                TextField(filter, onValueChange = {
                    filter = it
                })
                Text("级别:")
            }
        }
        item {

        }
    }
}