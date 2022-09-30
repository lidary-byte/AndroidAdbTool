import okio.buffer
import okio.source

object AdbTool {

    /**
     * 获取windows上安装的adb路径
     */
    fun adbPath(): String? {
        val p = Runtime.getRuntime().exec("adb version")
        val path = p.inputStream.source().buffer().readUtf8()
        if (path.contains("Installed as")) {
            return path.split("Installed as")[1].trim()
        }
        return null
    }
}