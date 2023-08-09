package page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bean.FileBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tool.AdbTool
import tool.runExecAndAdbToBuffer

@Composable
fun FileManager(deviceId: String) {

    var foldName by remember { mutableStateOf("/sdcard/") }
    val fileList = remember { mutableStateListOf<FileBean>() }

    // 获取本机文件及文件夹
    LaunchedEffect(foldName) {
        withContext(Dispatchers.IO) {
            AdbTool.fileList(deviceId, foldName).runExecAndAdbToBuffer().use {
                while (!it.readUtf8Line().isNullOrBlank()) {
                    val line = it.readUtf8Line() ?: continue
                    val hasFold = line.endsWith("/")
                    withContext(Dispatchers.Main) {
                        fileList.add(
                            FileBean(
                                if (hasFold) line.removeRange(line.length - 1, line.length) else line,
                                hasFold
                            )
                        )
                    }
                }
            }
        }
    }
    LazyColumn {
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        if (foldName != "/sdcard/") {
                            // 查找最后一个以斜杠结尾的内容的位置
                            val split = foldName.splitToSequence('/')
                                .filter { it.isNotBlank() }
                                .toMutableList()
                            split.removeLast()

                            foldName = "/${split.joinToString("/")}/"
                        }
                    }.padding(12.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(foldName != "/sdcard/") {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
                Spacer(modifier = Modifier.width(18.dp))
                Text(foldName, fontSize = 16.sp)
            }
        }
        items(fileList.size) {
            val item = fileList[it]
            Row(
                modifier = Modifier
                    .clickable {
                        if (item.fold) {
                            // 去当前文件夹
                            fileList.clear()
                            foldName = "$foldName${item.name}/"
                        }
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(if (item.fold) "ic_folder.svg" else "ic_log.svg"),
                    "",
                    modifier = Modifier.width(24.dp).height(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(item.name, fontSize = 16.sp)
            }
        }
    }
}