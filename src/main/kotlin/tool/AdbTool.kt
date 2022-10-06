package tool

import okio.buffer
import okio.source

object AdbTool {

    /**
     * 获取windows上安装的adb路径
     */
    fun adbPath(): String? {
        return kotlin.runCatching {
            val p = Runtime.getRuntime().exec("adb version")
            val path = p.inputStream.source().buffer().readUtf8()
            if (path.contains("Installed as")) {
                return path.split("Installed as")[1].trim()
            }
            return null
        }.getOrNull()
    }


    /// Home键
    fun pressHome(deviceId: String) {
        arrayOf( "-s", deviceId, "shell", "input", "keyevent", "3"
        ).runExecAndAdb()
    }

    /// 返回键
    fun pressBack(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "4",
        ).runExecAndAdb()
    }

    /// 菜单键
    fun pressMenu(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "82",
        ).runExecAndAdb()
    }

    /// 增加音量
    fun pressVolumeUp(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "24",
        ).runExecAndAdb()
    }

    /// 减少音量
    fun pressVolumeDown(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "25",
        ).runExecAndAdb()
    }

    /// 静音
    fun pressVolumeMute(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "164",
        ).runExecAndAdb()
    }

    /// 电源键
    fun pressPower(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "26",
        ).runExecAndAdb()
    }

    /// 切换应用
    fun pressSwitchApp(deviceId: String) {
        arrayOf(
            "-s",
            deviceId,
            "shell",
            "input",
            "keyevent",
            "187",
        ).runExecAndAdb()
    }
}