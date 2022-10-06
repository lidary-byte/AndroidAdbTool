package tool

import okio.buffer
import okio.source

fun String.runExec(): String {
    val p = Runtime.getRuntime().exec(this)
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}

fun Array<String>.runExec(): String {
    val p = Runtime.getRuntime().exec(this)
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}

fun Array<String>.runExecAndAdb(): String {
    val p = Runtime.getRuntime().exec(this.toMutableList().apply {
        add(0, "adb")
    }.toTypedArray())
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}