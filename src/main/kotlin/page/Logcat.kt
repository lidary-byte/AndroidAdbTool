package page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import bean.FileBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import res.whiteColor
import tool.AdbTool
import tool.runExecAndAdbToBuffer
import kotlin.math.log

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Logcat(deviceId: String) {
    var filter by remember { mutableStateOf("*:A") }
    val logList = remember { mutableStateListOf("") }
    val (isPopupVisible, setIsPopupVisible) = remember { mutableStateOf(false) }
    var clickCoordinates by remember { mutableStateOf(IntSize(0, 0)) }
    // 箭头旋转动画
    val arrowAnim by animateFloatAsState(if (isPopupVisible) -180f else 0f)

    LaunchedEffect(filter) {
        withContext(Dispatchers.IO) {
            AdbTool.log(deviceId, filter).runExecAndAdbToBuffer().use {
                while (true) {
                    val line = it.readUtf8Line() ?: continue
                    println("=====================$line")
                    withContext(Dispatchers.Main) {
                        logList.add(0,line)
                    }
                }
            }
        }
    }

    LazyColumn {
        stickyHeader {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)) {
                TextButton(
                    {
                        setIsPopupVisible(!isPopupVisible)
                    },
                    modifier = Modifier
                        .onSizeChanged {
                            clickCoordinates = it
                        },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Logcat Level", color = Color.Black)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier.graphicsLayer {
                            rotationX = arrowAnim
                        })
                }


                TextField(filter, onValueChange = {
                    filter = it
                })

                if (isPopupVisible) {
                    LevelWidget(clickCoordinates.width, clickCoordinates.height) {
                        filter = it
                    }
                }
            }
        }
        items(logList.size) {
            val item = logList[it]
            Text(item)
        }
    }
}

@Composable
fun LevelWidget(x: Int, y: Int, selectLevel: (String) -> Unit) {
    Popup(offset = IntOffset(0, y)) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .shadow(0.dp, RoundedCornerShape(10.dp))
                .background(Color.Red)
        ) {
            arrayOf("A", "D", "E", "I", "V", "W").forEach {
                Text(it, fontSize = 16.sp, modifier = Modifier
                    .clickable {
                        selectLevel.invoke("*:$it")
                    }
                    .padding(vertical = 6.dp, horizontal = 4.dp))
            }
        }
    }
}