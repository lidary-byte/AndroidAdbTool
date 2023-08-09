package tool

fun String.findLevel() = when(this){
    "*:V"->"Verbose"
    "*:D"->"Debug"
    "*:I"->"Info"
    "*:W"->"Warn"
    "*:E"->"Error"
    else->"Verbose"
}