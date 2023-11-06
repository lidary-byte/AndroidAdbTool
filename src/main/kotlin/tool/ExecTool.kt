package tool

import okio.BufferedSource
import okio.buffer
import okio.source

var deviceId = ""
var packageName = ""

@Deprecated("")
fun String.runExec(): String {
    val p = Runtime.getRuntime().exec(this)
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}

fun Array<String>.runExec(): String {
    val p = Runtime.getRuntime().exec(this)
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}

fun MutableList<String>.runExecAndAdb(): String {
    val p = Runtime.getRuntime().exec(
        arrayOf("adb", "-s", deviceId).plus(this)
    )
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}

fun Array<String>.runExecAndAdbToBuffer(): BufferedSource {
    val p = Runtime.getRuntime().exec(this.toMutableList().apply {
        add(0, "adb")
        add(1, "-s")
        add(2, deviceId)
    }.toTypedArray())
    return p.inputStream.source().buffer()
}