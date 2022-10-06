package tool

import okio.buffer
import okio.source

fun String.runExec(): String {
    val p = Runtime.getRuntime().exec(this)
    return p.inputStream.source().buffer().readUtf8().trimEnd()
}