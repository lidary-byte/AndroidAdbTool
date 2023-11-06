package screen.quick

import bean.QuickBean
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tool.runExecAndAdb

class QuickScreenModel : ScreenModel {

    init {
        buildData()
    }

    private val _quickData = MutableStateFlow<List<QuickBean>>(emptyList())
    val quickData
        get() = _quickData.asStateFlow()


    fun runExec(position: Int, command: MutableList<String>) {
        screenModelScope.launch(Dispatchers.IO) {
            val a = command.runExecAndAdb()
        }
    }

    private fun buildData() {
        screenModelScope.launch(Dispatchers.IO) {
            _quickData.value = mutableListOf(
                QuickBean("安装应用", 0xe693),
                QuickBean("输入文本", 0xe816),
                QuickBean("截图保存电脑", 0xe931),
                QuickBean(
                    "查看当前Activity",
                    0xe607,
                    mutableListOf("shell", "dumpsys", "window", "|", "grep", " mCurrentFocus")
                ),
                QuickBean("卸载应用", 0xe740),
                QuickBean("启动应用", 0xe6af),
                QuickBean("停止运行", 0xe875),
                QuickBean("重启应用", 0xe7d6),
                QuickBean("清除数据", 0xe613),
                QuickBean("清除数据并重启应用", 0xe816),
                QuickBean("重置权限", 0xe647),
                QuickBean("重置权限并重启应用", 0xe628),
                QuickBean("授权所有权限", 0xe66c),
                QuickBean("查看应用安装路径", 0xe76d),
                QuickBean("保存Apk到电脑", 0xe60e),
                QuickBean("开始录屏", 0xe695),
                QuickBean("结束录屏保存到电脑", 0xe71d),
                QuickBean("查看AndroidId", 0xe881),
                QuickBean("查看系统版本", 0xe617),
                QuickBean("查看IP地址", 0xe632),
                QuickBean("查看Mac地址", 0xe65d),
                QuickBean("重启手机", 0xe6b2),
                QuickBean("查看系统属性", 0xe61e),
                QuickBean("Home键", 0xe68e, arrayListOf("shell", "input", "keyevent", "3")),
                QuickBean("Back键", 0xe616, arrayListOf("shell", "input", "keyevent", "4")),
                QuickBean(
                    "Menu键", 0xe605, arrayListOf(
                        "shell",
                        "input",
                        "keyevent",
                        "82",
                    )
                ),
                QuickBean(
                    "Power键", 0xe615, arrayListOf(
                        "shell",
                        "input",
                        "keyevent",
                        "26",
                    )
                ),
                QuickBean("增加音量", 0xe76e, arrayListOf("shell", "input", "keyevent", "24")),
                QuickBean(
                    "降低音量", 0xe771, arrayListOf(
                        "shell",
                        "input",
                        "keyevent",
                        "25"
                    )
                ),
                QuickBean(
                    "静音", 0xe612, arrayListOf(
                        "shell",
                        "input",
                        "keyevent",
                        "164"
                    )
                ),
                QuickBean(
                    "切换应用", 0xe658, arrayListOf(
                        "shell",
                        "input",
                        "keyevent",
                        "187"
                    )
                ),
                QuickBean("遥控器", 0xe796),
                QuickBean("向上滑动", 0xe795),
                QuickBean("向下滑动", 0xe603),
                QuickBean("向左滑动", 0xe60a),
                QuickBean("向右滑动", 0xe6ca),
                QuickBean("屏幕点击", 0xe697)
            )
        }
    }


}