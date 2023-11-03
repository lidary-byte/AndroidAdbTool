package screen.quick

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import res.randomColor
import tool.AdbTool
import tool.runExec
import tool.ttfFontFamily
import kotlin.random.Random
import kotlin.random.nextInt

data class QuickScreen(val device: String) : Screen {

    @Composable
    override fun Content() {
        val quickModel = rememberScreenModel { QuickScreenModel() }
        val quickData = quickModel.quickData.collectAsState()


        var dialogTitle by remember { mutableStateOf("") }
        var dialogContent by remember { mutableStateOf("") }


        if (dialogContent.isNotEmpty()) {
            MessageDialog(dialogTitle, dialogContent) {
                dialogTitle = ""
                dialogContent = ""
            }
        }

//        Column {
//            quickData.value.forEach {
//                Spacer(modifier = Modifier.height(166.dp))
//                Surface(
//                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().background(Color.White),
//                    shape = RoundedCornerShape(6.dp),
//                    border = BorderStroke(0.5.dp, Color(220, 220, 220)),  // 边框
//                ) {
//                    Column(
//                        modifier = Modifier.padding(12.dp)
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Spacer(
//                                modifier = Modifier.width(4.dp).height(16.dp).clip(RoundedCornerShape(8.dp))
//                                    .background(Color(146, 106, 255))
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(it.title)
//                        }
//                        Spacer(modifier = Modifier.height(8.dp))
//                        LazyVerticalGrid(
//                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//                            // 固定两列
//                            columns = GridCells.Fixed(4),
//                            content = {
//                                item(span = {
//                                    GridItemSpan(4)
//                                }) {
//                                    Text("哈哈哈哈哈哈哈哈哈哈哈哈")
//                                }
//                                items(it.child) {
//                                    Text(it.title)
//                                }
//                            }
//                        )
//                    }
//                }
//
//
//            }

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp).background(Color.White),
            // 固定两列
            columns = GridCells.Fixed(4),

            content = {
                quickData.value.forEach {
                    item(span = {
                        GridItemSpan(4)
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(it.title)
                        }
                    }
                    items(it.child) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(
                                    randomColor[Random.nextInt(
                                        IntRange(
                                            0, randomColor.size - 1
                                        )
                                    )]
                                ),
                            ) {
                                Text(
                                    text = "${Char(it.icon)}",
                                    fontFamily = ttfFontFamily(),
                                    fontSize = 28.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it.title, fontSize = 12.sp)
                        }
                    }
                    item(span = {
                        GridItemSpan(4)
                    }) {
                        Spacer(modifier = Modifier.height(18 .dp).background(Color(240, 240, 240)))
                    }
                }


            }
        )
//            items(quickData.value) {
//                Text(it.title)
//                LazyVerticalGrid(
//                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//                    // 固定两列
//                    columns = GridCells.Fixed(4),
//                    content = {
//                        this@LazyColumn.items(it.child) {
//                            Text(it.title)
//                        }
//                    }
//                )
//            }
//            item {
//                quickData.value.forEach {
//                    Text(it.title)
//                    LazyHorizontalGrid(GridCells.Fixed(2)){
//                        item {
//                            it.child.forEach {
//                                Text(it.title)
//                            }
//                        }
//                    }
//                }
//
//            }
    }
//        LazyColumn(
//            modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp, vertical = 12.dp).fillMaxWidth(),
//        ) {
//            // 前后加个间距
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//                CommonFunction(device) { title, content ->
//                    dialogTitle = title
//                    dialogContent = content
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                AboutApp()
//                Spacer(modifier = Modifier.height(16.dp))
//                AboutSystem()
//                Spacer(modifier = Modifier.height(16.dp))
//                AboutKeyBoard(device)
//                Spacer(modifier = Modifier.height(16.dp))
//                ScreenInput()
//                Spacer(modifier = Modifier.height(16.dp))
//
//            }
////
//            item {
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }


    /**
     * 屏幕输入
     */
    @Composable
    private fun ScreenInput() {
        BaseQuick("屏幕输入", color = Color(146, 106, 255)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe795, "向上滑动", modifier = Modifier.weight(1f))
                QuickItem(0xe603, "向下滑动", modifier = Modifier.weight(1f))
                QuickItem(0xe60a, "向左滑动", modifier = Modifier.weight(1f))
                QuickItem(0xe6ca, "向右滑动", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe697, "屏幕点击", modifier = Modifier.weight(1f))
                QuickItem(modifier = Modifier.weight(1f))
                QuickItem(modifier = Modifier.weight(1f))
                QuickItem(modifier = Modifier.weight(1f))
            }
        }
    }

    /**
     * 按键相关
     */
    @Composable
    private fun AboutKeyBoard(deviceId: String) {
        BaseQuick("按键相关", color = Color(158, 176, 184)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe68e, "Home键", modifier = Modifier.weight(1f).clickable { AdbTool.pressHome(deviceId) })
                QuickItem(0xe616, "Back键", modifier = Modifier.weight(1f).clickable { AdbTool.pressBack(deviceId) })
                QuickItem(0xe605, "Menu键", modifier = Modifier.weight(1f).clickable { AdbTool.pressMenu(deviceId) })
                QuickItem(0xe615, "Power键", modifier = Modifier.weight(1f).clickable { AdbTool.pressPower(deviceId) })
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(
                    0xe76e,
                    "增加音量",
                    modifier = Modifier.weight(1f).clickable { AdbTool.pressVolumeUp(deviceId) })
                QuickItem(
                    0xe771,
                    "降低音量",
                    modifier = Modifier.weight(1f).clickable { AdbTool.pressVolumeDown(deviceId) })
                QuickItem(
                    0xe612,
                    "静音",
                    modifier = Modifier.weight(1f).clickable { AdbTool.pressVolumeMute(deviceId) })
                QuickItem(
                    0xe658,
                    "切换应用",
                    modifier = Modifier.weight(1f).clickable { AdbTool.pressSwitchApp(deviceId) })
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe796, "遥控器", modifier = Modifier.weight(1f))
                QuickItem(modifier = Modifier.weight(1f))
                QuickItem(modifier = Modifier.weight(1f))
                QuickItem(modifier = Modifier.weight(1f))
            }
        }
    }

    /**
     * 系统相关
     */
    @Composable
    private fun AboutSystem() {
        BaseQuick("系统相关", color = Color(255, 193, 7)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe695, "开始录屏", modifier = Modifier.weight(1f))
                QuickItem(0xe71d, "结束录屏保存到电脑", modifier = Modifier.weight(1f))
                QuickItem(0xe881, "查看AndroidId", modifier = Modifier.weight(1f))
                QuickItem(0xe617, "查看系统版本", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe632, "查看IP地址", modifier = Modifier.weight(1f))
                QuickItem(0xe65d, "查看Mac地址", modifier = Modifier.weight(1f))
                QuickItem(0xe6b2, "重启手机", modifier = Modifier.weight(1f))
                QuickItem(0xe61e, "查看系统属性", modifier = Modifier.weight(1f))
            }
        }
    }

    /**
     * 应用相关
     */
    @Composable
    private fun AboutApp() {
        BaseQuick("应用相关", color = Color(0, 188, 212)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe740, "卸载应用", modifier = Modifier.weight(1f))
                QuickItem(0xe6af, "启动应用", modifier = Modifier.weight(1f))
                QuickItem(0xe875, "停止运行", modifier = Modifier.weight(1f))
                QuickItem(0xe7d6, "重启应用", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe613, "清除数据", modifier = Modifier.weight(1f))
                QuickItem(0xe633, "清除数据并重启应用", modifier = Modifier.weight(1f))
                QuickItem(0xe647, "重置权限", modifier = Modifier.weight(1f))
                QuickItem(0xe628, "重置权限并重启应用", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe66c, "授权所有权限", modifier = Modifier.weight(1f))
                QuickItem(0xe76d, "查看应用安装路径", modifier = Modifier.weight(1f))
                QuickItem(0xe60e, "保存Apk到电脑", modifier = Modifier.weight(1f))
                // 补全一个
                QuickItem(modifier = Modifier.weight(1f))
            }
        }
    }

    /**
     * 常用功能
     */
    @Composable
    private fun CommonFunction(device: String, onClick: (title: String, content: String) -> Unit) {
        BaseQuick("常用功能", color = Color(255, 152, 0)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickItem(0xe693, "安装应用", modifier = Modifier.weight(1f))
                QuickItem(0xe816, "输入文本", modifier = Modifier.weight(1f))
                QuickItem(0xe931, "截图保存到电脑", modifier = Modifier.weight(1f))
                QuickItem(0xe607, "查看当前Activity", modifier = Modifier.weight(1f).clickable {
                    onClick.invoke(
                        "查看当前Activity",
                        "adb -s $device shell dumpsys window | grep mCurrentFocus".runExec()
                    )
                })
            }
        }
    }

    @Composable
    private fun BaseQuick(
        title: String,
        color: Color = Color.Transparent,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Surface(
            modifier = Modifier.background(Color.White).fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(0.5.dp, Color(220, 220, 220)),  // 边框
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier.width(4.dp).height(16.dp).clip(RoundedCornerShape(8.dp)).background(color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title)
                }
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }

    @Composable
    private fun QuickItem(ttf: Int? = null, title: String? = null, modifier: Modifier = Modifier) {
        Column(modifier = modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (ttf == null || title == null) return@Column
            Box(
                modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(
                    randomColor[Random.nextInt(
                        IntRange(
                            0, randomColor.size - 1
                        )
                    )]
                ),
            ) {
                Text(
                    text = "${Char(ttf)}",
                    fontFamily = ttfFontFamily(),
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 12.sp)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun MessageDialog(title: String, content: String, clickDismiss: () -> Unit) {
        AlertDialog(onDismissRequest = {
            clickDismiss.invoke()
        }, buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = clickDismiss,
                ) {
                    Text("确定")
                }
            }
        }, title = {
            Text(title)
        }, text = {
            // text默认不支持复制黏贴 需要用SelectionContainer包裹
            SelectionContainer {
                Text(content)
            }
        })
    }
}