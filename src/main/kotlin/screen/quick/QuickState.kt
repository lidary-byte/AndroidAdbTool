package screen.quick

import MainState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bean.QuickBean
import bean.TipDialog

/**
 * @Author : lcc
 * @CreateData : 2024/2/28
 * @Description:ø
 */

@Stable
class QuickState(private val mainState: MainState) {
    var quickData by mutableStateOf<List<QuickBean>>(emptyList())

    // apk文件安装相关
    var showFileChooseDialog by mutableStateOf(false)


    var tipDialog by mutableStateOf<TipDialog?>(null)

    fun runExec(command: String) {
        runExecUnit { mainState.selectDevice?.executeShell(command) }
    }

    fun runExec(command: () -> String?): String? {
        var text: String? = null
        return try {
            command.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun runExecUnit(command: () -> Unit) {
        try {
            command.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        quickData = buildData()
    }
}


private fun buildData() = mutableListOf(
    QuickBean("安装应用", 0xe693, type = QuickBean.ADB_TYPE_INSTALL, commandList = mutableListOf("安装应用")),
    QuickBean("输入文本", 0xe816),
    QuickBean("截图保存电脑", 0xe931),
    QuickBean(
        "查看当前Activity",
        0xe607,
        type = QuickBean.ADB_TYPE_SHOW_DIALOG,
        commandList = mutableListOf("shell", "dumpsys activity activities | grep mResumedActivity"),
        command = "shell dumpsys activity activities | grep mResumedActivity"
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

    QuickBean(
        "查看系统版本",
        0xe617,
        mutableListOf("shell", "getprop", "ro.build.version.release"),
        type = QuickBean.ADB_TYPE_SHOW_DIALOG
    ),
    QuickBean("查看IP地址", 0xe632, mutableListOf("shell", "ifconfig"), type = QuickBean.ADB_TYPE_SHOW_DIALOG),
    // 需要提权
//                QuickBean(
//                    "查看Mac地址",
//                    0xe65d,
//                    mutableListOf("shell", "ip", "link", "show"),
//                    type = QuickBean.ADB_TYPE_SHOW_DIALOG
//                ),
    QuickBean("重启手机", 0xe6b2, mutableListOf("reboot")),
    QuickBean(
        "查看系统属性",
        0xe61e,
        mutableListOf("shell", "getprop"),
        type = QuickBean.ADB_TYPE_SHOW_DIALOG
    ),
    QuickBean("Home键", 0xe68e, mutableListOf("shell", "input", "keyevent", "3")),
    QuickBean("Back键", 0xe616, mutableListOf("shell", "input", "keyevent", "4")),
    QuickBean(
        "Menu键", 0xe605, mutableListOf(
            "shell",
            "input",
            "keyevent",
            "82",
        )
    ),
    QuickBean(
        "Power键", 0xe615, mutableListOf(
            "shell",
            "input",
            "keyevent",
            "26",
        )
    ),
    QuickBean("增加音量", 0xe76e, mutableListOf("shell", "input", "keyevent", "24")),
    QuickBean(
        "降低音量", 0xe771, mutableListOf(
            "shell",
            "input",
            "keyevent",
            "25"
        )
    ),
    QuickBean(
        "静音", 0xe612, mutableListOf(
            "shell",
            "input",
            "keyevent",
            "164"
        )
    ),
    QuickBean(
        "切换应用", 0xe658, mutableListOf(
            "shell",
            "input",
            "keyevent",
            "187"
        )
    )
)
